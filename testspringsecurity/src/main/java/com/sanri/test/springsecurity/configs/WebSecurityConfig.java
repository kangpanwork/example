package com.sanri.test.springsecurity.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Bean
//    public UserDetailsService userDetailsService(){
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        String passwordEncode = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("h123");
//        log.info(passwordEncode);
//        inMemoryUserDetailsManager.createUser(User.withUsername("9420").password(passwordEncode).roles("p1").build());
//        inMemoryUserDetailsManager.createUser(User.withUsername("sanri").password(passwordEncode).roles("p2").build());
//        return inMemoryUserDetailsManager;
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService());
        auth.inMemoryAuthentication().withUser("9420").password(new BCryptPasswordEncoder().encode("123")).authorities("p1")
        .and()
                .withUser("sanri").password(new BCryptPasswordEncoder().encode("123")).authorities("p2")
        ;

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/r/r1").hasAuthority("p1")
                .antMatchers("/r/r2").hasAuthority("p2")
                .anyRequest().permitAll()
                .and()
                .formLogin()
        ;
    }
}
