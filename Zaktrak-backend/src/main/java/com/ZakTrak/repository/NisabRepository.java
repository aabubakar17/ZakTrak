package com.ZakTrak.repository;
import com.ZakTrak.model.Nisab;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NisabRepository extends MongoRepository<Nisab, String> {

    Optional<Nisab> findFirstByOrderByPriceDateDesc();

}
