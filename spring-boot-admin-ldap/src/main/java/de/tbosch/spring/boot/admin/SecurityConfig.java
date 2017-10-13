package de.tbosch.spring.boot.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Page with login form is served as /login.html and does a POST on
		// /login
		http.formLogin().loginPage("/login.html").loginProcessingUrl("/login").permitAll();
		// The UI does a POST on /logout on logout
		http.logout().logoutUrl("/logout");
		// The ui currently doesn't support csrf
		http.csrf().disable();

		// Requests for the login page and the static assets are allowed
		http.authorizeRequests().antMatchers("/login.html", "/**/*.css", "/img/**", "/third-party/**").permitAll();
		// ... and any other request needs to be authorized
		http.authorizeRequests().antMatchers("/**").authenticated();

		// Enable so that the clients can authenticate via HTTP basic for
		// registering
		http.httpBasic();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication()//
				.userSearchFilter("(uid={0})")//
				.userSearchBase("dc=example,dc=com")//
				.contextSource()//
				.url("ldap://localhost:10389");
	}

}