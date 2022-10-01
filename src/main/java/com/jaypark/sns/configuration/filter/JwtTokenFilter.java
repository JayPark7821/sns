package com.jaypark.sns.configuration.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jaypark.sns.model.User;
import com.jaypark.sns.service.UserService;
import com.jaypark.sns.utils.JwtTokenUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

	private final String key;
	private final UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// get header
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (header == null || header.startsWith("Bearer ")) {
			log.error("Error occurs while getting header. Header is null or invalid");
			filterChain.doFilter(request, response);
			return;
		}

		try {
			final String token = header.split(" ")[1].trim();

			// TODO : check token is valid
			if (JwtTokenUtils.isExpired(token, key)) {
				log.error("Key is Expired");
				filterChain.doFilter(request, response);
				return;

			}


			String userName = JwtTokenUtils.getUserName(token, key);
			User user = userService.loadUserByUserName(userName);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				user, null, user.getAuthorities());

			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		} catch (RuntimeException e) {
			log.error("Error occurs while validating. {}", e.toString());
			filterChain.doFilter(request, response);
			return;
		}
		filterChain.doFilter(request, response);
	}
}