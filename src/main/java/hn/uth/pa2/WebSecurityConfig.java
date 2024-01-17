/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hn.uth.pa2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Buddys
 */
@Configuration

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    String[] resources = new String[]{
        "/include/**", "/css/**", "/icons/**", "/imagenes/**", "/js/**", "/layer/**"
    };
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http
        .authorizeRequests()
        .antMatchers(resources).permitAll()
        .antMatchers("/","/index").permitAll() 
            .anyRequest().authenticated() 
            .and() 
        .formLogin()
            .loginPage("/login")
            .permitAll()
            .defaultSuccessUrl("/menuInicio")
            .failureUrl("/login?error=true") 
            .usernameParameter("username") 
            .passwordParameter("contrasenia") 
            .and() 
            .csrf().disable()
        .logout()
            .permitAll() 
            .logoutSuccessUrl("/login?logout");
    }
    
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        //Nivel de encriptacion 4, exiten niveles desde 4 - 31
		bCryptPasswordEncoder = new BCryptPasswordEncoder(4);
        return bCryptPasswordEncoder;
    }
    
    @Autowired
    UserDetailsService userDetailsService;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { 
    	//Especificar el encargado del login y encriptacion del password
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
