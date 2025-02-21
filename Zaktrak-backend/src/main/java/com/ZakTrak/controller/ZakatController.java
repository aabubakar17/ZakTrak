package com.ZakTrak.controller;

import com.ZakTrak.service.ZakatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;


import java.math.BigDecimal;

@RestController
@RequestMapping("/api/zakat")
@RequiredArgsConstructor
public class ZakatController {

    private final ZakatService zakatService;

    @GetMapping("/calculate/{type}")
    public BigDecimal calculateZakat(@PathVariable String type) {
        return zakatService.calculateZakat(type);
    }
}