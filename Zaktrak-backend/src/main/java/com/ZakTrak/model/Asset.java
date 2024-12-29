package com.ZakTrak.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "assets")
@Getter
public class Asset {
    @Id
    private String id;

    @Indexed
    private String userId;

    private AssetType type;
    private BigDecimal value;
    private String description;
    private boolean zakatable;
    private LocalDateTime acquisitionDate;
    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;

    // Following your pattern of using a constructor with validation
    public Asset(String userId, AssetType type, BigDecimal value, String description,
                 boolean zakatable, LocalDateTime acquisitionDate) {
        validateAsset(userId, type, value);
        this.userId = userId;
        this.type = type;
        this.value = value;
        this.description = description;
        this.zakatable = zakatable;
        this.acquisitionDate = acquisitionDate != null ? acquisitionDate : LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    private void validateAsset(String userId, AssetType type, BigDecimal value) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Asset type cannot be null");
        }
        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Asset value cannot be null or negative");
        }
    }

    // Method to update value, following immutable pattern except for specific updates
    public void updateValue(BigDecimal newValue) {
        if (newValue == null || newValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("New value cannot be null or negative");
        }
        this.value = newValue;
        this.lastUpdated = LocalDateTime.now();
    }

    // Method to update description
    public void updateDescription(String newDescription) {
        this.description = newDescription;
        this.lastUpdated = LocalDateTime.now();
    }

    // Method to update zakatable status
    public void updateZakatableStatus(boolean zakatable) {
        this.zakatable = zakatable;
        this.lastUpdated = LocalDateTime.now();
    }
}