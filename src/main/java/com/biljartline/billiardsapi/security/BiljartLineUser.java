package com.biljartline.billiardsapi.security;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@Builder
public class BiljartLineUser implements UserDetails {
    private String username;
    private List<SimpleGrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    @Override
    public String getPassword() {
        return null;
    }

}
