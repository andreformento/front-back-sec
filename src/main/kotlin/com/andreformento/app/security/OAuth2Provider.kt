package com.andreformento.app.security


enum class OAuth2Provider {
    GITHUB, GOOGLE;

    companion object {
        fun getByName(name: String?): OAuth2Provider? =
            values().find { it.name.equals(name, ignoreCase = true) }
    }
}
