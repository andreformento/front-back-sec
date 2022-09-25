package com.andreformento.app.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class ProviderNotIdentifiedException(message: String?) : RuntimeException(message)