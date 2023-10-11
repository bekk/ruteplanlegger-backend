package com.example.ruteplanlegger.controller

import com.example.ruteplanlegger.model.Kjorerute
import com.example.ruteplanlegger.service.KjoreruteService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/kjoreruter")
@CrossOrigin(origins = ["*"])

class KjoreruteController(val kjoreruteService: KjoreruteService) {

    @GetMapping
    fun getFotruter(@RequestParam(required = false) anbefalt: Boolean):  List<Kjorerute>? {
        return kjoreruteService.getKjoreruteOsloHamar()
    }

}