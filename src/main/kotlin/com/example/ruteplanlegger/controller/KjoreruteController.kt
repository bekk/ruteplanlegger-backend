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
    fun getKjoreruter(@RequestParam start: List<Double>, @RequestParam slutt: List<Double>):  Kjorerute? {
        return kjoreruteService.getKjoreruter(start, slutt)
    }


    @GetMapping
    @RequestMapping("/tourist")
    fun getTouristKjoreruter(@RequestParam start: List<Double>, @RequestParam slutt: List<Double>):  Kjorerute? {
        return kjoreruteService.getTouristKjoreruter(start, slutt)
    }

}