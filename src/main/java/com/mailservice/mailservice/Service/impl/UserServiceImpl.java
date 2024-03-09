package com.mailservice.mailservice.Service.impl;

import com.mailservice.mailservice.Service.EmailService;
import com.mailservice.mailservice.Service.UserService;
import com.mailservice.mailservice.domain.Confirmation;
import com.mailservice.mailservice.domain.User;
import com.mailservice.mailservice.repository.ConfirmationRepository;
import com.mailservice.mailservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, ConfirmationRepository confirmationRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.confirmationRepository = confirmationRepository;
//        this.emailService = emailService;
        this.emailService = emailService;
    }

    @Override
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) { throw new RuntimeException("Email already exists"); }
        user.setEnabled(false);
        userRepository.save(user);

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);

        /* TODO Send email to user with token */
//        emailService.sendSimpleMailMessage(user.getName(), user.getEmail(), confirmation.getToken());
        emailService.sendMimeMessageWithAttachments(user.getName(), user.getEmail(), confirmation.getToken());
        //emailService.sendMimeMessageWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
        //emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());
//        emailService.sendHtmlEmailWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());

        return user;
    }
    @Override
    public String sendEmail(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        String name = user.getName();
        String to = user.getEmail();
        Confirmation confirmation = confirmationRepository.findByUser(user);
        String token = confirmation.getToken();
//        emailService.sendSimpleMailMessage(name, to, token);
//        emailService.sendMimeMessageWithAttachments(user.getName(), user.getEmail(), confirmation.getToken());
//        emailService.sendMimeMessageWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
        emailService.sendHtmlEmail(user.getName(), user.getEmail(), confirmation.getToken());
//        emailService.sendHtmlEmailWithEmbeddedFiles(user.getName(), user.getEmail(), confirmation.getToken());
        return "Email sent successfully";
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        //confirmationRepository.delete(confirmation);
        return Boolean.TRUE;
    }
}
