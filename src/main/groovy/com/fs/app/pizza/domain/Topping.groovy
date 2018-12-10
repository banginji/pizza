package com.fs.app.pizza.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Topping {
    @Id @GeneratedValue
    private Long id

    @Column(name = 'topping_name', nullable = false)
    String toppingName

    @Column(name = 'price', nullable = false)
    Integer price
}
