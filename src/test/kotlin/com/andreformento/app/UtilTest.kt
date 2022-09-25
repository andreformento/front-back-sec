package com.andreformento.app

import org.springframework.test.web.reactive.server.WebTestClient

fun WebTestClient.ResponseSpec.printBody(): WebTestClient.ResponseSpec {
    this.expectBody().consumeWith(System.out::println)
    return this
}
