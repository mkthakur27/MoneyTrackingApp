package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.Profile;
import java.util.Optional;

public interface ProfileRepository {
    Optional<Profile> findByUserId(Long userId);
    Profile save(Profile profile);
}
