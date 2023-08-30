package com.example.ruteplanlegger.controller

import com.example.ruteplanlegger.Service.KunstService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/kunst")
@CrossOrigin(origins = ["*"])
class KunstController(val kunstService: KunstService) {

    @GetMapping
    fun getKunstOgUtsmykking(): JsonNode? {
        return kunstService.getKunstOgUtsmykking()
    }
}