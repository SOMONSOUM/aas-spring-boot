package kh.moc.aas.user.service;

import java.time.LocalDateTime;
import jakarta.transaction.Transactional;
import kh.moc.aas.auth.dto.RegisterRequest;
import kh.moc.aas.user.exception.UserAlreadyExistsException;
import kh.moc.aas.user.model.User;
import kh.moc.aas.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository=userRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User register(RegisterRequest req) {
        if(doesEmailExist(req.email())){
            throw new UserAlreadyExistsException("Email already exists");
        }

        User newUser = new User();
        newUser.setName(req.name());
        newUser.setEmail(req.email());
        newUser.setPassword(req.password());
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(newUser);
    }

    public Boolean doesEmailExist(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
