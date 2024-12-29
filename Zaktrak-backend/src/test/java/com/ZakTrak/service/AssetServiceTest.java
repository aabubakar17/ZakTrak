package com.ZakTrak.service;

import com.ZakTrak.model.Asset;
import com.ZakTrak.model.AssetType;
import com.ZakTrak.model.User;
import com.ZakTrak.dto.NewAssetRequest;
import com.ZakTrak.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AssetService assetService;

    private User authenticatedUser;
    private Asset testAsset;

    @BeforeEach
    void setUp() {

        authenticatedUser = mock(User.class);
        when(authenticatedUser.getId()).thenReturn("User123");
        testAsset = new Asset(
                "User123",
                AssetType.CASH_AND_SAVINGS,
                new BigDecimal("1000.00"),
                "Test Savings",
                true,
                LocalDateTime.now()
        );

        when(userService.getCurrentUser()).thenReturn(authenticatedUser);
    }

    @Test
    @DisplayName("Should create new asset for authenticated user")
    void shouldCreateNewAsset() {
        // Arrange
        NewAssetRequest request = new NewAssetRequest(
                AssetType.CASH_AND_SAVINGS,
                new BigDecimal("1000.00"),
                "Test Savings",
                true
        );
        when(assetRepository.save(any(Asset.class))).thenReturn(testAsset);

        // Act
        Asset createdAsset = assetService.createAsset(request);

        // Assert
        assertThat(createdAsset).isNotNull();
        assertThat(createdAsset.getUserId()).isEqualTo(authenticatedUser.getId());
        verify(assetRepository).save(any(Asset.class));
    }

    @Test
    @DisplayName("Should retrieve all assets for authenticated user")
    void shouldGetAllUserAssets() {
        when(assetRepository.findByUserId(authenticatedUser.getId()))
                .thenReturn(Arrays.asList(testAsset));

        List<Asset> assets = assetService.getUserAssets();

        assertThat(assets)
                .hasSize(1)
                .allMatch(asset -> asset.getUserId().equals(authenticatedUser.getId()));
        verify(assetRepository).findByUserId(authenticatedUser.getId());
    }

    @Test
    @DisplayName("Should retrieve assets by type for authenticated user")
    void shouldGetAssetsByType() {
        when(assetRepository.findByUserIdAndType(authenticatedUser.getId(), AssetType.CASH_AND_SAVINGS))
                .thenReturn(Arrays.asList(testAsset));

        List<Asset> assets = assetService.getAssetsByType(AssetType.CASH_AND_SAVINGS);

        assertThat(assets)
                .hasSize(1)
                .allMatch(asset -> asset.getType() == AssetType.CASH_AND_SAVINGS);
        verify(assetRepository).findByUserIdAndType(authenticatedUser.getId(), AssetType.CASH_AND_SAVINGS);
    }

    @Test
    @DisplayName("Should throw exception when getting assets of non-existent type")
    void shouldThrowExceptionWhenGettingNonExistentType() {
        when(assetRepository.findByUserIdAndType(authenticatedUser.getId(), AssetType.CASH_AND_SAVINGS))
                .thenReturn(List.of());

        assertThrows(IllegalArgumentException.class,
                () -> assetService.getAssetsByType(AssetType.CASH_AND_SAVINGS),
                "No assets found for the given type"
        );
    }

    @Test
    @DisplayName("Should update assets of given type")
    void shouldUpdateAssetsValue() {
        Asset asset1 = new Asset(
                authenticatedUser.getId(),
                AssetType.CASH_AND_SAVINGS,
                new BigDecimal("1000.00"),
                "Savings 1",
                true,
                LocalDateTime.now()
        );
        Asset asset2 = new Asset(
                authenticatedUser.getId(),
                AssetType.CASH_AND_SAVINGS,
                new BigDecimal("2000.00"),
                "Savings 2",
                true,
                LocalDateTime.now()
        );

        when(assetRepository.findByUserIdAndType(authenticatedUser.getId(), AssetType.CASH_AND_SAVINGS))
                .thenReturn(Arrays.asList(asset1, asset2));
        when(assetRepository.save(any(Asset.class))).thenReturn(asset1);

        List<Asset> updatedAssets = assetService.updateAssetValue(
                AssetType.CASH_AND_SAVINGS,
                new BigDecimal("3000.00")
        );

        assertThat(updatedAssets).hasSize(2);
        verify(assetRepository, times(2)).save(any(Asset.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent assets")
    void shouldThrowExceptionForNonExistentAsset() {
        when(assetRepository.findByUserIdAndType(authenticatedUser.getId(), AssetType.CASH_AND_SAVINGS))
                .thenReturn(List.of());

        assertThrows(IllegalArgumentException.class,
                () -> assetService.updateAssetValue(AssetType.CASH_AND_SAVINGS, new BigDecimal("1000.00")),
                "No assets found for the given type"
        );
        verify(assetRepository).findByUserIdAndType(authenticatedUser.getId(), AssetType.CASH_AND_SAVINGS);
    }

    @Test
    @DisplayName("Should calculate total zakatable value for authenticated user")
    void shouldCalculateZakatableValue() {
        Asset secondAsset = new Asset(
                authenticatedUser.getId(),
                AssetType.GOLD_AND_SILVER,
                new BigDecimal("2000.00"),
                "Gold investment",
                true,
                LocalDateTime.now()
        );

        when(assetRepository.findByUserIdAndZakatableTrue(authenticatedUser.getId()))
                .thenReturn(Arrays.asList(testAsset, secondAsset));

        BigDecimal totalZakatable = assetService.calculateTotalZakatableValue();

        assertThat(totalZakatable).isEqualByComparingTo(new BigDecimal("3000.00"));
        verify(assetRepository).findByUserIdAndZakatableTrue(authenticatedUser.getId());
    }

    @Test
    @DisplayName("Should delete assets of given type")
    void shouldDeleteAssets() {
        when(assetRepository.findByUserIdAndType(authenticatedUser.getId(), AssetType.CASH_AND_SAVINGS))
                .thenReturn(Arrays.asList(testAsset));

        assetService.deleteAsset(AssetType.CASH_AND_SAVINGS);

        verify(assetRepository).findByUserIdAndType(authenticatedUser.getId(), AssetType.CASH_AND_SAVINGS);
        verify(assetRepository).deleteAll(any());
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent assets")
    void shouldThrowExceptionWhenDeletingNonExistentAssets() {
        when(assetRepository.findByUserIdAndType(authenticatedUser.getId(), AssetType.CASH_AND_SAVINGS))
                .thenReturn(List.of());

        assertThrows(IllegalArgumentException.class,
                () -> assetService.deleteAsset(AssetType.CASH_AND_SAVINGS),
                "No assets found for the given type"
        );
    }
}