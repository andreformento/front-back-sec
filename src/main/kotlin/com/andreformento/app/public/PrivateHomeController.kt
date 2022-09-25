package com.andreformento.app.public

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class PrivateHomeController {

    @GetMapping("/profile")
    suspend fun profile() = "index"

}
