package com.ZakTrak.repository;


import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.ZakTrak.model.Asset;
import com.ZakTrak.model.AssetType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;



@DataMongoTest
public class AssetRepositoryTest {
    @Autowired
    private AssetRepository assetRepository;

    private String testUserId;
    private Asset cashAsset;
    private Asset goldAsset;


    @BeforeEach
    void setUp() {
        assetRepository.deleteAll();
        testUserId = "user123";
        LocalDateTime now = LocalDateTime.now();
        cashAsset = new Asset(
                testUserId,
                AssetType.CASH_AND_SAVINGS,
                new BigDecimal("1000.00"),
                "Emergency fund",
                true,
                now
        );

        goldAsset = new Asset(
                testUserId,
                AssetType.GOLD_AND_SILVER,
                new BigDecimal("5000.00"),
                "Investment gold",
                true,
                now
        );

        assetRepository.save(cashAsset);
        assetRepository.save(goldAsset);
    }

    @Test
    @DisplayName("Should save and find asset by user ID")
    void shouldSaveandFindAssetByUserId() {
        // Act
        List<Asset> userAssets = assetRepository.findByUserId(testUserId);

        // Assert
        assertEquals(2, userAssets.size(), "Should find 2 assets for the user");
        assertThat(userAssets).extracting("type").containsExactlyInAnyOrder(AssetType.CASH_AND_SAVINGS, AssetType.GOLD_AND_SILVER);
    }

    @Test
    @DisplayName("Should find only zakatable assets")
    void shouldFindZakatableAssets() {
       //Arrange
       Asset nonZakatableAsset = new Asset(
               testUserId,
               AssetType.INVESTMENTS,
               new BigDecimal("1000.00"),
               "Non-zakatable investment",
               false,
               LocalDateTime.now()
       );
         assetRepository.save(nonZakatableAsset);


            //Act
            List<Asset> zakatableAssets = assetRepository.findByUserIdAndZakatableTrue(testUserId);

            //Assert
        assertThat(zakatableAssets)
                .hasSize(2)
                .allMatch(Asset::isZakatable);
    }


    @Test
    @DisplayName("Should find assets by type")
    void shouldFindAssetsByType() {
        // When we search for cash assets
        List<Asset> cashAssets = assetRepository.findByUserIdAndType(testUserId, AssetType.CASH_AND_SAVINGS);

        // Then we should only find the cash asset
        assertThat(cashAssets)
                .hasSize(1)
                .extracting("type")
                .containsOnly(AssetType.CASH_AND_SAVINGS);
    }

    @Test
    @DisplayName("Should find no assets for non-existent user")
    void shouldFindNoAssetsForNonExistentUser() {
        // When we search for assets of a non-existent user
        List<Asset> assets = assetRepository.findByUserId("nonexistent");

        // Then we should get an empty list
        assertThat(assets).isEmpty();
    }

}
