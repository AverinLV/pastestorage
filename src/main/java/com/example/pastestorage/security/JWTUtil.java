package com.example.pastestorage.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


@Component
public class JWTUtil {
    private final String secret;
    private static final String ISSUER = "pastestorage";
    private static final String SUBJECT = "User details";
    private static final String CLAIM_NAME = "username";

    public JWTUtil(@Value("${jwt_secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(String username) {
        Instant expirationDate = Instant.now().plus(1, ChronoUnit.HOURS);

        return JWT.create()
                .withSubject(SUBJECT)
                .withClaim(CLAIM_NAME, username)
                .withIssuedAt(Instant.now())
                .withIssuer(ISSUER)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(SUBJECT)
                .withIssuer(ISSUER)
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim(CLAIM_NAME).asString();
    }
}
