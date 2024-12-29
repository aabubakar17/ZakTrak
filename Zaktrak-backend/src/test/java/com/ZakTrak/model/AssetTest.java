
package com.ZakTrak.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AssetTest {

    @Test
    @DisplayName("Should successfully create an asset with valid parameters")
    void shouldCreateValidAsset() {
        // We'll create a test asset with all required fields to verify basic construction
        LocalDateTime acquisitionDate = LocalDateTime.now();
        Asset asset = new Asset(
                "testUserId",
                AssetType.CASH_AND_SAVINGS,
                new BigDecimal("1000.00"),
                "Emergency savings",
                true,
                acquisitionDate
        );

        // Verify all fields are set correctly
        assertNotNull(asset);
        assertEquals("testUserId", asset.getUserId());
        assertEquals(AssetType.CASH_AND_SAVINGS, asset.getType());
        assertEquals(new BigDecimal("1000.00"), asset.getValue());
        assertEquals("Emergency savings", asset.getDescription());
        assertTrue(asset.isZakatable());
        assertEquals(acquisitionDate, asset.getAcquisitionDate());
        assertNotNull(asset.getCreatedAt());
        assertNotNull(asset.getLastUpdated());
    }

    @Test
    @DisplayName("Should create asset with default acquisition date when none provided")
    void shouldCreateAssetWithDefaultAcquisitionDate() {
        // Create an asset without specifying an acquisition date
        LocalDateTime now = LocalDateTime.now();
        Asset asset = new Asset(
                "testUserId",
                AssetType.CASH_AND_SAVINGS,
                new BigDecimal("1000.00"),
                "Test asset",
                true,
                null
        );

        // Verify that acquisition date defaults to creation time
        assertNotNull(asset.getAcquisitionDate());
        assertTrue(asset.getAcquisitionDate().isAfter(now));

    }

    @Test
    @DisplayName("Should throw exception when creating asset with null userId")
    void shouldThrowExceptionForNullUserId() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Asset(
                        null,
                        AssetType.CASH_AND_SAVINGS,
                        new BigDecimal("1000.00"),
                        "Test asset",
                        true,
                        LocalDateTime.now()
                )
        );

        assertEquals("User ID cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating asset with null type")
    void shouldThrowExceptionForNullType() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Asset(
                        "testUserId",
                        null,
                        new BigDecimal("1000.00"),
                        "Test asset",
                        true,
                        LocalDateTime.now()
                )
        );

        assertEquals("Asset type cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when creating asset with negative value")
    void shouldThrowExceptionForNegativeValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Asset(
                        "testUserId",
                        AssetType.CASH_AND_SAVINGS,
                        new BigDecimal("-100.00"),
                        "Test asset",
                        true,
                        LocalDateTime.now()
                )
        );

        assertEquals("Asset value cannot be null or negative", exception.getMessage());
    }

    @Test
    @DisplayName("Should successfully update asset value and update timestamp")
    void shouldUpdateValueAndTimestamp() {
        // Create an initial asset
        Asset asset = new Asset(
                "testUserId",
                AssetType.CASH_AND_SAVINGS,
                new BigDecimal("1000.00"),
                "Test asset",
                true,
                LocalDateTime.now()
        );

        LocalDateTime originalUpdateTime = asset.getLastUpdated();

        // Brief pause to ensure timestamp difference
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Update the value
        asset.updateValue(new BigDecimal("2000.00"));

        // Verify the update
        assertEquals(new BigDecimal("2000.00"), asset.getValue());
        assertTrue(asset.getLastUpdated().isAfter(originalUpdateTime));
    }

    @Test
    @DisplayName("Should successfully update description and update timestamp")
    void shouldUpdateDescriptionAndTimestamp() {
        Asset asset = new Asset(
                "testUserId",
                AssetType.CASH_AND_SAVINGS,
                new BigDecimal("1000.00"),
                "Original description",
                true,
                LocalDateTime.now()
        );

        LocalDateTime originalUpdateTime = asset.getLastUpdated();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        asset.updateDescription("Updated description");

        assertEquals("Updated description", asset.getDescription());
        assertTrue(asset.getLastUpdated().isAfter(originalUpdateTime));
    }

    @Test
    @DisplayName("Should successfully update zakatable status and update timestamp")
    void shouldUpdateZakatableStatusAndTimestamp() {
        Asset asset = new Asset(
                "testUserId",
                AssetType.INVESTMENTS,
                new BigDecimal("1000.00"),
                "Test asset",
                true,
                LocalDateTime.now()
        );

        LocalDateTime originalUpdateTime = asset.getLastUpdated();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        asset.updateZakatableStatus(false);

        assertFalse(asset.isZakatable());
        assertTrue(asset.getLastUpdated().isAfter(originalUpdateTime));
    }
}
