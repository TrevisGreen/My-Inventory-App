package com.myInventory.config;


import com.myInventory.utils.LoginHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

// Enable spring security
// @EnableWebSecurity
// Spring @Configuration annotation removed the need for an XML solution
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // Declaration of objects
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private LoginHandler loginHandler;

    // The password encoder used to store the password encrypted
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    // Method to ignore configurations for the following files and directories
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**",
                        "/crossdomain.xml",
                        "/robots.xml",
                        "/humans.xml",
                        "/404.html"
        );
    }

    // Gives specific roles access to directories defined in this method
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/resources/**","/crossdomain.xml","/robots.xml","/humans.xnl","/404.html").permitAll()
                .anyRequest().hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(loginHandler)
                .permitAll()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        auth
                .authenticationProvider(authenticationProvider)
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled, accountexpired, accountlocked, credentialsexpired, firstname, lastname from users where username = 'admin@kinetic.com'")
                .authoritiesByUsernameQuery("select users_username as username, roles_authority as authority from users_roles where users_username = ?");
    }

    // return user details service spring
    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailsService;
    }
}
