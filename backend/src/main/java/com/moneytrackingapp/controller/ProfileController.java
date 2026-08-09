package com.moneytrackingapp.controller;

import com.moneytrackingapp.model.Profile;
import com.moneytrackingapp.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public Profile getProfile() {
        return profileService.getProfile();
    }

    @PutMapping
    public ResponseEntity<Profile> updateProfile(@Valid @RequestBody Profile profile) {
        return ResponseEntity.ok(profileService.updateProfile(profile));
    }
}
