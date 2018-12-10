package com.fs.app.pizza.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.fs.app.pizza.domain.Cheese

interface CheeseRepository extends JpaRepository<Cheese, Long> {
    Optional<Cheese> findByType(String type)
}