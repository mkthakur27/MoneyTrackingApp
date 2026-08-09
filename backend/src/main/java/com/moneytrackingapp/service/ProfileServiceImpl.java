package com.moneytrackingapp.service;

import com.moneytrackingapp.model.Profile;
import com.moneytrackingapp.repository.ProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository repository;

    public ProfileServiceImpl(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public Profile getProfile() {
        return repository.get();
    }

    @Override
    public Profile updateProfile(Profile profile) {
        return repository.save(profile);
    }
}
