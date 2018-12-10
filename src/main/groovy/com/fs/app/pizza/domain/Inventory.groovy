package com.fs.app.pizza.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Inventory {
    @Id @GeneratedValue
    Long id

    @Column(name = "item", nullable = false)
    String item

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable

    @Column(name = "price")
    private Integer price
}
