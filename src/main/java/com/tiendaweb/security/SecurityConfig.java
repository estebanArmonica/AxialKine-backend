package com.tiendaweb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    //Este bean va a encargarse de verificar la información de los usuarios que se loguearán en nuestra api
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //Con este bean nos encargaremos de encriptar todas nuestras contraseñas
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //Este bean incorporará el filtro de seguridad de json web token que creamos en nuestra clase anterior
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    //Vamos a crear un bean el cual va a establecer una cadena de filtros de seguridad en nuestra aplicación.
    // Y es aquí donde determinaremos los permisos segun los roles de usuarios para acceder a nuestra aplicación
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)) //Permitimos el manejo de excepciones y nos establece un punto de entrada personalizada de autenticacion
                .sessionManagement(sessionManagemen -> sessionManagemen.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Permite la gestión de sessiones
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/swagger-ui.html/**",
                                                           "/swagger-ui/**",
                                                           "/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/usuarios/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/categoria/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/estados/listar").hasAuthority("ADMIN")
                                .requestMatchers("/api/productos/**").hasAuthority("ADMIN")
                                .requestMatchers("/api/tareas/**").hasAuthority("ADMIN")
                                .requestMatchers( "/api/items/**").hasAuthority("USER")
                                .requestMatchers("/api/carrito/**").hasAuthority("USER")
                                .anyRequest().authenticated()
                ) //Toda petición http debe ser autorizada
                .httpBasic(httpBasic -> {});
        http.addFilterBefore(jwtAuthenticationFilter() , UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
