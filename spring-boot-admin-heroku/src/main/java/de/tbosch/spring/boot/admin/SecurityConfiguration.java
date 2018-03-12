package de.tbosch.spring.boot.admin;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import de.codecentric.boot.admin.server.config.AdminServerProperties;

@Configuration
public class SecurityConfiguration {

	@Configuration
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		private final String adminContextPath;

		public FormLoginWebSecurityConfigurerAdapter(AdminServerProperties adminServerProperties) {
			this.adminContextPath = adminServerProperties.getContextPath();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
			successHandler.setTargetUrlParameter("redirectTo");

			http.authorizeRequests()//
					.requestMatchers(EndpointRequest.to("info", "health")).permitAll()//
					.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")//
					.antMatchers(adminContextPath + "/assets/**").permitAll()//
					.antMatchers(adminContextPath + "/login").permitAll()//
					.anyRequest().authenticated()//
					.and()//
					.formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()//
					.logout().logoutUrl(adminContextPath + "/logout").and()//
					.httpBasic().and()//
					.csrf().disable();//
		}
	}

}