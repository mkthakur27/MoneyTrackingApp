package com.moneytrackingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;

public class Profile {
    @JsonIgnore
    private Long userId;

    @NotBlank(message = "Currency code is required")
    private String currencyCode = "USD";

    @NotBlank(message = "Currency symbol is required")
    private String currencySymbol = "$";

    public Profile() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
