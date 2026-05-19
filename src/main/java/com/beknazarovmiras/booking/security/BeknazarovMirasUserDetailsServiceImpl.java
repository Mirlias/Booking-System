package com.beknazarovmiras.booking.security;

import com.beknazarovmiras.booking.entity.BeknazarovMirasUser;
import com.beknazarovmiras.booking.repository.BeknazarovMirasUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeknazarovMirasUserDetailsServiceImpl implements UserDetailsService {

    private final BeknazarovMirasUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        BeknazarovMirasUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        return new BeknazarovMirasUserDetailsImpl(user);
    }
}
