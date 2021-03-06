package com.car_service.egea1r.service.authentication;

import com.car_service.egea1r.persistance.entity.Credential;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@AllArgsConstructor
public class AuthCredentialDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private long credentialId;
    private String emailAsIdentifier;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static AuthCredentialDetailsImpl build(Credential credential){
        return new AuthCredentialDetailsImpl(
                credential.getCredentialId(),
                credential.getEmail(),
                credential.getPassword(),
                Collections.singleton((new SimpleGrantedAuthority(String.valueOf(credential.getPermission())))));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return emailAsIdentifier;
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
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AuthCredentialDetailsImpl credential = (AuthCredentialDetailsImpl) o;
        return Objects.equals(credentialId, credential.credentialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentialId, emailAsIdentifier, password, authorities);
    }
}
