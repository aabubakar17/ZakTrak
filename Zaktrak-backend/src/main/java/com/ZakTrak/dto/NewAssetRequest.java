package com.ZakTrak.dto;

import com.ZakTrak.model.AssetType;
import java.math.BigDecimal;

public record NewAssetRequest(
        AssetType type,
        BigDecimal value,
        String description,
        boolean zakatable
) {



    public NewAssetRequest {
        if (type == null) {
            throw new IllegalArgumentException("Asset type cannot be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Value cannot be negative");
        }

    }
}