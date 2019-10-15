package com.gashu.mariadbspring;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@ComponentScan("com.concretepage")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@PropertySource("classpath:jdbc.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
        Environment env;
	@Autowired
	AuthenticationService authenticationService;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/info/**").hasAnyRole("ADMIN","USER").
		and().formLogin();
	}
        @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		BCryptPasswordEncoder bEncoder = new BCryptPasswordEncoder();
		// StandardPasswordEncoder encoder = new StandardPasswordEncoder();
		// ShaPasswordEncoder shaencoder = new ShaPasswordEncoder();
		// PasswordEncoder passencoder = new PasswordEncoder();
		// NoOpPasswordEncoder noop = NoOpPasswordEncoder.getInstance();
		auth.userDetailsService(authenticationService).passwordEncoder(bEncoder);	// System.out.println("bEncoder: "+ bEncoder.); // 
	}
        @Bean
	public DataSource getDataSource() {
	    BasicDataSource dataSource = new BasicDataSource();
	    dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
	    dataSource.setUrl(env.getProperty("jdbc.url"));
	    dataSource.setUsername(env.getProperty("jdbc.username"));
	    dataSource.setPassword(env.getProperty("jdbc.password"));
	    return dataSource;
	}
}   
