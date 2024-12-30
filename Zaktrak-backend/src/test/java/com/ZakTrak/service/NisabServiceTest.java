package com.ZakTrak.service;

import com.ZakTrak.model.Nisab;
import com.ZakTrak.repository.NisabRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NisabServiceTest {

    @Mock
    private NisabRepository nisabRepository;

    @InjectMocks
    private NisabService nisabService;

    @Test
    void shouldCreateNewNisabThreshold() {
        Nisab nisab = new Nisab(
                new BigDecimal("800.00"),
                new BigDecimal("15.00"),
                LocalDate.now()
        );
        when(nisabRepository.save(any(Nisab.class))).thenReturn(nisab);

        Nisab created = nisabService.updateNisabThreshold(
                new BigDecimal("800.00"),
                new BigDecimal("15.00")
        );

        assertThat(created.getGoldPricePerGram()).isEqualByComparingTo(new BigDecimal("800.00"));
    }

    @Test
    void shouldGetLatestNisabThreshold() {
        Nisab nisab = new Nisab(
                new BigDecimal("800.00"),
                new BigDecimal("15.00"),
                LocalDate.now()
        );
        when(nisabRepository.findFirstByOrderByPriceDateDesc()).thenReturn(Optional.of(nisab));

        Nisab latest = nisabService.getLatestNisabThreshold();

        assertThat(latest).isNotNull();
    }

    @Test
    void shouldThrowExceptionWhenNoNisabExists() {
        when(nisabRepository.findFirstByOrderByPriceDateDesc()).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class,
                () -> nisabService.getLatestNisabThreshold());
    }
}