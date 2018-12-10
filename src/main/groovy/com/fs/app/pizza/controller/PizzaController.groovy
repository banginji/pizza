package com.fs.app.pizza.controller

import com.fs.app.pizza.domain.Cheese
import com.fs.app.pizza.domain.Sauce
import com.fs.app.pizza.domain.Topping
import com.fs.app.pizza.repository.CheeseRepository
import com.fs.app.pizza.repository.InventoryRepository
import com.fs.app.pizza.repository.SauceRepository
import com.fs.app.pizza.repository.ToppingRepository
import com.fs.app.pizza.view.OrderRequest
import com.fs.app.pizza.view.Status
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.persistence.EntityNotFoundException

@RestController
class PizzaController {
    @Autowired
    CheeseRepository cheeseRepository

    @Autowired
    SauceRepository sauceRepository

    @Autowired
    ToppingRepository toppingRepository

    @GetMapping(value = "/v1/status")
    @ResponseBody
    public Status status() {
        return new Status(status: 'OK')
    }

    @PostMapping(value = "/v1/order")
    @ResponseBody
    void order(@RequestBody OrderRequest request) {
        // Checking if cheese / sauce / topping is present in request and if present then the value parsed
        // out is used to check the respective repositories
        if (request.cheese) {
            cheeseRepository.findByType(request.cheese).orElseThrow({
                new EntityNotFoundException()
            })
        }

        if (request.sauce) {
            sauceRepository.findByType(request.sauce).orElseThrow({
                new EntityNotFoundException()
            })
        }

        if (request.toppings) {
            request.toppings.forEach {
                toppingRepository.findByToppingName(it).orElseThrow({
                    new EntityNotFoundException()
                })
            }
        }
    }

    @PostMapping(value = '/v1/admin/cheese/{cheeseType}')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    void createCheese(@PathVariable String cheeseType) {
        cheeseRepository.findByType(cheeseType)
                .orElse(cheeseRepository.save(new Cheese(type: cheeseType)))
    }

    @PostMapping(value = '/v1/admin/sauce/{sauceType}')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    void createSauce(@PathVariable String sauceType) {
        sauceRepository.findByType(sauceType)
                .orElse(sauceRepository.save(new Sauce(type: sauceType)))
    }

    @PostMapping(value = '/v1/admin/topping')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    void createTopping(@RequestBody Topping topping) {
        toppingRepository.findByToppingName(topping.toppingName)
                .orElse(toppingRepository.save(topping))
    }

    @DeleteMapping(value = '/v1/admin/cheese/{cheeseType}')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteCheese(@PathVariable String cheeseType) {
        cheeseRepository.findByType(cheeseType)
                .orElseThrow({
            new EntityNotFoundException()
        })
    }

    @DeleteMapping(value = '/v1/admin/sauce/{sauceType}')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteSauce(@PathVariable String sauceType) {
        sauceRepository.findByType(sauceType).orElseThrow({
            new EntityNotFoundException()
        })
    }

    @DeleteMapping(value = '/v1/admin/topping/{toppingName}')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteTopping(@PathVariable String toppingName) {
        toppingRepository.findByToppingName(toppingName).orElseThrow({
            new EntityNotFoundException()
        })
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity.notFound().build()
    }
}
