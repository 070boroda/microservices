package com.zelenko.authservice.user;

import com.zelenko.Repository.ApplicationUserRepository;
import com.zelenko.entity.ApplicationUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserDetailsServiceImpl implements UserDetailsService  {

    @Autowired
    private  ApplicationUserRepository applicationUserRepository;


    @Override
    public UserDetails loadUserByUsername(String username){

        log.info("Searching in DB the user by user name '{}'", username);
        ApplicationUser applicationUser = applicationUserRepository.findByUserName(username);
        log.info("Found ApplicatinUser user '{}'", applicationUser);
        if(applicationUser == null)
            throw new UsernameNotFoundException(String.format("Application user '%s' not found", username));
        return new CustomUserDetails(applicationUser);
    }


    private static final class CustomUserDetails extends ApplicationUser implements UserDetails{
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.commaSeparatedStringToAuthorityList( "ROLE_" + this.getRole());
        }

        CustomUserDetails(@NotNull ApplicationUser applicationUser) {
            super(applicationUser);
        }

        @Override
        public String getUsername() {
            return null;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
