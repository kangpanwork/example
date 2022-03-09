package com.sanri.test.sometest;

import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.Key;
import java.util.Date;

public class JwtMain {

    private final static String base64Secret = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";
    private final static int expiresSecond = 172800000;

    public static Claims parseJWT(String jsonWebToken) throws ExpiredJwtException {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Secret))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
    }

    public static String createJWT(String username, String roles, String privileges) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("user_name", username)
                .claim("user_role", roles)
                .claim("user_privilege", privileges)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间 2 天
        if (expiresSecond >= 0) {
            long expMillis = nowMillis + expiresSecond;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        //生成JWT
        return builder.compact();
    }

    @Test
    public void testGen(){
        String sanri = createJWT("sanri", "1,2,3", "1,23,4,5,65");
        System.out.println(sanri);
    }

    @Test
    public void testParse(){
//        System.out.println(parseJWT("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX25hbWUiOiJzYW5yaSIsInVzZXJfcm9sZSI6IjEsMiwzIiwidXNlcl9wcml2aWxlZ2UiOiIxLDIzLDQsNSw2NSIsImV4cCI6MTU3NzgzODc0OCwibmJmIjoxNTc3NjY1OTQ4fQ.fYaqelUUeQaUcqsRxNIVKda2rHrWEpVBM4PrUw77lL8"));
        System.out.println(parseJWT("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyX25hbWUiOiJzYW5yaSIsInVzZXJfcm9sZSI6IjEsMiwzIiwidXNlcl9wcml2aWxlZ2UiOiIxLDIzLDQsNSw2NSIsImV4cCI6MTU5MTMxNzkxNCwibmJmIjoxNTkxMTQ1MTE0fQ.th8t805G5TFNs9QF1rVvP14IuRoQQppWdzkM3J8w6h4"));
    }

    @Test
    public void testPassword(){
        byte[] bytes = Base64.decodeBase64(base64Secret);
//        String s = Hex.encodeHexString(bytes);
//        System.out.println(s);
    }
}