package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        // Veritabanı boşsa, örnek kullanıcılar ekle
        if (userRepository.count() == 0) {
            userRepository.save(new User(null, "Ali", "Yılmaz", "ali.yilmaz@example.com", 28));
            userRepository.save(new User(null, "Ayşe", "Kaya", "ayse.kaya@example.com", 34));
            userRepository.save(new User(null, "Mehmet", "Demir", "mehmet.demir@example.com", 45));
            userRepository.save(new User(null, "Zeynep", "Öztürk", "zeynep.ozturk@example.com", 22));
            userRepository.save(new User(null, "Mustafa", "Şahin", "mustafa.sahin@example.com", 39));
        }
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    
    // Pagination için metot
    public Page<User> findPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable);
    }
    
    public long getTotal() {
        return userRepository.count();
    }
}
