package com.moneytrackingapp.service;

import com.moneytrackingapp.model.Profile;
import com.moneytrackingapp.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository repository;

    public ProfileServiceImpl(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Profile getProfile() {
        return repository.findById(Profile.SINGLETON_ID).orElseGet(() -> repository.save(new Profile()));
    }

    @Override
    @Transactional
    public Profile updateProfile(Profile profile) {
        profile.setId(Profile.SINGLETON_ID);
        return repository.save(profile);
    }
}
