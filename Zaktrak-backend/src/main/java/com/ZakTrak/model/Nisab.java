package com.ZakTrak.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collection = "nisab")
@Getter
public class Nisab {
    @Id
    private String id;
    private BigDecimal goldPricePerGram;
    private BigDecimal silverPricePerGram;
    private BigDecimal goldNisab;
    private BigDecimal silverNisab;
    private LocalDate priceDate;

    private static final BigDecimal GOLD_NISAB_WEIGHT = new BigDecimal("85");  // 85 grams
    private static final BigDecimal SILVER_NISAB_WEIGHT = new BigDecimal("600"); // 600 grams

    public Nisab(BigDecimal goldPricePerGram, BigDecimal silverPricePerGram, LocalDate priceDate) {
        validatePrices(goldPricePerGram, silverPricePerGram);
        this.goldPricePerGram = goldPricePerGram;
        this.silverPricePerGram = silverPricePerGram;
        this.priceDate = priceDate;
        calculateNisabValues();
    }

    private void validatePrices(BigDecimal goldPrice, BigDecimal silverPrice) {
        if (goldPrice.compareTo(BigDecimal.ZERO) < 0 || silverPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Prices cannot be negative");
        }
    }

    private void calculateNisabValues() {
        this.goldNisab = goldPricePerGram.multiply(GOLD_NISAB_WEIGHT);
        this.silverNisab = silverPricePerGram.multiply(SILVER_NISAB_WEIGHT);
    }
}