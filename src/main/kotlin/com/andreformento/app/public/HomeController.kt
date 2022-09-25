package com.andreformento.app.public

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/")
class HomeController {

    @GetMapping
    suspend fun index() = "index"

    @GetMapping("/login")
    suspend fun login() = "index"

    @GetMapping(value=["/organizations", "/organizations/**"])
    suspend fun organizations() = "index"

    @GetMapping("/oauth2/redirect")
    suspend fun oauth2Redirect() = "index"

}
