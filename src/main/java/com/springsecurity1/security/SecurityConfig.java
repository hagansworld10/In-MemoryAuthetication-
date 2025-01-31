package com.springsecurity1.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails normalUser = User.builder()
                .username("john")
                .password("{noop}test1111")
                .roles("EMPLOYEE")
                .build();

        UserDetails Manager = User.builder()
                .username("mary")
                .password("{noop}test2222")
                .roles("MANAGER","EMPLOYEE")
                .build();

        UserDetails Admin = User.builder()
                .username("susan")
                .password("{noop}test3333")
                .roles("MANAGER","EMPLOYEE","ADMIN")
                .build();

        return  new InMemoryUserDetailsManager(normalUser, Manager, Admin);
    }

    /**
     * @param httpSecurity
     * @return
     * @throws Exception
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity ) throws Exception {
        //                                .logoutUrl("/logout")
        //                                .logoutSuccessUrl("/login?logout=true")
        httpSecurity
                    .authorizeHttpRequests(registry -> {

                        registry
                                .requestMatchers("/home").hasRole("EMPLOYEE")
                                .requestMatchers("/leaders/**").hasRole("MANAGER")
                                .requestMatchers("/systems/**").hasRole("ADMIN")
                                .anyRequest().authenticated();
                    })
                    .formLogin(form -> {
                        form
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll();
                    })
                    .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(exception -> {
                    exception
                            .accessDeniedPage("/access-denied");

                });
                 return httpSecurity.build();


    }
}
