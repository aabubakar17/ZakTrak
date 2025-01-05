package com.ZakTrak.config;

import com.ZakTrak.model.Nisab;
import com.ZakTrak.repository.NisabRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.math.BigDecimal;
import java.time.LocalDate;




@Configuration
public class NisabConfig {

    @Bean
    CommandLineRunner initNisab(NisabRepository nisabRepository) {
        return args -> {
            if (nisabRepository.count() == 0) {
                Nisab defaultNisab = new Nisab(
                        new BigDecimal("85.00"), // Default gold price
                        new BigDecimal("0.95"),  // Default silver price
                        LocalDate.now()
                );
                nisabRepository.save(defaultNisab);
            }
        };
    }
}