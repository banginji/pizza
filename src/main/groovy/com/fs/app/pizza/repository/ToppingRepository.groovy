package com.fs.app.pizza.repository

import com.fs.app.pizza.domain.Topping
import org.springframework.data.jpa.repository.JpaRepository

interface ToppingRepository extends JpaRepository<Topping, Long> {
    Optional<Topping> findByToppingName(String name)
}