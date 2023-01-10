package com.newverse.yama.live.domain.utils;

import com.newverse.yama.live.domain.constant.SecurityConstants;
import com.newverse.yama.live.domain.constant.TokenConstants;
import com.newverse.yama.live.domain.text.Convert;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 使用Jwt从header中快速提取uid信息
 *
 * @author liangyu
 */
@Slf4j
public class JwtUtil {
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

    public static final String secret = TokenConstants.SECRET;


    /**
     * null for invalid or expired token
     *
     * @param request 请求
     * @return null or uid
     */
    public static String parseJwtFromRequest(NativeWebRequest request) {
        String header = request.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        return header.replace(TOKEN_PREFIX, "");
    }

    public static String parseJwtFromHttpRequest(HttpServletRequest request) {
        String header = request.getHeader(HEADER_STRING);
        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            return null;
        }
        return header.replace(TOKEN_PREFIX, "");
    }

    public static String parseFromSourceFromHttpRequest(HttpServletRequest request) {
        String header = request.getHeader(SecurityConstants.FROM_SOURCE);
        if (header == null) {
            return null;
        }
        return header;
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> claims)
    {
        String token = Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    public static Claims parseToken(String token)
    {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * 根据令牌获取用户标识
     *
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserKey(String token)
    {
        Claims claims = parseToken(token);
        return getValue(claims, SecurityConstants.USER_KEY);
    }

    /**
     * 根据身份信息获取键值
     *
     * @param claims 身份信息
     * @param key 键
     * @return 值
     */
    public static String getValue(Claims claims, String key)
    {
        return Convert.toStr(claims.get(key), "");
    }
}
