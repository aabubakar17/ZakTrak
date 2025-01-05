package com.ZakTrak.repository;

import com.ZakTrak.model.ZakatPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface ZakatPaymentRepository extends MongoRepository<ZakatPayment, String> {

    List<ZakatPayment> findByUserId(String userId);

    List<ZakatPayment> findByUserIdAndPaymentDateBetween(
            String userId,
            LocalDate startDate,
            LocalDate endDate
    );

    @Aggregation(pipeline = {
            "{ $match: { 'userId': ?0, 'paymentDate': { $gte: ?1, $lte: ?2 } } }",
            "{ $group: { _id: null, total: { $sum: { $convert: { input: '$amount', to: 'double' } } } } }",
            "{ $project: { _id: 0, total: { $ifNull: ['$total', 0] } } }"
    })
    Double sumPaymentsByUserIdAndPaymentDateBetween(
            String userId,
            LocalDate startDate,
            LocalDate endDate
    );

    void deleteByUserId(String userId);

    Optional<ZakatPayment> findByIdAndUserId(String id, String userId);
}