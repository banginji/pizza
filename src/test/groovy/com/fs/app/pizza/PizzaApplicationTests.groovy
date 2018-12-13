package com.fs.app.pizza

import com.fasterxml.jackson.databind.ObjectMapper
import com.fs.app.pizza.controller.PizzaController
import com.fs.app.pizza.domain.Discount
import com.fs.app.pizza.domain.Topping
import com.fs.app.pizza.repository.CheeseRepository
import com.fs.app.pizza.repository.SauceRepository
import com.fs.app.pizza.repository.ToppingRepository
import com.fs.app.pizza.view.OrderRequest
import com.fs.app.pizza.view.Size
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PizzaApplicationTests extends BaseTest {

    @Autowired
    PizzaController pizzaController

    @Autowired
    CheeseRepository cheeseRepository

    @Autowired
    SauceRepository sauceRepository

    @Autowired
    ToppingRepository toppingRepository

    private final String ORDER_URL = '/v1/order'

    private MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(pizzaController)
                .setControllerAdvice(pizzaController)
                .alwaysDo(print())
                .build()
    }

    def 'testing basic pizza order for SMALL size'() {
        given:
        OrderRequest orderRequest = new OrderRequest(
                cheese: 'mozarella',
                sauce: 'red sauce',
                toppings: ['mushrooms'],
                size: Size.SMALL
        )

        when:
        def result = mockMvc.perform(post(ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andReturn()

        then:
        result.response.contentAsString == '12.14'
    }

    def 'testing basic pizza order for MEDIUM size'() {
        given:
        OrderRequest orderRequest = new OrderRequest(
                cheese: 'mozarella',
                sauce: 'red sauce',
                toppings: ['mushrooms'],
                size: Size.MEDIUM
        )

        when:
        def result = mockMvc.perform(post(ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andReturn()

        then:
        result.response.contentAsString == '16.54'
    }

    def 'testing basic pizza order for LARGE size'() {
        given:
        OrderRequest orderRequest = new OrderRequest(
                cheese: 'mozarella',
                sauce: 'red sauce',
                toppings: ['mushrooms'],
                size: Size.LARGE
        )

        when:
        def result = mockMvc.perform(post(ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andReturn()

        then:
        result.response.contentAsString == '20.94'
    }

    def 'testing basic pizza order for three toppings'() {
        given:
        OrderRequest orderRequest = new OrderRequest(
                cheese: 'mozarella',
                sauce: 'red sauce',
                toppings: ['mushrooms', 'onions', 'black olive'],
                size: Size.LARGE
        )

        when:
        def result = mockMvc.perform(post(ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andReturn()

        then:
        result.response.contentAsString == '23.78'
    }

    def 'testing basic pizza order when discount is enabled'() {
        given:
        OrderRequest orderRequest = new OrderRequest(
                cheese: 'mozarella',
                sauce: 'red sauce',
                toppings: ['mushrooms', 'onions', 'black olive'],
                size: Size.LARGE
        )

        Discount.anyDeals = true

        when:
        def result = mockMvc.perform(post(ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andReturn()

        then:
        result.response.contentAsString == '21.62'
    }

    def 'testing basic pizza order when cheese does not exist'() {
        given:
        OrderRequest orderRequest = new OrderRequest(
                cheese: 'non exitent',
                sauce: 'red sauce',
                toppings: ['mushrooms'],
                size: Size.SMALL
        )

        expect:
        mockMvc.perform(post(ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isNotFound())
    }

    def 'testing basic pizza order when sauce does not exist'() {
        given:
        OrderRequest orderRequest = new OrderRequest(
                cheese: 'mozarella',
                sauce: 'non exitent',
                toppings: ['mushrooms'],
                size: Size.SMALL
        )

        expect:
        mockMvc.perform(post(ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isNotFound())
    }

    def 'testing basic pizza order when topping does not exist'() {
        given:
        OrderRequest orderRequest = new OrderRequest(
                cheese: 'mozarella',
                sauce: 'red sauce',
                toppings: ['non exitent'],
                size: Size.SMALL
        )

        expect:
        mockMvc.perform(post(ORDER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isNotFound())
    }

    def 'get all cheese'() {
        expect:
        mockMvc.perform(get('/v1/admin/cheese'))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$').isArray())
                .andExpect(jsonPath('$.length()').value(3))
    }

    def 'get all sauce'() {
        expect:
        mockMvc.perform(get('/v1/admin/sauce'))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$').isArray())
                .andExpect(jsonPath('$.length()').value(2))
    }

    def 'get all toppings'() {
        expect:
        mockMvc.perform(get('/v1/admin/toppings'))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$').isArray())
                .andExpect(jsonPath('$.length()').value(6))
    }

    def 'testing create cheese'() {
        given:
        String cheeseType = 'pepper jack'

        when:
        mockMvc.perform(post("/v1/admin/cheese/$cheeseType"))
                .andExpect(status().isCreated())

        then:
        assert cheeseRepository.findByType(cheeseType)
    }

    def 'testing create cheese that already exists'() {
        given:
        String cheeseType = 'mozarella'

        expect:
        mockMvc.perform(post("/v1/admin/cheese/$cheeseType"))
                .andExpect(status().isConflict())
    }

    def 'testing create sauce'() {
        given:
        String sauceType = 'light red sauce'

        when:
        mockMvc.perform(post("/v1/admin/sauce/$sauceType"))
                .andExpect(status().isCreated())

        then:
        assert sauceRepository.findByType(sauceType)
    }

    def 'testing create sauce that already exists'() {
        given:
        String sauceType = 'red sauce'

        expect:
        mockMvc.perform(post("/v1/admin/sauce/$sauceType"))
                .andExpect(status().isConflict())
    }

    def 'testing create topping'() {
        given:
        String toppingName = 'cauliflower'
        Double price = 1.49
        Topping topping = new Topping(toppingName: toppingName, price: price)

        when:
        mockMvc.perform(post("/v1/admin/topping")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(topping)))
                .andExpect(status().isCreated())

        then:
        assert toppingRepository.findByToppingName(toppingName)
    }

    def 'testing create topping that already exists'() {
        given:
        String toppingName = 'onions'
        Double price = 1.49
        Topping topping = new Topping(toppingName: toppingName, price: price)

        expect:
        mockMvc.perform(post("/v1/admin/topping")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(topping)))
                .andExpect(status().isConflict())
    }

    def 'testing delete cheese'() {
        given:
        String cheeseType = 'mozarella'

        when:
        mockMvc.perform(delete("/v1/admin/cheese/$cheeseType"))
                .andExpect(status().isNoContent())

        then:
        assert !cheeseRepository.findByType(cheeseType)
    }

    def 'testing delete non existent cheese'() {
        given:
        String cheeseType = 'non existent'

        expect:
        mockMvc.perform(delete("/v1/admin/cheese/$cheeseType"))
                .andExpect(status().isNotFound())
    }

    def 'testing delete sauce'() {
        given:
        String sauceType = 'red sauce'

        when:
        mockMvc.perform(delete("/v1/admin/sauce/$sauceType"))
                .andExpect(status().isNoContent())

        then:
        assert !sauceRepository.findByType(sauceType)
    }

    def 'testing delete non existent sauce'() {
        given:
        String sauceType = 'non existent'

        expect:
        mockMvc.perform(delete("/v1/admin/sauce/$sauceType"))
                .andExpect(status().isNotFound())
    }

    def 'testing delete topping'() {
        given:
        String toppingName = 'onions'

        when:
        mockMvc.perform(delete("/v1/admin/topping/$toppingName"))
                .andExpect(status().isNoContent())

        then:
        assert !toppingRepository.findByToppingName(toppingName)
    }

    def 'testing delete non existent topping'() {
        given:
        String toppingName = 'non existent'

        when:
        mockMvc.perform(delete("/v1/admin/topping/$toppingName"))
                .andExpect(status().isNotFound())

        then:
        assert !toppingRepository.findByToppingName(toppingName)
    }
}
