package com.zelenko.authservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.zelenko.entity.ApplicationUser;
import com.zelenko.property.JwtConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class JwtUserNameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfiguration jwtConfiguration;


    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        log.info("Attempting Authentification . . .");
        ApplicationUser applicationUser = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);

        if (applicationUser == null)
            throw new UsernameNotFoundException("Unable to retrieve name or password");

        log.info("Creating user with name {}", applicationUser.getUserName());

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(applicationUser.getUserName(),
                applicationUser.getPassword(), emptyList());
        usernamePasswordAuthenticationToken.setDetails(applicationUser);

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    @SneakyThrows
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        log.info("Authentication was successful for the user '{}' generet token ", auth.getName());

        SignedJWT signedJWT = createSignedJWT(auth);
        String encryptToken = encryptToken(signedJWT);
        response.addHeader("Access-Control-Expose-Headers", "XSRF-TOKEN"
                + jwtConfiguration.getHeader().getName());
        response.addHeader(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix() + encryptToken);
    }

    @SneakyThrows
    private SignedJWT createSignedJWT(Authentication auth){
        log.info("Starting to create token");
        ApplicationUser applicationUser = (ApplicationUser) auth.getPrincipal();

        JWTClaimsSet jwtClaimsSet =     createJwtClaimSet(auth, applicationUser);

        KeyPair rsaKeys = genereateKeyPair();

       JWK jwk =  new RSAKey.Builder((RSAPublicKey)rsaKeys.getPublic()).keyID(UUID.randomUUID().toString()).build();
       SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
               .jwk(jwk)
               .type(JOSEObjectType.JWT)
               .build(), jwtClaimsSet);
       log.info("Signing the token RSA key");

        RSASSASigner signer = new RSASSASigner(rsaKeys.getPrivate());

        signedJWT .sign(signer);

        return signedJWT;
    }

    JWTClaimsSet createJwtClaimSet (Authentication auth, ApplicationUser applicationUser){
        log.info("Create  JWTClaimSet");
        return new JWTClaimsSet.Builder()
                .subject(applicationUser.getUserName())
                .claim("authorities", auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(toList()))
                .issuer("zelenko")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + (jwtConfiguration.getDuration() * 1000)))
                        .build();
    }

    @SneakyThrows
    private KeyPair genereateKeyPair(){
        log.info ("Generate Key");
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.genKeyPair();

    }

    private  String encryptToken (SignedJWT signedJWT) throws JOSEException {
        log.info("Start encrypt token");

        DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getSecret().getBytes());

        JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
        .contentType("JWT")
        .build(), new Payload(signedJWT));

        jweObject.encrypt(directEncrypter);
        log.info("Token was encrypt");

        return jweObject.serialize();
    }
}
