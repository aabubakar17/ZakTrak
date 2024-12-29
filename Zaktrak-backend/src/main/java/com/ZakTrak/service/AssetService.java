package com.ZakTrak.service;

import com.ZakTrak.model.Asset;
import com.ZakTrak.model.AssetType;
import com.ZakTrak.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ZakTrak.dto.NewAssetRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;
    private final UserService userService;


    public Asset createAsset(NewAssetRequest request) {
        String userId = userService.getCurrentUser().getId();

        Asset newAsset = new Asset(
                userId,
                request.type(),
                request.value(),
                request.description(),
                request.zakatable(),
                LocalDateTime.now()
        );

        return assetRepository.save(newAsset);
    }


    public List<Asset> getUserAssets() {
        String userId = userService.getCurrentUser().getId();
        return assetRepository.findByUserId(userId);
    }


    public List<Asset> updateAssetValue(AssetType type, BigDecimal newValue) {
        String userId = userService.getCurrentUser().getId();
        List<Asset> assets = assetRepository.findByUserIdAndType(userId, type);

        if (assets.isEmpty()) {
            throw new IllegalArgumentException("No assets found for the given type");
        }

        // Update all assets of the given type
        assets.forEach(asset -> {
            asset.updateValue(newValue);
            assetRepository.save(asset);
        });

        return assetRepository.findByUserIdAndType(userId, type);
    }


    public void deleteAsset(AssetType type) {
        String userId = userService.getCurrentUser().getId();
        List<Asset> assets = assetRepository.findByUserIdAndType(userId, type);

        if (assets.isEmpty()) {
            throw new IllegalArgumentException("No assets found for the given type");
        }

        assetRepository.deleteAll(assets);
    }


    public BigDecimal calculateTotalZakatableValue() {
        String userId = userService.getCurrentUser().getId();
        List<Asset> zakatableAssets = assetRepository.findByUserIdAndZakatableTrue(userId);

        return zakatableAssets.stream()
                .map(Asset::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public List<Asset> getAssetsByType(AssetType type) {
        String userId = userService.getCurrentUser().getId();
        List<Asset> assets = assetRepository.findByUserIdAndType(userId, type);

        if (assets.isEmpty()) {
            throw new IllegalArgumentException("No assets found for the given type");
        }

        return assets;
    }
}