package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final ConcurrentHashMap<Long, User> byId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> idsByEmail = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(byId.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Long id = idsByEmail.get(normalize(email));
        return id == null ? Optional.empty() : findById(id);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        String email = normalize(user.getEmail());
        user.setEmail(email);
        byId.put(user.getId(), user);
        idsByEmail.put(email, user.getId());
        return user;
    }

    @Override
    public boolean existsByEmail(String email) {
        return idsByEmail.containsKey(normalize(email));
    }

    private String normalize(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
