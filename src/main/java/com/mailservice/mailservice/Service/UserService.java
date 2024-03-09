package com.mailservice.mailservice.Service;

import com.mailservice.mailservice.domain.User;

public interface UserService {
    User saveUser(User user);
    Boolean verifyToken(String token);

    public String sendEmail(Long id);
}
