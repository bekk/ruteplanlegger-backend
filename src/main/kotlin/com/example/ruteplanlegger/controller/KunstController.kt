package com.example.ruteplanlegger.controller

import com.example.ruteplanlegger.model.Kunst
import com.example.ruteplanlegger.service.KunstService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/kunst")
@CrossOrigin(origins = ["*"])
class KunstController(val kunstService: KunstService) {

    @GetMapping
    fun getKunstOgUtsmykking(@RequestParam(required = false) anbefalt: Boolean): List<Kunst>? =
        if (anbefalt) kunstService.getRecommendedKunstOgUtsmykking() else kunstService.getKunstOgUtsmykking()

}