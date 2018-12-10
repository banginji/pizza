package com.fs.app.pizza.domain

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Sauce {
    @Id @GeneratedValue
    private Long id

    @Column(name = 'type', nullable = false)
    String type
}
