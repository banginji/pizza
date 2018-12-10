package com.fs.app.pizza

import com.fs.app.pizza.domain.Inventory
import com.fs.app.pizza.repository.InventoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class PizzaApplication implements CommandLineRunner {

	static void main(String[] args) {
		SpringApplication.run PizzaApplication, args
	}

	@Autowired
	InventoryRepository inventoryRepository

	@Override
	void run(String... args) throws Exception {
		Inventory inventory = new Inventory(item: 'red sauce', isAvailable: true)
		inventoryRepository.save(inventory)
	}
}
