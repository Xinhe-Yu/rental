package com.chatop.rental.services;

import com.chatop.rental.configuration.CustomUserDetails;
import com.chatop.rental.entities.User;
import com.chatop.rental.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  public CustomUserDetailsService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }

    return new CustomUserDetails(user);
  }

  public User getCurrentUser(String email) {
    User user = userRepository.findByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return user;
  }

  public void save(String name, String email, String password) {
    User user = new User();
    user.setName(name);
    user.setEmail(email);
    user.setEncodedPassword(passwordEncoder.encode(password));
    user.setRole("");
    userRepository.save(user);
  }
}
