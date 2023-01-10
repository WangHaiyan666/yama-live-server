package com.newverse.yama.live.domain.common.client.apple;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.openidconnect.IdTokenVerifier;
import com.google.common.collect.Maps;
import com.newverse.yama.live.domain.utils.HttpClientUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liangyu
 */
@Slf4j
public class AppleIdTokenVerifier extends IdTokenVerifier {

    private static final String appleIssuerUrl = "https://appleid.apple.com";
    private static final String appleAuthUrl = "https://appleid.apple.com/auth/keys";

    private static Map<String, Map<String, String>> map;
    private static String kty;

    @NonNull
    private final Collection<String> audience;

    public AppleIdTokenVerifier(@NonNull Collection<String> audience) {
        this.audience = audience;
        try {
            // https://developer.apple.com/documentation/sign_in_with_apple/jwkset/keys
            ObjectMapper mapper = new ObjectMapper();
            String str = HttpClientUtil.doGet(appleAuthUrl);
            JsonNode data = mapper.readValue(str, JsonNode.class);
            JsonNode jsonArray = data.get("keys");
            map = new HashMap<>();
            if (jsonArray.isArray()) {
                for (JsonNode node : jsonArray) {
                    kty = node.get("kty").textValue();
                    Map<String, String> m = Maps.newHashMap();
                    m.put("n", node.get("n").textValue());
                    m.put("e", node.get("e").textValue());
                    map.put(node.get("kid").textValue(), m);
                }
            }
        } catch (Exception ex) {
            log.error("error while initial apple login util", ex);
        }
    }

    /**
     * 获取苹果的公钥
     *
     * @param kid 密钥类型
     * @return PublicKey
     */
    public PublicKey verifyPublicKey(String kid) {
        try {
            String n = map.get(kid).get("n");
            String e = map.get(kid).get("e");

            BigInteger modulus = new BigInteger(1, Base64.decodeBase64(n));
            BigInteger publicExponent = new BigInteger(1, Base64.decodeBase64(e));

            RSAPublicKeySpec spec = new RSAPublicKeySpec(modulus, publicExponent);
            //目前kty均为 "RSA"
            KeyFactory kf = KeyFactory.getInstance(kty);
            return kf.generatePublic(spec);
        } catch (Exception e) {
            log.error("getPublicKey error: ", e);
        }
        return null;
    }

    /**
     * 对前端传来的identityToken进行验证
     *
     * @param identityToken jwt
     * @return openId
     */
    public AppleIdToken verify(String identityToken) {
        try {
            // 1 解析
            val appleIdToken = AppleIdToken.parse(identityToken);

            // 2 生成publicKey
            PublicKey publicKey = verifyPublicKey(appleIdToken.getHeader().getKeyId());
            if (publicKey == null) {
                log.info("failed to get publicKey, idToken is {}", identityToken);
                return null;
            }
            // 3 验证  https://developer.apple.com/documentation/sign_in_with_apple/generate_and_validate_tokens
            if (!appleIdToken.verifyAudience(audience)) {
                log.info("audience is not correct {}", appleIdToken.getPayload().getAudience());
                return null;
            }

            Jws<Claims> claim = Jwts.parserBuilder().requireIssuer(appleIssuerUrl).requireAudience(appleIdToken.getPayload().getAudience().toString()).setSigningKey(publicKey).build().parseClaimsJws(identityToken);
            if (claim == null) {
                log.warn("苹果登陆授权 idToken 验证失败");
                return null;
            }

            //sub,即用户的Apple的openId
            if (StringUtils.isEmpty(claim.getBody().getSubject())) {
                log.warn("cannot get sub, id token is {}", identityToken);
                return null;
            }

            if (!appleIdToken.verifyIssuer(claim.getBody().getIssuer())) {
                log.warn("issuer is not correct, id token is {}", identityToken);
                return null;
            }

            if (!appleIdToken.verifyExpirationTime(System.currentTimeMillis(), 0)) {
                log.warn("idToken is expired, id token is {}", identityToken);
                return null;
            }

            return appleIdToken;
        } catch (ExpiredJwtException e) {
            log.warn("苹果登陆授权 idToken 已过期");
            return null;
        } catch (Exception e) {
            log.warn("非法的苹果登陆授权 idToken");
            return null;
        }
    }
}
