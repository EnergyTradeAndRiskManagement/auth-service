package com.trading.authservice;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

public class AuthserviceApplicationTests {

    public static void main(String[] args) {
        String secret = "mySuperSecretFallbackKeyThatIsLongEnoughToPreventErrors123456!";
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        System.out.println("Key algorithm: " + key.getAlgorithm());
        System.out.println("Key format: " + key.getFormat());
        System.out.println("Key encoded length: " + key.getEncoded().length);

        String token = Jwts.builder()
                .subject("testuser")
                .claim("roles", List.of("USER"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("Generated token: " + token);
        String[] parts = token.split("\\.");
        String header = new String(java.util.Base64.getUrlDecoder().decode(parts[0]));
        String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
        System.out.println("Header: " + header);
        System.out.println("Payload: " + payload);
    }

}
