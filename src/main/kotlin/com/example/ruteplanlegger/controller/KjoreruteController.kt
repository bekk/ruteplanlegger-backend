package com.example.ruteplanlegger.controller

import com.example.ruteplanlegger.model.Kjorerute
import com.example.ruteplanlegger.model.LatLong
import com.example.ruteplanlegger.service.KjoreruteService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/kjoreruter")
@CrossOrigin(origins = ["*"])

class KjoreruteController(val kjoreruteService: KjoreruteService) {

    @GetMapping
    @CrossOrigin(origins = ["*"])
    fun getKjoreruter(@RequestParam start: List<Double>, @RequestParam slutt: List<Double>):  ResponseEntity<Kjorerute>? {
        try {
            val res =  kjoreruteService.getKjoreruter(start, slutt)
            return ResponseEntity.ok(res)
        }
        catch (ex: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }


    @GetMapping
    @RequestMapping("/tourist")
    @CrossOrigin(origins = ["*"])
    fun getTouristKjoreruter(@RequestParam start: List<Double>, @RequestParam slutt: List<Double>):  ResponseEntity<Kjorerute>? {
        try {
            val res = kjoreruteService.getTouristKjoreruter(start, slutt)
            return ResponseEntity.ok(res)
        }
        catch (ex: Exception) {
            return ResponseEntity.badRequest().build()
        }

    }

}