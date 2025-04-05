package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static final Map<Long, User> users = new HashMap<>();
    private static Long idCounter = 1L;

    static {
        // Bazı örnek kullanıcılar ekleyelim
        users.put(1L, new User(1L, "Ali", "Yılmaz", "ali.yilmaz@example.com", 28));
        users.put(2L, new User(2L, "Ayşe", "Kaya", "ayse.kaya@example.com", 34));
        users.put(3L, new User(3L, "Mehmet", "Demir", "mehmet.demir@example.com", 45));
        users.put(4L, new User(4L, "Zeynep", "Öztürk", "zeynep.ozturk@example.com", 22));
        users.put(5L, new User(5L, "Mustafa", "Şahin", "mustafa.sahin@example.com", 39));
        idCounter = 6L;
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User findById(Long id) {
        return users.get(id);
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter++);
        }
        users.put(user.getId(), user);
        return user;
    }

    public void deleteById(Long id) {
        users.remove(id);
    }
}
