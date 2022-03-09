package com.sanri.test.testmvc.service;

import com.sanri.test.testmvc.exception.BusinessException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Component
public class UserService {
    private final static String base64Secret = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";

    /**
     * 简单登录
     * @param username
     * @param password
     * @return
     */
    public String login(String username, String password) {
        if("zs".equals(username)){
            String userId = "14";

            // 生成 jwts 验证字符串
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

            //生成签名密钥
            Key signingKey = signKey(signatureAlgorithm);

            // 过期时间, 现在时间加两天
            Date expireDate = DateUtils.addDays(new Date(), 2);

            JwtBuilder jwtBuilder = Jwts.builder().setHeaderParam("type", "JWT")
                    .claim("username", username)
                    .claim("userId", userId)
                    .signWith(signatureAlgorithm, signingKey)
                    .setExpiration(expireDate).setNotBefore(new Date());

            return jwtBuilder.compact();
        }
        throw BusinessException.create("用户验证失败");
    }

    public Key signKey(SignatureAlgorithm signatureAlgorithm) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Secret);
        return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    }
}
