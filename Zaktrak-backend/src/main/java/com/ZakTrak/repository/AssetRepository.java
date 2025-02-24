package com.ZakTrak.repository;


import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ZakTrak.model.Asset;
import java.util.List;
import java.util.Optional;

import com.ZakTrak.model.AssetType;


@Repository
public interface AssetRepository extends MongoRepository<Asset, String> {
    List<Asset> findByUserId(String userId);

    List<Asset> findByUserIdAndZakatableTrue(String userId);

    List<Asset> findByUserIdAndType(String userId, AssetType type);

    void deleteByUserId(String userId);

    Optional<Asset>  findByIdAndUserId(String id, String userId);


}
