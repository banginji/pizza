package com.fs.app.pizza

import com.fs.app.pizza.domain.Cheese
import com.fs.app.pizza.domain.Sauce
import com.fs.app.pizza.domain.Topping
import com.fs.app.pizza.repository.CheeseRepository
import com.fs.app.pizza.repository.SauceRepository
import com.fs.app.pizza.repository.ToppingRepository
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
	CheeseRepository cheeseRepository

	@Autowired
	SauceRepository sauceRepository

	@Autowired
	ToppingRepository toppingRepository

	@Override
	void run(String... args) throws Exception {
		Cheese mozarella = new Cheese(type: 'mozarella')
		cheeseRepository.save(mozarella)

		Cheese parmesan = new Cheese(type: 'parmesan')
		cheeseRepository.save(parmesan)

		Cheese cheddar = new Cheese(type: 'cheddar')
		cheeseRepository.save(cheddar)

		Sauce redSauce = new Sauce(type: 'red sauce')
		sauceRepository.save(redSauce)

		Sauce biancaSauce = new Sauce(type: 'bianca')
		sauceRepository.save(biancaSauce)

		Topping blackOlive = new Topping(toppingName: 'black olive', price: 1.29)
		toppingRepository.save(blackOlive)

		Topping greenOlive = new Topping(toppingName: 'green olive', price: 1.29)
		toppingRepository.save(greenOlive)

		Topping greenPeppers = new Topping(toppingName: 'green peppers', price: 1.29)
		toppingRepository.save(greenPeppers)

		Topping mushrooms = new Topping(toppingName: 'mushrooms', price: 1.29)
		toppingRepository.save(mushrooms)

		Topping onions = new Topping(toppingName: 'onions', price: 1.29)
		toppingRepository.save(onions)

		Topping pineapple = new Topping(toppingName: 'pineapple', price: 1.29)
		toppingRepository.save(pineapple)
	}
}
