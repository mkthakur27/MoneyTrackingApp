package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.Profile;

public interface ProfileRepository {
    Profile get();
    Profile save(Profile profile);
}
