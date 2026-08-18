package com.moneytrackingapp.security;

public class AppUserPrincipal {
    private final Long id;
    private final String email;

    public AppUserPrincipal(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
