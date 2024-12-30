package com.ZakTrak.service;

import com.ZakTrak.model.Nisab;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ZakatService {

    private final AssetService assetService;
    private final NisabService nisabService;
    private static final BigDecimal ZAKAT_RATE = new BigDecimal("0.025"); // 2.5%


    public BigDecimal calculateZakat() {
        BigDecimal totalAssets = assetService.calculateTotalZakatableValue();
        Nisab nisab = nisabService.getLatestNisabThreshold();

        if (isAboveNisab(totalAssets, nisab)) {
            return totalAssets.multiply(ZAKAT_RATE).setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    private boolean isAboveNisab(BigDecimal totalAssets, Nisab nisab) {
        return totalAssets.compareTo(nisab.getSilverNisab()) > 0 ||
                totalAssets.compareTo(nisab.getGoldNisab()) > 0;
    }
}