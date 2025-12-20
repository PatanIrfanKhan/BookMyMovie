package com.example.BookMyMovie.SecurityConfig;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SpringSecurity {


    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }


//    @Autowired
    private final UserDetailServiceImplementation userdetailservice;

    @Autowired
    private JWTFilter jwtFilter;

    public SpringSecurity(UserDetailServiceImplementation userdetailservice)
    {
        this.userdetailservice=userdetailservice;
    }

    @Bean
    AuthenticationManager authenticationManager()
    {
        DaoAuthenticationProvider authprovider=new DaoAuthenticationProvider();
        authprovider.setPasswordEncoder(passwordEncoder());
        authprovider.setUserDetailsService(userdetailservice);
        return new ProviderManager(authprovider);
    }
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
      return http.authorizeHttpRequests(auth->auth.requestMatchers("/location/**","/Movie/**","/Theatre/**").hasAnyRole("ADMIN")
                      .requestMatchers("/user/**","/BookTickets/**").hasAnyRole("ADMIN","USER")
                      .anyRequest().permitAll())
//              .httpBasic(Customizer.withDefaults()) ---> Basic Authentication
              .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)//---->JWT Authentication
               .csrf(csrf->csrf.disable())
              .cors(cors -> cors.configurationSource(corsConfigurationSource()))
              .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
    }
    @Bean //When frontend is separate from backend use the cors for running on the same server ex;localohost:5050
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
    @Bean
    RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

//    @Bean
//    public StringRedisTemplate stringRedisTemplate()
//    {
//        return new StringRedisTemplate();
//    }




//    @Bean
//    KafkaTemplate kafkaTemplate()
//    {
//        return new KafkaTemplate();
//    }
    
}
