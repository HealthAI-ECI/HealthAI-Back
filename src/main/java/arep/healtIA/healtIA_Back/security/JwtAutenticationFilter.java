package arep.healtIA.healtIA_Back.security;

import arep.healtIA.healtIA_Back.model.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Authentication filter for the application and generate the secure token.
 * @author Cristan Duarte.
 * @author Johann Amaya.
 * @author Sebastian Zamora.
 * @version 1.0
 */
public class JwtAutenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtils JwtUtils;

    /**
     * Constructor of the class.
     * @param jwtUtils the jwt utils.
     */
    public JwtAutenticationFilter(arep.healtIA.healtIA_Back.security.JwtUtils jwtUtils) {
        JwtUtils = jwtUtils;
    }

    /**
     * This method is used to attempt the authentication of the user.
     * @param request the http request.
     * @param response the http response.
     * @return the authentication.
     * @throws AuthenticationException if there is an error.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        UserEntity user = null;
        String username="";
        String password="";
        try {
            user = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
            username = user.getEmail();
            password = user.getHashPassword();
        } catch (Exception e) {
            e.printStackTrace();
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    /**
     * This method is used to generate the token of the user.
     * @param request the http request.
     * @param response the http response.
     * @param chain the filter chain.
     * @param authResult the authentication result.
     * @throws IOException if there is an error.
     * @throws ServletException if there is an error.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String token = JwtUtils.generateToken(user.getUsername());
        response.addHeader("Authorization", "Bearer " + token);
        Map<String,Object> httpRespones = new HashMap<>();
        httpRespones.put("token",token);
        httpRespones.put("message","Authentication successful");
        httpRespones.put("username",user.getUsername());
        response.getWriter().write(new ObjectMapper().writeValueAsString(httpRespones));
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().flush();
        super.successfulAuthentication(request, response, chain, authResult);
    }
}

