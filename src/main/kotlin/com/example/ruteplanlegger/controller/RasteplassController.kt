package com.example.ruteplanlegger.controller

import com.example.ruteplanlegger.model.Rasteplass
import com.example.ruteplanlegger.service.RasteplasserService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rasteplasser")
@CrossOrigin(origins = ["*"])
class Rasteplasser(val rasteplasserService: RasteplasserService) {

    @GetMapping
    fun getRasteplasser(): List<Rasteplass>? {
        return rasteplasserService.getAllRasteplasser()
    }
}