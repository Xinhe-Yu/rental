package com.chatop.rental.mappers;

import org.springframework.stereotype.Component;

import com.chatop.rental.dto.UserDTO;
import com.chatop.rental.entities.User;

@Component
public class UserMapper {
  public UserDTO convertToDTO(User user) {
    UserDTO dto = new UserDTO();
    dto.setId(user.getId());
    dto.setName(user.getName());
    dto.setEmail(user.getEmail());
    dto.setCreatedAt(user.getCreatedAt());
    dto.setUpdatedAt(user.getUpdatedAt());
    return dto;
  }
}
