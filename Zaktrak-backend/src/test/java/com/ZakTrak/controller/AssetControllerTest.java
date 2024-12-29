package com.ZakTrak.controller;

import com.ZakTrak.dto.NewAssetRequest;
import com.ZakTrak.model.Asset;
import com.ZakTrak.model.AssetType;
import com.ZakTrak.service.AssetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AssetService assetService;

    private final String BASE_URL = "/api/assets";

    @Test
    @DisplayName("Should create new asset")
    @WithMockUser
    void shouldCreateNewAsset() throws Exception {
        // Arrange
        NewAssetRequest request = new NewAssetRequest(
                AssetType.CASH_AND_SAVINGS,
                new BigDecimal("1000.00"),
                "Test savings",
                true
        );

        Asset createdAsset = new Asset(
                "userId123",
                AssetType.CASH_AND_SAVINGS,
                new BigDecimal("1000.00"),
                "Test savings",
                true,
                LocalDateTime.now()
        );

        when(assetService.createAsset(any(NewAssetRequest.class))).thenReturn(createdAsset);

        // Act & Assert
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value(AssetType.CASH_AND_SAVINGS.toString()))
                .andExpect(jsonPath("$.value").value("1000.0"))
                .andExpect(jsonPath("$.description").value("Test savings"));
    }

    @Test
    @DisplayName("Should get all user assets")
    @WithMockUser
    void shouldGetAllUserAssets() throws Exception {
        // Arrange
        List<Asset> assets = Arrays.asList(
                new Asset("userId123", AssetType.CASH_AND_SAVINGS, new BigDecimal("1000.0"),
                        "Savings", true, LocalDateTime.now()),
                new Asset("userId123", AssetType.GOLD_AND_SILVER, new BigDecimal("2000.00"),
                        "Gold", true, LocalDateTime.now())
        );

        when(assetService.getUserAssets()).thenReturn(assets);

        // Act & Assert
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value(AssetType.CASH_AND_SAVINGS.toString()))
                .andExpect(jsonPath("$[1].type").value(AssetType.GOLD_AND_SILVER.toString()));
    }

    @Test
    @DisplayName("Should get assets by type")
    @WithMockUser
    void shouldGetAssetsByType() throws Exception {
        // Arrange
        List<Asset> assets = Arrays.asList(
                new Asset("userId123", AssetType.CASH_AND_SAVINGS, new BigDecimal("1000.00"),
                        "Savings 1", true, LocalDateTime.now()),
                new Asset("userId123", AssetType.CASH_AND_SAVINGS, new BigDecimal("2000.00"),
                        "Savings 2", true, LocalDateTime.now())
        );

        when(assetService.getAssetsByType(AssetType.CASH_AND_SAVINGS)).thenReturn(assets);

        // Act & Assert
        mockMvc.perform(get(BASE_URL + "/type/{type}", AssetType.CASH_AND_SAVINGS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value(AssetType.CASH_AND_SAVINGS.toString()))
                .andExpect(jsonPath("$[1].type").value(AssetType.CASH_AND_SAVINGS.toString()));
    }

    @Test
    @DisplayName("Should update asset value")
    @WithMockUser
    void shouldUpdateAssetValue() throws Exception {
        // Arrange
        BigDecimal newValue = new BigDecimal("2000.00");
        List<Asset> updatedAssets = Arrays.asList(
                new Asset("userId123", AssetType.CASH_AND_SAVINGS, newValue,
                        "Updated savings", true, LocalDateTime.now())
        );

        when(assetService.updateAssetValue(AssetType.CASH_AND_SAVINGS, newValue))
                .thenReturn(updatedAssets);

        // Act & Assert
        mockMvc.perform(put(BASE_URL + "/type/{type}/value", AssetType.CASH_AND_SAVINGS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newValue.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].value").value("2000.0"));
    }

    @Test
    @DisplayName("Should delete assets by type")
    @WithMockUser
    void shouldDeleteAssetsByType() throws Exception {
        // Act & Assert
        mockMvc.perform(delete(BASE_URL + "/type/{type}", AssetType.CASH_AND_SAVINGS))
                .andExpect(status().isNoContent());
    }
}