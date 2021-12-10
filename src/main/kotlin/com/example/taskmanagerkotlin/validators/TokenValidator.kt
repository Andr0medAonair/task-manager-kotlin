package com.example.taskmanagerkotlin.validators

import com.example.taskmanagerkotlin.utils.JWTUtils
import org.springframework.stereotype.Component

@Component
class TokenValidator {

    fun isTokenValid(token: String): Boolean {
        val claims = JWTUtils().getClaimsToken(token)
        if (claims != null) {
            val subjectId = claims.subject
            if (!subjectId.isNullOrEmpty() && !subjectId.isNullOrBlank())
                return true
        }
        return false
    }

}