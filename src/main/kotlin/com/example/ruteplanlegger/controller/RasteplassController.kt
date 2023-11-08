package com.example.ruteplanlegger.controller

import com.example.ruteplanlegger.model.Rasteplass
import com.example.ruteplanlegger.service.RasteplassService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rasteplasser")
@CrossOrigin(origins = ["*"])
class Rasteplasser(val rasteplasserService: RasteplassService) {

    @GetMapping
    fun getRasteplasser(@RequestParam(required = false) anbefalt: Boolean): List<Rasteplass>? =
        if (anbefalt) rasteplasserService.getRecommendedRasteplasser() else rasteplasserService.getRasteplasser()

}