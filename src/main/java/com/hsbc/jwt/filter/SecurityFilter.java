package com.hsbc.jwt.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hsbc.jwt.util.JwtUtil;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = request.getHeader("Authorization");

		if (token != null) {

			String username = jwtUtil.getUsername(token);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

				UserDetails userdetails = userDetailsService.loadUserByUsername(username);
				Boolean isValid = jwtUtil.isTokenValid(token, userdetails.getUsername());

				if (isValid) {

					// UsernamePasswordAuthenticationToken implements Authentication
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userdetails.getUsername(), userdetails.getPassword(), userdetails.getAuthorities());

					// link the authtoken to the current request
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					//
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}

		}
		filterChain.doFilter(request, response);

	}

}
