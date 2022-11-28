package fr.polytech.ig5;

import fr.polytech.ig5.jwt.JWTUtil;
import fr.polytech.ig5.jwt.User;
import fr.polytech.ig5.payload.LoginPayload;
import fr.polytech.ig5.payload.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping(path = "/token",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getToken(@RequestBody LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken loginCredentials =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(), loginRequest.getPassword());
        Authentication authentication =
                authenticationManager.authenticate(loginCredentials);
        User user = (User) authentication.getPrincipal();
        String jwtToken = jwtUtil.createJWT(user);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken)
                .build();

    }
}