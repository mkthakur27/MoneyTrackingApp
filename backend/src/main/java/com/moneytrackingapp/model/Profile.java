package com.moneytrackingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @JsonIgnore
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Currency code is required")
    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode = "USD";

    @NotBlank(message = "Currency symbol is required")
    @Column(name = "currency_symbol", nullable = false, length = 10)
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
