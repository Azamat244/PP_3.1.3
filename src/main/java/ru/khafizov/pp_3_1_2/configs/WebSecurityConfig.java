package ru.khafizov.pp_3_1_2.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import ru.khafizov.pp_3_1_2.security.UserDetailServiceImpl;
import ru.khafizov.pp_3_1_2.services.UserServiceImpl;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final SuccessUserHandler successUserHandler;
    private final EncoderConfig encoderConfig;
    private final UserDetailServiceImpl userDetailServiceImpl;

    @Autowired
    public WebSecurityConfig(SuccessUserHandler successUserHandler, UserServiceImpl userServiceImpl, EncoderConfig encoderConfig, UserDetailServiceImpl userDetailServiceImpl) {
        this.successUserHandler = successUserHandler;
        this.encoderConfig = encoderConfig;
        this.userDetailServiceImpl = userDetailServiceImpl;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.
                authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/user/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers("/").permitAll()

                )
                .formLogin(form -> form //логика перенаправления после успешной аутентификации
                        .successHandler(successUserHandler)
                        .permitAll()
                )
                .logout(logout -> logout //логика логаута
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .build();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(encoderConfig.passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailServiceImpl);
        return authenticationProvider;
    }
}