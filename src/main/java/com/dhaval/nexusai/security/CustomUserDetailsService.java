package com.dhaval.nexusai.security;

import com.dhaval.nexusai.entity.User;
import com.dhaval.nexusai.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user =userRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User Not Found"));

        return org.springframework.security.core.userdetails.User.builder().username(user.getEmail()).password(user.getPassword()).build();
    }
}
