package com.moneytrackingapp.service;

import com.moneytrackingapp.model.Profile;
import com.moneytrackingapp.repository.ProfileRepository;
import com.moneytrackingapp.security.CurrentUser;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository repository;
    private final CurrentUser currentUser;

    public ProfileServiceImpl(ProfileRepository repository, CurrentUser currentUser) {
        this.repository = repository;
        this.currentUser = currentUser;
    }

    @Override
    public Profile getProfile() {
        Long userId = currentUser.requireUserId();
        return repository.findByUserId(userId).orElseGet(() -> {
            Profile profile = new Profile();
            profile.setUserId(userId);
            return repository.save(profile);
        });
    }

    @Override
    public Profile updateProfile(Profile profile) {
        Long userId = currentUser.requireUserId();
        Profile existing = repository.findByUserId(userId).orElseGet(Profile::new);
        existing.setUserId(userId);
        existing.setCurrencyCode(profile.getCurrencyCode());
        existing.setCurrencySymbol(profile.getCurrencySymbol());
        return repository.save(existing);
    }
}
