package com.fs.app.pizza.repository

import com.fs.app.pizza.domain.Inventory
import org.springframework.data.jpa.repository.JpaRepository

interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByItem(String item)
}