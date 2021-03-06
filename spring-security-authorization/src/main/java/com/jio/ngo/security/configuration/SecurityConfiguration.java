package com.jio.ngo.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception 
	{
		auth.inMemoryAuthentication()
		.withUser("Bhaumik")
		.password("Bhaumik")
		.roles("SON")
		.and()
		.withUser("Nandini")
		.password("Nandini")
		.roles("WIFE");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception 
	{
//		http.authorizeRequests()
//		.antMatchers("/").permitAll()
//		.antMatchers("/**").hasRole("WIFE")
//		.and().formLogin();
		http.authorizeRequests()
		.antMatchers("/user").hasRole("SON")
		.antMatchers("/admin").hasAnyRole("SON","WIFE")
		.antMatchers("/").permitAll()
		.and().formLogin();
	}
	
	@Bean
	public PasswordEncoder getPasswordEncoder()
	{
		return NoOpPasswordEncoder.getInstance();
	}
}
