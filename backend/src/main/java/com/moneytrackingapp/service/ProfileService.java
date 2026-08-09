package com.moneytrackingapp.service;

import com.moneytrackingapp.model.Profile;

public interface ProfileService {
    Profile getProfile();
    Profile updateProfile(Profile profile);
}
