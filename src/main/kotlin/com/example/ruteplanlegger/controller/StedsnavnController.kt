package com.example.ruteplanlegger.controller

import com.example.ruteplanlegger.model.Stedsnavn
import com.example.ruteplanlegger.service.StedsnavnService
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/stedsnavn")
@CrossOrigin(origins = ["*"])
class StedsnavnController (val stedsnavnService: StedsnavnService) {
    @GetMapping
    fun getStedsnavn(@RequestParam name: String) : List<Stedsnavn>?{
        return stedsnavnService.getStedsnavn(name)
    }
}
