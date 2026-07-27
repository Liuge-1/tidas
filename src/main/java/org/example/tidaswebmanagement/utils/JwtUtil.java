package org.example.tidaswebmanagement.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.refresh-expiration}")
    private Long refreshExpiration;

    // 获取加密密钥
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成token，存入userId和role
     */
    public String generateToken(Integer userId, String role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userId.toString()) // 主体，存放用户id
                .claim("role", role)           // 存放角色
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 解析token，获取用户id
     */
    public Integer getUserIdByToken(String token) {
        Claims claims = getClaims(token);
        return Integer.parseInt(claims.getSubject());
    }

    /**
     * 解析token，获取用户角色
     */
    public String getRoleByToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }

    /**
     * 解析token，获取所有Claims
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 校验token是否合法、未过期
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成 refreshToken（7天有效期，包含userId和role）
     */
    public String generateRefreshToken(Integer userId, String role) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpiration);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 校验 refreshToken 是否合法、未过期
     */
    public boolean validateRefreshToken(String token) {
        return validateToken(token); // 复用相同的校验逻辑
    }
}