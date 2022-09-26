package com.andreformento.app.util

import java.util.*

fun <T> Optional<T>.unwrap(): T? = orElse(null)
