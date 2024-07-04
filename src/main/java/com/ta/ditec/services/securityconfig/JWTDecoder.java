package com.ta.ditec.services.securityconfig;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JWTDecoder {

    public static void main(String[] args) {
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhLXJlenphcXVlQGFzc2FtLmdvdi5pbiIsImlhdCI6MTcxMzUxMDU5OCwiZXhwIjoxNzEzNTk2OTk4fQ.bLsbj99hpcT5Sn4edJU3RKxgsQmgMFPT3Mm-O67ioAw";
        String secretKey = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3"; // Replace

        String[] splitString = jwtToken.split("\\.");
        String base64EncodedHeader = splitString[0];
        String base64EncodedBody = splitString[1];
        String base64EncodedSignature = splitString[2];

        System.out.println("------------ Decode JWT ------------");

        Base64.Decoder decoder = Base64.getUrlDecoder();

        // Decode header and body
        String header = new String(decoder.decode(base64EncodedHeader));
        String body = new String(decoder.decode(base64EncodedBody));

        System.out.println("JWT Header: " + header);
        System.out.println("JWT Body: " + body);

        // Verify signature
        try {
            Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
            byte[] secretKeyBytes = secretKey.getBytes();
            Key secretKeySpec = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
            hmacSHA256.init(secretKeySpec);
            byte[] signatureBytes = hmacSHA256.doFinal((base64EncodedHeader + "." + base64EncodedBody).getBytes());
            String calculatedSignature = new String(Base64.getUrlEncoder().withoutPadding().encode(signatureBytes));

            System.out.println("JWT Signature: " + base64EncodedSignature);
            System.out.println("Calculated Signature: " + calculatedSignature);

            if (calculatedSignature.equals(base64EncodedSignature)) {
                System.out.println("Signature verification successful.");
            } else {
                System.out.println("Signature verification failed.");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
