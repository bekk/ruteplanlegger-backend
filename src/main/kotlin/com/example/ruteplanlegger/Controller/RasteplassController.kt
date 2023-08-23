package com.example.ruteplanlegger.Controller
import com.example.ruteplanlegger.service.RasteplasserService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rasteplasser")
@CrossOrigin(origins = ["*"])
class Rasteplasser(val rasteplasserService: RasteplasserService) {

    @GetMapping
    fun getRasteplasser(): String? {
        return rasteplasserService.getAllRasteplasser()
    }


}