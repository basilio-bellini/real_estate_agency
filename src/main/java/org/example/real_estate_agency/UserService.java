package org.example.real_estate_agency;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        if (!user.getPassword().startsWith("$2a$")) { // Пример для BCrypt
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById((long) id).orElse(null);
    }
}
