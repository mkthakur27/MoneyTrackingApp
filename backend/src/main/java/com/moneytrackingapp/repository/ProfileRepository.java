package com.moneytrackingapp.repository;

import com.moneytrackingapp.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
