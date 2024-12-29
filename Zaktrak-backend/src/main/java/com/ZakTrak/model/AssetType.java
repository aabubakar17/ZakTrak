package com.ZakTrak.model;

public enum AssetType {
    CASH_AND_SAVINGS("Cash and Savings"),
    GOLD_AND_SILVER("Gold and Silver"),
    INVESTMENTS("Investments"),
    BUSINESS_ASSETS("Business Assets");

    private final String displayName;

    AssetType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Useful validation method similar to your email validation
    public static void validateType(String type) {
        try {
            AssetType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid asset type");
        }
    }
}