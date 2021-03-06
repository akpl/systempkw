package pkw;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import pkw.authentication.CustomUserDetailsService;
import pkw.repositories.UzytkownikRepository;
import pkw.services.LoginLogger;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackageClasses = {UzytkownikRepository.class, CustomUserDetailsService.class})
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    private LoginLogger loginLogger;

    @Autowired
    private UserDetailsService customUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/panel/uzytkownik/**").access("hasAuthority('ADMINISTRATOR')")
                .antMatchers("/panel/wybory/**").access("hasAnyAuthority('ADMINISTRATOR', 'CZLONEK_PKW')")
                .antMatchers("/panel/komisje/**").access("hasAnyAuthority('ADMINISTRATOR', 'CZLONEK_PKW')")
                .antMatchers("/panel/protokoly/**").access("hasAuthority('CZLONEK_OKW')")
                .antMatchers("/panel", "/panel/", "/panel/**").authenticated()
                .anyRequest().permitAll()
                .and()
            .formLogin()
                .successHandler(loginLogger)
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll().logoutUrl("/logout").logoutSuccessUrl("/logout-success");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService).passwordEncoder(new ShaPasswordEncoder(256));
    }
}
