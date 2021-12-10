package com.example.taskmanagerkotlin.configurations

import com.example.taskmanagerkotlin.Repository.UserRepository
import com.example.taskmanagerkotlin.filters.JWTAuthFilter
import com.example.taskmanagerkotlin.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfiguration : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var jwtUtils: JWTUtils

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun configure(http: HttpSecurity) {
        http.csrf().disable().authorizeRequests()
            .antMatchers(HttpMethod.POST, "/api/login").permitAll()
            .antMatchers(HttpMethod.POST, "/api/user").permitAll()
            .anyRequest().authenticated()

        http.cors().configurationSource(configurationCors())
        http.addFilter(JWTAuthFilter(authenticationManager(), jwtUtils, userRepository))
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun configurationCors(): CorsConfigurationSource? {
        println("THE CORRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRS")
        val config = CorsConfiguration()
        println("caofig" + config)
        config.allowedOrigins = mutableListOf("*")
        config.allowedMethods = mutableListOf("*")
        config.allowedHeaders = mutableListOf("*")
        val source = UrlBasedCorsConfigurationSource()
        println("sórce" + source)
        source.registerCorsConfiguration("/**", config)
        return source
    }
}