package com.example.ruteplanlegger.controller

import com.example.ruteplanlegger.model.Kjorerute
import com.example.ruteplanlegger.model.LatLong
import com.example.ruteplanlegger.service.KjoreruteService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/kjoreruter")
@CrossOrigin(origins = ["*"])

class KjoreruteController(val kjoreruteService: KjoreruteService) {

    @GetMapping
    fun getFotruter(@RequestParam start: List<Double>, @RequestParam slutt: List<Double>):  List<Kjorerute>? {
        return kjoreruteService.getKjoreruteOsloHamar()
    }

}