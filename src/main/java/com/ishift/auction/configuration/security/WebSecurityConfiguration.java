package com.ishift.auction.configuration.security;

import com.ishift.auction.configuration.security.entrypoint.JwtTokenAdminEntryPoint;
import com.ishift.auction.configuration.security.entrypoint.JwtTokenEntryPoint;
import com.ishift.auction.configuration.security.filter.JwtAdminAuthenticationFilter;
import com.ishift.auction.configuration.security.filter.JwtAuthenticationFilter;
import com.ishift.auction.configuration.security.filter.JwtFarmAuthenticationFilter;
import com.ishift.auction.configuration.security.handler.AdminUserAccessDeniedHandler;
import com.ishift.auction.configuration.security.handler.BidUserAccessDeniedHandler;
import com.ishift.auction.configuration.security.provider.AdminUserAuthenticationProvider;
import com.ishift.auction.interceptor.CustomBeforeAuthenticationFilter;
import com.ishift.auction.service.admin.user.AdminUserDetailsService;
import com.ishift.auction.service.user.BidUserDetailsService;
import com.ishift.auction.service.user.FarmUserDetailsService;
import com.ishift.auction.util.Constants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;


@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AdminUserAuthenticationProvider adminUserAuthenticationProvider;
	
	@Autowired
	private AdminUserDetailsService adminUserDetailsService;
	
	@Autowired
	private BidUserDetailsService bidUserDetailsService;
	
	@Autowired
	private FarmUserDetailsService farmUserDetailsService;

	/**
	 * 정적 자원은 security 설정을 적용하지 않도록 추가
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers(headers -> headers.cacheControl(cache -> cache.disable()));
	}
	
	// 관리자 권한 체크
	@Order(1)
	@Configuration
	@RequiredArgsConstructor
	public static class AdminConfigurationAdapter extends WebSecurityConfigurerAdapter {
		
		@Autowired
		private JwtAdminAuthenticationFilter jwtAdminAuthenticationFilter;

		@Autowired
		private AdminUserAccessDeniedHandler adminUserAccessDeniedHandler;

		@Autowired
		private JwtTokenAdminEntryPoint jwtTokenAdminEntryPoint;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			log.debug("##### AdminConfigurationAdapter.configure [s] #####");

			RequestMatcher csrfRequestMatcher = new RequestMatcher() {
				// Disable CSFR protection on the following urls:
				private AntPathRequestMatcher[] requestMatchers = {
					new AntPathRequestMatcher("/proxy.do"),
					new AntPathRequestMatcher("/logout"),
					new AntPathRequestMatcher("/login/**"),
					new AntPathRequestMatcher("/management/**"),
					new AntPathRequestMatcher("/"),
					new AntPathRequestMatcher("/static/**"),
					new AntPathRequestMatcher("/favicon.ico"),
				};

				@Override
				public boolean matches(HttpServletRequest request) {
					// If the request match one url the CSFR protection will be disabled
		//			for (AntPathRequestMatcher rm : requestMatchers) {
		//				if (rm.matches(request)) { return false; }
		//			}
		//			return true;
					return false;
					
				} // method matches

			}; // new RequestMatcher

			// ROLE이 ROLE_로 시작하면 Exception을 리턴함
			http.cors()
			.and()
				.requestMatchers()
					.antMatchers("/office/**")
					.antMatchers("/api/**")
					.antMatchers("/daemon/api/**")
					.antMatchers("/kiosk/api/**")
			.and()
				.csrf()
					.requireCsrfProtectionMatcher(csrfRequestMatcher)
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.and()
				// jwt 인증을 사용하므로 세션을 사용하지 않는다.
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
					.antMatchers("/office/user/loginProc").permitAll()
					.antMatchers("/office/redirect", "/office/auction/board", "/office/auction/bidInfo", "/office/auction/videoStream").permitAll()
					.antMatchers("/api/**/auth/**", "/api/appversion", "/api/**/biz/**", "/api/**/my/**", "/api/**/host/**").permitAll()
					.antMatchers("/office/**").hasRole(Constants.UserRole.ADMIN)
					.antMatchers("/api/**").hasRole(Constants.UserRole.ADMIN)
					.antMatchers("/daemon/api/**/login").permitAll()
					.antMatchers("/daemon/api/**").hasRole(Constants.UserRole.ADMIN)
					.antMatchers("/kiosk/api/**/login").permitAll()
					.antMatchers("/kiosk/api/**").hasRole(Constants.UserRole.ADMIN)
					.anyRequest().permitAll()
			.and()
				.exceptionHandling()
					.accessDeniedHandler(adminUserAccessDeniedHandler)
					.authenticationEntryPoint(jwtTokenAdminEntryPoint)
			.and()
				.formLogin()
					.loginPage("/office/user/login").permitAll();

			http.addFilterBefore(jwtAdminAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
			
			http.headers(headers -> headers.cacheControl(cache -> cache.disable()));
			log.debug("##### AdminConfigurationAdapter.configure [e] #####");
		}
	}
	
	// 중도매인 권한 체크
	@Order(2)
	@Configuration
	@RequiredArgsConstructor
	public static class BidderConfigurationAdapter extends WebSecurityConfigurerAdapter {
		
		@Autowired
		private JwtAuthenticationFilter jwtAuthenticationFilter;
		
		@Autowired
		private JwtFarmAuthenticationFilter jwtFarmAuthenticationFilter;

		@Autowired
		private BidUserAccessDeniedHandler bidUserAccessDeniedHandler;

		@Autowired
		private JwtTokenEntryPoint jwtTokenEntryPoint;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			log.debug("##### BidderConfigurationAdapter.configure [s] #####");

			RequestMatcher csrfRequestMatcher = new RequestMatcher() {
				// Disable CSFR protection on the following urls:
				private AntPathRequestMatcher[] requestMatchers = {
					new AntPathRequestMatcher("/proxy.do"),
					new AntPathRequestMatcher("/logout"),
					new AntPathRequestMatcher("/login/**"),
					new AntPathRequestMatcher("/management/**"),
					new AntPathRequestMatcher("/"),
					new AntPathRequestMatcher("/static/**"),
					new AntPathRequestMatcher("/favicon.ico"),
				};

				@Override
				public boolean matches(HttpServletRequest request) {
					// If the request match one url the CSFR protection will be disabled
		//			for (AntPathRequestMatcher rm : requestMatchers) {
		//				if (rm.matches(request)) { return false; }
		//			}
		//			return true;
					return false;
				} // method matches

			}; // new RequestMatcher

			// ROLE이 ROLE_로 시작하면 Exception을 리턴함
			http.cors()
			.and()
				.csrf()
					.requireCsrfProtectionMatcher(csrfRequestMatcher)
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			.and()
				// jwt 인증을 사용하므로 세션을 사용하지 않는다.
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
					.antMatchers("/my/bid").hasRole(Constants.UserRole.BIDDER)
					.antMatchers("/my/buy").hasRole(Constants.UserRole.BIDDER)
					.antMatchers("/bid").hasRole(Constants.UserRole.BIDDER)
					.antMatchers("/my/entry").hasRole(Constants.UserRole.FARM)
					.anyRequest().permitAll()
			.and()
				.exceptionHandling()
					.accessDeniedHandler(bidUserAccessDeniedHandler)
					.authenticationEntryPoint(jwtTokenEntryPoint)
			.and()
				.formLogin()
					.loginPage("/user/login").permitAll();

			http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtFarmAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

			http.headers(headers -> headers.cacheControl(cache -> cache.disable()));
			log.debug("##### BidderConfigurationAdapter.configure [e] #####");
		}
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// 관리자
		auth.authenticationProvider(adminUserAuthenticationProvider)
			.userDetailsService(adminUserDetailsService)
			.passwordEncoder(passwordEncoder());
		// 중도매인
		auth.userDetailsService(bidUserDetailsService).passwordEncoder(passwordEncoder());
		// 농가
		auth.userDetailsService(farmUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("*"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
}

	public UsernamePasswordAuthenticationFilter getBeforeAuthenticationFilter() throws Exception {
		CustomBeforeAuthenticationFilter filter = new CustomBeforeAuthenticationFilter();
		filter.setAuthenticationManager(authenticationManager());
		filter.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request
												, HttpServletResponse response
												, AuthenticationException exception) throws IOException, ServletException {
				super.setDefaultFailureUrl("/login?error");
				super.onAuthenticationFailure(request, response, exception);
			}
		});
		return filter;
	}

}
