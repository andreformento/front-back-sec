package com.andreformento.app.util

import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable

abstract class BaseView<ID>(
    private var id: ID?,
) : Persistable<ID> {

    @org.springframework.data.annotation.Transient
    private var isNew = false

    @Id
    override fun getId() = id

    override fun isNew() = isNew

    fun markNew() {
        isNew = true
    }
}