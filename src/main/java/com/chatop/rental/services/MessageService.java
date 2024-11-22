package com.chatop.rental.services;

import com.chatop.rental.configuration.CustomUserDetails;
import com.chatop.rental.dto.MessageRequestDTO;
import com.chatop.rental.entities.Message;
import com.chatop.rental.entities.Rental;
import com.chatop.rental.entities.User;
import com.chatop.rental.repositories.MessageRepository;
import com.chatop.rental.repositories.RentalRepository;
import com.chatop.rental.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MessageService {
  private final MessageRepository messageRepository;
  private final RentalRepository rentalRepository;
  private final UserRepository userRepository;

  public MessageService(MessageRepository messageRepository,
      RentalRepository rentalRepository,
      UserRepository userRepository) {
    this.messageRepository = messageRepository;
    this.rentalRepository = rentalRepository;
    this.userRepository = userRepository;
  }

  public void createMessage(MessageRequestDTO messageDTO, CustomUserDetails userDetails) {
    User user = getUserFromUserDetails(userDetails);

    Rental rental = rentalRepository.findById(messageDTO.getRentalId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found"));

    Message message = new Message(rental, user, messageDTO.getMessage());
    messageRepository.save(message);
  }

  private User getUserFromUserDetails(CustomUserDetails userDetails) {
    String userEmail = userDetails.getUsername();
    User user = userRepository.findByEmail(userEmail);
    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }
    return user;
  }
}
