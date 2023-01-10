package com.newverse.yama.live.domain.common.client.apple;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.json.gson.GsonFactory;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;

/**
 * @author liangyu
 */
public class AppleIdToken extends IdToken {

    /**
     * @param header             header
     * @param payload            payload
     * @param signatureBytes     bytes of the signature
     * @param signedContentBytes bytes of the signature content
     */
    public AppleIdToken(Header header, Payload payload, byte[] signatureBytes, byte[] signedContentBytes) {
        super(header, payload, signatureBytes, signedContentBytes);
    }

    public static AppleIdToken parse(@NonNull String idToken) throws IOException {
        val parsedIdToken = IdToken.parse(new GsonFactory(), idToken);
        return new AppleIdToken(
                parsedIdToken.getHeader(),
                parsedIdToken.getPayload(),
                parsedIdToken.getSignatureBytes(),
                parsedIdToken.getSignedContentBytes());
    }

    public String getEmail() {
        return super.getPayload().get("email").toString();
    }

    public String getEmailVerified() {
        return super.getPayload().get("email_verified").toString();
    }
}
