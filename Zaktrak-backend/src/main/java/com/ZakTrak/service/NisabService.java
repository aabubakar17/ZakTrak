package com.ZakTrak.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ZakTrak.repository.NisabRepository;
import com.ZakTrak.model.Nisab;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class NisabService {

    private final NisabRepository nisabRepository;

    public Nisab getLatestNisabThreshold() {
        return nisabRepository.findFirstByOrderByPriceDateDesc().orElseThrow(() -> new IllegalStateException("No Nisab found"));
    }

    public Nisab updateNisabThreshold(BigDecimal goldPrice, BigDecimal silverPrice) {
        Nisab nisab = new Nisab(goldPrice, silverPrice, LocalDate.now());
        return nisabRepository.save(nisab);
    }

    public BigDecimal getGoldNisab() {
        return getLatestNisabThreshold().getGoldNisab();
    }

    public BigDecimal getSilverNisab() {
        return getLatestNisabThreshold().getSilverNisab();
    }



}
