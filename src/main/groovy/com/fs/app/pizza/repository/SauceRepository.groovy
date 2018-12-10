package com.fs.app.pizza.repository

import com.fs.app.pizza.domain.Sauce
import org.springframework.data.jpa.repository.JpaRepository

interface SauceRepository extends JpaRepository<Sauce, Long> {
    Optional<Sauce> findByType(String type)
}