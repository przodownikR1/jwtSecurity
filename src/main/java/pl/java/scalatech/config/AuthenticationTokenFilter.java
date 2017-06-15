package pl.java.scalatech.config;

import static org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext;
import static pl.java.scalatech.SecurityConstants.TOKEN_HEADER;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

class AuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {
    
        private static final String CORS_ORIGINS = "cors-origins";
    
        private TokenUtils tokenUtils;
    
        @Autowired
        private Environment enviroment;
    
        private UserDetailsService userDetailsService;
    
        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
            tokenUtils =  getRequiredWebApplicationContext(this.getServletContext()).getBean(TokenUtils.class);
            userDetailsService = getRequiredWebApplicationContext(this.getServletContext()).getBean(UserDetailsService.class);
            corsEnable(res);
            HttpServletRequest httpServletRequest = (HttpServletRequest) req;
            String authToken = httpServletRequest.getHeader(TOKEN_HEADER);
            Optional<String> optUsername = this.tokenUtils.getUsernameFromToken(authToken);
            if (optUsername.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(optUsername.get());
                if (this.tokenUtils.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                            userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            chain.doFilter(req, res);
        }
    
        private void corsEnable(ServletResponse res) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) res;
            httpServletResponse.setHeader("Access-Control-Allow-Origin", enviroment.getProperty(CORS_ORIGINS));
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE,PATCH");
            httpServletResponse.setHeader("Access-Control-Allow-Max-Age", "3600");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, " + TOKEN_HEADER);
        }
    }

