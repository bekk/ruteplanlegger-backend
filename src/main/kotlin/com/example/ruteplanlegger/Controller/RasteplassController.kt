package com.example.ruteplanlegger.Controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rasteplasser")
class Rasteplasser() {

    @GetMapping
    fun getRasteplasser(): String {
        return "Legg til funksjon for å hente rasteplasser fra repository"
    }
}