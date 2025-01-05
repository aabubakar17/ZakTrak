package com.ZakTrak.controller;

import com.ZakTrak.dto.NewAssetRequest;
import com.ZakTrak.model.Asset;
import com.ZakTrak.model.AssetType;
import com.ZakTrak.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Asset createAsset(@RequestBody NewAssetRequest request) {
        return assetService.createAsset(request);
    }

    @GetMapping
    public List<Asset> getAllAssets() {
        return assetService.getUserAssets();
    }

    @GetMapping("/type/{type}")
    public List<Asset> getAssetsByType(@PathVariable AssetType type) {
        return assetService.getAssetsByType(type);
    }

    @PutMapping("/type/{type}/value")
    public List<Asset> updateAssetValue(
            @PathVariable AssetType type,
            @RequestBody BigDecimal newValue) {
        return assetService.updateAssetValue(type, newValue);
    }

    @DeleteMapping("/type/{type}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAsset(@PathVariable AssetType type) {
        assetService.deleteAsset(type);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAssetById(@PathVariable String id) {
        assetService.deleteAssetById(id);
    }


    // Optional: Add endpoint to calculate total zakatable value
    @GetMapping("/zakatable/total")
    public BigDecimal getTotalZakatableValue() {
        return assetService.calculateTotalZakatableValue();
    }

    // Exception handler for illegal arguments
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgument(IllegalArgumentException e) {
        return e.getMessage();
    }
}