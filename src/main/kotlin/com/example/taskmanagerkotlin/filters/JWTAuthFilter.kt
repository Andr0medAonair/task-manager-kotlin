package com.example.taskmanagerkotlin.filters

import com.example.taskmanagerkotlin.Repository.UserRepository
import com.example.taskmanagerkotlin.authorization
import com.example.taskmanagerkotlin.bearer
import com.example.taskmanagerkotlin.impl.UserDetails
import com.example.taskmanagerkotlin.utils.JWTUtils
import com.example.taskmanagerkotlin.validators.TokenValidator
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthFilter(authenticationManager: AuthenticationManager, val jwtUtils: JWTUtils, private val userRepository: UserRepository) :
    BasicAuthenticationFilter(authenticationManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val auth = request.getHeader(authorization)

        if (auth != null && auth.startsWith(bearer)) {
            val authorized = getAuth(auth)
            SecurityContextHolder.getContext().authentication = authorized
        }

        chain.doFilter(request, response)
    }

    private fun getAuth(auth: String): UsernamePasswordAuthenticationToken {
        val token = auth.substring(7)
        if (TokenValidator().isTokenValid(token)) {
            val idString = UserFilter().getUserId(token)
            if (!idString.isNullOrEmpty() && !idString.isNullOrBlank()){
                val user = userRepository.findByIdOrNull(idString.toLong()) ?: throw UsernameNotFoundException("User not found.")
//                val user = User(idString.toLong(), "Joan Fontaine", "joan@fontaine.com", "rebecca")
                val userImpl = UserDetails(user)
                return UsernamePasswordAuthenticationToken(userImpl, null, userImpl.authorities)
            }
        }
        throw UsernameNotFoundException(HttpStatus.UNAUTHORIZED.reasonPhrase)
    }
}