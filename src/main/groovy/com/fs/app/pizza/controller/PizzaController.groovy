package com.fs.app.pizza.controller

import com.fs.app.pizza.domain.*
import com.fs.app.pizza.repository.CheeseRepository
import com.fs.app.pizza.repository.SauceRepository
import com.fs.app.pizza.repository.ToppingRepository
import com.fs.app.pizza.view.OrderRequest
import com.fs.app.pizza.view.Status
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException

@RestController
@Slf4j
@ControllerAdvice
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
    @ResponseStatus(code = HttpStatus.OK)
    Double order(@RequestBody OrderRequest request) {
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

        Double toppingsPriceTotal = 0.0

        if (request.toppings) {
            request.toppings.forEach {
                Topping topping = toppingRepository.findByToppingName(it).orElseThrow({
                    new EntityNotFoundException()
                })
                toppingsPriceTotal += topping.price
            }
        }

        Double basePrice = new BasePrice(request.size).getPrice()
        Double totalPrice = (basePrice + toppingsPriceTotal)
        Double tax = 0.1 * totalPrice
        if(Discount.anyDeals) {
            totalPrice -= totalPrice * ( Discount.discountPercentage / 100 )
        }

        return (totalPrice + tax).round(2)
    }

    @PostMapping(value = '/v1/admin/cheese/{cheeseType}')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    void createCheese(@PathVariable String cheeseType) {
        Optional<Cheese> chesseFromRepo = cheeseRepository.findByType(cheeseType)
        if(chesseFromRepo.isPresent()) {
            throw new EntityExistsException()
        } else {
            cheeseRepository.save(new Cheese(type: cheeseType))
        }
    }

    @PostMapping(value = '/v1/admin/sauce/{sauceType}')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    void createSauce(@PathVariable String sauceType) {
        Optional<Sauce> sauce = sauceRepository.findByType(sauceType)
        if(sauce.isPresent()) {
            throw new EntityExistsException()
        } else {
            sauceRepository.save(new Sauce(type: sauceType))
        }
    }

    @PostMapping(value = '/v1/admin/topping')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    void createTopping(@RequestBody Topping topping) {
        Optional<Topping> toppingFromRepo = toppingRepository.findByToppingName(topping.toppingName)
        if(toppingFromRepo.isPresent()) {
            throw new EntityExistsException()
        } else {
            toppingRepository.save(topping)
        }
    }

    @PutMapping(value = '/v1/admin/discount/{anyDeals}/{discountPercentage}')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    void setDiscount(@PathVariable boolean anyDeals, @PathVariable Double discountPercentage) {
        Discount.anyDeals = anyDeals
        Discount.discountPercentage = discountPercentage
    }

    @DeleteMapping(value = '/v1/admin/cheese/{cheeseType}')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteCheese(@PathVariable String cheeseType) {
        Cheese cheese = cheeseRepository.findByType(cheeseType)
                .orElseThrow({
            new EntityNotFoundException()
        })
        cheeseRepository.delete(cheese)
    }

    @DeleteMapping(value = '/v1/admin/sauce/{sauceType}')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteSauce(@PathVariable String sauceType) {
        Sauce sauce = sauceRepository.findByType(sauceType).orElseThrow({
            new EntityNotFoundException()
        })
        sauceRepository.delete(sauce)
    }

    @DeleteMapping(value = '/v1/admin/topping/{toppingName}')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteTopping(@PathVariable String toppingName) {
        Topping topping = toppingRepository.findByToppingName(toppingName).orElseThrow({
            new EntityNotFoundException()
        })
        toppingRepository.delete(topping)
    }

    @GetMapping(value = '/v1/admin/cheese')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    List<Cheese> getCheese() {
        cheeseRepository.findAll()
    }

    @GetMapping(value = '/v1/admin/sauce')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    List<Sauce> getSauce() {
        sauceRepository.findAll()
    }

    @GetMapping(value = '/v1/admin/toppings')
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    List<Topping> getToppings() {
        toppingRepository.findAll()
    }

    @ExceptionHandler(EntityNotFoundException.class)
    ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity.notFound().build()
    }

    @ExceptionHandler(EntityExistsException.class)
    ResponseEntity<?> handleEntityExistsException(EntityExistsException exception) {
        return new ResponseEntity<Object>(HttpStatus.CONFLICT)
    }
}
