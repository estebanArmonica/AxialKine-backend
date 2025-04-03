package com.tiendaweb.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class ConstantesSeguridad {
    // equivale a 15 min
    public static final long JWT_EXPIRATION_TOKEN = 900000;

    //@Value("${JWT_FIRMA}")
    public static final SecretKey JWT_FIRMA = Keys.secretKeyFor(SignatureAlgorithm.HS512);
}
