package com.example.ruteplanlegger.controller



import com.example.ruteplanlegger.model.VegBilde
import com.example.ruteplanlegger.service.VegbildeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.lang.Exception


@RestController
@RequestMapping("/vegbilde")
@CrossOrigin(origins = ["*"])
class VegbildeController(private val vegbildeService: VegbildeService) {

    @GetMapping("/{bbox}")
    fun getVegbilde(@PathVariable bbox: String): ResponseEntity<List<VegBilde>> {
        try {
            val vegbilde = vegbildeService.getVegbilde(bbox)
            return ResponseEntity.ok(vegbilde)

        }
        catch (ex: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }
}