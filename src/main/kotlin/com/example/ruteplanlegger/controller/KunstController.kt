package com.example.ruteplanlegger.controller

import com.example.ruteplanlegger.model.Kunst
import com.example.ruteplanlegger.service.KunstService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/kunst")
@CrossOrigin(origins = ["*"])
class KunstController(val kunstService: KunstService) {

    @GetMapping
    fun getKunstOgUtsmykking(): List<Kunst>? {
        return kunstService.getKunstOgUtsmykking()
    }
}