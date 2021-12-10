package com.example.taskmanagerkotlin.utils

import com.example.taskmanagerkotlin.securityKey
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component

@Component
class JWTUtils {

    fun generateToken(userId: String): String {
        return Jwts.builder().setSubject(userId).signWith(SignatureAlgorithm.HS512, securityKey.toByteArray()).compact()
    }

    fun getClaimsToken(token: String) =
        try {
            Jwts.parser().setSigningKey(securityKey.toByteArray()).parseClaimsJws(token).body
        } catch (err: Exception) {
            null
        }
}