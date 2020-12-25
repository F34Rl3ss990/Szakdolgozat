package com.EGEA1R.CarService.service.authentication;

import com.EGEA1R.CarService.entity.Credential;
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
    private Long credentialId;
    private String emailAsIdentifier;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static AuthCredentialDetailsImpl build(Credential credential){
        List<GrantedAuthority> authority = Collections.singletonList(new SimpleGrantedAuthority(String.valueOf(credential.getPermission())));
       // List<GrantedAuthority> authority = new ArrayList<>();
      //  authority.add(new SimpleGrantedAuthority(credential.getPermission()));
        return new AuthCredentialDetailsImpl(
                credential.getCredential_id(),
                credential.getEmail(),
                credential.getPassword(),
                authority);
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

}
