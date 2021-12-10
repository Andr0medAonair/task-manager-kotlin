package com.example.taskmanagerkotlin.filters

import com.example.taskmanagerkotlin.utils.JWTUtils

class UserFilter {
    fun getUserId(token : String) : String?{
        val claims = JWTUtils().getClaimsToken(token)
        return claims?.subject
    }
}