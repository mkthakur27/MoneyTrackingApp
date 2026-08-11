package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.Profile;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryProfileRepository implements ProfileRepository {
    private volatile Profile profile = new Profile();

    @Override
    public Profile get() {
        return profile;
    }

    @Override
    public Profile save(Profile profile) {
        this.profile = profile;
        return this.profile;
    }
}
