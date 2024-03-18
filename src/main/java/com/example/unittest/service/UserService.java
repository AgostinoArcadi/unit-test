package com.example.unittest.service;

import com.example.unittest.entity.User;
import com.example.unittest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User addUser(User user) {
        userRepository.save(user);
        return user;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id) {
        Optional<User> userOpt = userRepository.findById(id);

        if(userOpt.isPresent()) {
            return userOpt;
        }
        return Optional.empty();
    }

    public Optional<User> updateUser(User user, Integer id) {
        Optional<User> userOpt = userRepository.findById(id);

        if(userOpt.isPresent()) {
            userOpt.get().setNome(user.getNome());

            User userUpdated = userRepository.save(userOpt.get());
            return Optional.of(userUpdated);
        }
        return Optional.empty();
    }

    public Optional<User> deleteUser(Integer id) {
        Optional<User> userOpt = userRepository.findById(id);

        if(userOpt.isPresent()) {
            userRepository.deleteById(id);
            return userOpt;
        }
        return Optional.empty();
    }

}
