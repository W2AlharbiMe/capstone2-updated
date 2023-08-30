package com.example.capstone2updated.Configuration;

import com.example.capstone2updated.Service.SecurityUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final SecurityUserDetailsService securityUserDetailsService;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(securityUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authenticationProvider(daoAuthenticationProvider())

                .authorizeHttpRequests()


                // specific endpoints

                .requestMatchers(HttpMethod.POST, "/api/v1/auth/create/sales-person").permitAll()

                // the following will allow users with SALES_PERSON role to only access GET endpoints.
                .requestMatchers(HttpMethod.GET, "/api/v1/inventories/**").hasAnyAuthority("SALES_PERSON", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/inventory-items/**").hasAnyAuthority("SALES_PERSON", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/cars/**").hasAnyAuthority("SALES_PERSON", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/parts/**").hasAnyAuthority("SALES_PERSON", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/manufacturers/**").hasAnyAuthority("SALES_PERSON", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/dealer-services/**").hasAnyAuthority("SALES_PERSON", "ADMIN")

                // the following is used to view the currently logged in sales person details.
                .requestMatchers(HttpMethod.GET, "/api/v1/sales-persons/@me").hasAnyAuthority("SALES_PERSON", "ADMIN")





                // the user with ADMIN can do everything view, edit, delete.
                .requestMatchers("/api/v1/cars/**").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/dealer-services/**").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/inventories/**").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/inventory-items/**").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/manufacturers/**").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/parts/**").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/sales-persons/**").hasAuthority("ADMIN")

                .requestMatchers("/api/v1/sales-invoices/**").hasAnyAuthority("SALES_PERSON", "ADMIN")
                .requestMatchers("/api/v1/customers/**").hasAnyAuthority("SALES_PERSON", "ADMIN")



                .anyRequest().authenticated()

                .and()

                .logout().logoutUrl("/api/v1/auth/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)

                .and()

                .httpBasic()
        ;

        return http.build();
    }

}
