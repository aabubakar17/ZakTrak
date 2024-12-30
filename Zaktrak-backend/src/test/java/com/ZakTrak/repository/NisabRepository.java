package com.ZakTrak.repository;

import com.ZakTrak.model.Nisab;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
class NisabRepositoryTest {

    @Autowired
    private NisabRepository nisabRepository;

    @Test
    void shouldSaveAndRetrieveNisab() {
        Nisab nisab = new Nisab(
                new BigDecimal("800.00"),
                new BigDecimal("15.00"),
                LocalDate.now()
        );

        Nisab savedNisab = nisabRepository.save(nisab);
        Optional<Nisab> retrieved = nisabRepository.findById(savedNisab.getId());

        assertThat(retrieved)
                .isPresent()
                .get()
                .matches(n -> n.getGoldPricePerGram().equals(new BigDecimal("800.00")));
    }

    @Test
    void shouldFindLatestNisabThreshold() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();

        Nisab oldNisab = new Nisab(
                new BigDecimal("790.00"),
                new BigDecimal("14.00"),
                yesterday
        );

        Nisab newNisab = new Nisab(
                new BigDecimal("800.00"),
                new BigDecimal("15.00"),
                today
        );

        nisabRepository.save(oldNisab);
        nisabRepository.save(newNisab);

        Optional<Nisab> latest = nisabRepository.findFirstByOrderByPriceDateDesc();

        assertThat(latest)
                .isPresent()
                .get()
                .matches(n -> n.getPriceDate().equals(today));
    }
}