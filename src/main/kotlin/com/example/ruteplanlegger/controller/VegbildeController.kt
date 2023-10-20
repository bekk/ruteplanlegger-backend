package com.example.ruteplanlegger.controller



import com.example.ruteplanlegger.model.VegBilde
import com.example.ruteplanlegger.service.VegbildeService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


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