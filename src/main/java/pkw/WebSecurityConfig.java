package pkw;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import pkw.services.LoginLogger;

import javax.sql.DataSource;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    private LoginLogger loginLogger;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/uzytkownik/**").access("hasAuthority('ADMINISTRATOR')")
                .antMatchers("/wybory/**").access("hasAnyAuthority('ADMINISTRATOR', 'CZLONEK_PKW')")
                .antMatchers("/komisje/**").access("hasAnyAuthority('ADMINISTRATOR', 'CZLONEK_PKW')")
                .antMatchers("/protokoly/**").access("hasAuthority('CZLONEK_OKW')")
                .antMatchers("/css/**", "/img/**", "/template/**", "/wyniki", "/wyniki/*","/","/kontakt")
                .permitAll()
                .anyRequest().authenticated()
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
            .jdbcAuthentication()
            .dataSource(dataSource).passwordEncoder(new ShaPasswordEncoder(256))
            .usersByUsernameQuery("select login as username, haslo as password, 1 as enabled from uzytkownicy where login=?")
            .authoritiesByUsernameQuery("select uzytkownicy.login as username, poziomy_dostepu.nazwa as role from uzytkownicy left join poziomy_dostepu on uzytkownicy.poziom_dostepu_id = poziomy_dostepu.id where login=?");
    }
}
