package com.gker.gkerlove.util;


import com.gker.gkerlove.bean.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;


@Component
public class JwtUtils {
    public static final long EXPIRE = 1000 * 60 * 60 * 24; // 过期时间为1天
    @Value("${JWT.appSecret}")
    public String APP_SECRET;

    public String getToken(User user) {
        return Jwts.builder().setHeaderParam("typ", "JWT").setHeaderParam("alg", "HS256").setSubject("user") // 分类
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                // token主体部分，存储用户ID
                .claim("id", user.getId())
                // 签名
                .signWith(SignatureAlgorithm.HS256, APP_SECRET).compact();
    }

    public boolean checkToken(String token) {
        if (!StringUtils.hasLength(token)) return true;
        try {
            Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getUserId(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("id");
    }
}