package com.example.ruteplanlegger.controller

import com.example.ruteplanlegger.model.Fotrute
import com.example.ruteplanlegger.service.FotruteService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/fotruter")
@CrossOrigin(origins = ["*"])

class FotruteController(val fotruteService: FotruteService) {

    @GetMapping
    fun getFotruter(@RequestParam(required = false) anbefalt: Boolean): List<Fotrute> {
        if (anbefalt == true) {
            return fotruteService.getRecommendedFotruter()
        }
        return fotruteService.getFotruter()
    }

}