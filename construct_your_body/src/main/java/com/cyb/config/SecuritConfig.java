package com.cyb.config;

import java.text.Normalizer.Form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.cyb.customSuccessHandeler.CustSuccessHandeler;
import com.cyb.entity.User;

@Configuration
@EnableWebSecurity
public class SecuritConfig {
	
	@Autowired
	private final CustSuccessHandeler customSuccessHandeler;
	

	public SecuritConfig(CustSuccessHandeler customSuccessHandeler) {
		super();
		this.customSuccessHandeler = customSuccessHandeler;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecur) throws Exception{
		
		httpSecur
//			.csrf().disable()
			.authorizeHttpRequests(auth ->auth
					.requestMatchers("/","/register").permitAll()
					.requestMatchers("/user_css/header.css").permitAll()
					.requestMatchers("index.css","/user_css/**","/admin_css/**").permitAll()
					.requestMatchers("/admin/**").hasRole("ADMIN")
					.requestMatchers("/user/**").hasRole("USER")
					.anyRequest().authenticated()
				)
			.formLogin(form -> form
					.loginPage("/login")
					
//					.defaultSuccessUrl("/",true)
					.successHandler(customSuccessHandeler)
					.permitAll()
				)
			.logout(logout->logout
					.logoutUrl("/logout")
					.logoutSuccessUrl("/")
					.permitAll()
				);
		
		return httpSecur.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		
		UserDetails adminDetails = org.springframework.security.core.userdetails.User.withUsername("ad@gmail.com")
				.password(passwordEncoder().encode("ad"))
				.roles("ADMIN")
				.build();
		
		UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("123saktisingh@gmail.com")
				.password(passwordEncoder().encode("sd"))
				.roles("USER")
				.build();
		
		return new InMemoryUserDetailsManager(adminDetails,userDetails);
	}
	
}
