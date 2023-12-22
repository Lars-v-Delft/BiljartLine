package com.biljartline.billiardsapi.security;

import java.util.Set;

public record UserDetailsDTO(
        String username,
        Set<String> roles,
        boolean isAccountNonExpired,
        boolean isAccountNonLocked,
        boolean isCredentialsNonExpired,
        boolean isEnabled) {
}
