package com.moneytrackingapp.controller;

import com.moneytrackingapp.model.Profile;
import com.moneytrackingapp.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
@Tag(name = "Profile", description = "Manage profile currency denomination settings")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    @Operation(summary = "Get profile settings")
    @ApiResponse(responseCode = "200", description = "Profile returned successfully")
    public Profile getProfile() {
        return profileService.getProfile();
    }

    @PutMapping
    @Operation(summary = "Update profile settings")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid profile payload")
    })
    public ResponseEntity<Profile> updateProfile(@Valid @RequestBody Profile profile) {
        return ResponseEntity.ok(profileService.updateProfile(profile));
    }
}
