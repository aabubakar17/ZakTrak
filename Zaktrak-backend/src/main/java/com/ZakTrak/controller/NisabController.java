package com.ZakTrak.controller;

import com.ZakTrak.model.Nisab;
import com.ZakTrak.dto.NisabRequest;
import com.ZakTrak.service.NisabService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;

@RestController
@RequestMapping("/api/nisab")
@RequiredArgsConstructor
public class NisabController {

    private final NisabService nisabService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Nisab updateNisabThreshold(@RequestBody NisabRequest request) {
        return nisabService.updateNisabThreshold(request.goldPrice(), request.silverPrice());
    }

    @GetMapping("/latest")
    public Nisab getLatestNisab() {
        return nisabService.getLatestNisabThreshold();
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(IllegalStateException e) {
        return e.getMessage();
    }
}