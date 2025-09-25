package com.longlong.user.utils;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @program: 极度真实还原大麦网高并发实战项目。 添加 阿星不是程序员 微信，添加时备注 大麦 来获取项目的完整资料
 * @description: token工具
 * @author: 阿星不是程序员
 **/
@Slf4j
public class JWTUtils {

    private static final long Expiration = 86400L;
    public static final String TokenPrefix = "Bearer ";
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    public static final String tokenSecret = "SecretKey039245678901232039487623456783092349288901402967890140939827";


    public static String generateAccessToken(String id, String info) {

        long nowMillis = System.currentTimeMillis();


        JwtBuilder jwtToken = Jwts.builder()
                .setId(id)
                .setIssuedAt(new Date())
                .setSubject(info)
                .signWith(SIGNATURE_ALGORITHM, tokenSecret)
                .setExpiration(new Date(System.currentTimeMillis() + Expiration * 1000));
        return TokenPrefix + jwtToken;
    }

    public static String parseJwtToken(String jwtToken) {
        if (StringUtils.hasText(jwtToken)) {
            String actualJwtToken = jwtToken.replace(TokenPrefix, "");
            try {
                Claims claims = Jwts.parser().
                        setSigningKey(tokenSecret).
                        parseClaimsJws(actualJwtToken).
                        getBody();
                Date expiration = claims.getExpiration();
                if (expiration.after(new Date())) {
                    String subject = claims.getSubject();
                    return subject;
                }
            }catch (ExpiredJwtException jwtException) {
                log.error("parseToken error",jwtException);
                throw new RuntimeException("token已过期");
            }
        }
        return null;
    }

}