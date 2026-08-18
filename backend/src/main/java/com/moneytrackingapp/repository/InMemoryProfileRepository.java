package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProfileRepository implements ProfileRepository {
    private final ConcurrentHashMap<Long, Profile> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Profile> findByUserId(Long userId) {
        return Optional.ofNullable(storage.get(userId));
    }

    @Override
    public Profile save(Profile profile) {
        storage.put(profile.getUserId(), profile);
        return profile;
    }
}
