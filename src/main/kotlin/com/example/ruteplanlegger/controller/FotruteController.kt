package com.example.ruteplanlegger.controller

import com.example.ruteplanlegger.model.Fotrute
import com.example.ruteplanlegger.service.FotruteService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["*"])
class FotruteController(val fotruteService: FotruteService) {


    @RequestMapping("/fotruter")
    fun getFotruter(): List<Fotrute> {

        return fotruteService.getFotruter()


    }

}