package com.fs.app.pizza

import com.fasterxml.jackson.databind.ObjectMapper
import com.fs.app.pizza.domain.Topping
import com.fs.app.pizza.view.OrderRequest
import com.fs.app.pizza.view.Size
import com.google.common.net.HttpHeaders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.Base64Utils
import org.springframework.web.context.WebApplicationContext

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class SecurityTests extends BaseTest {

    @Autowired
    WebApplicationContext context

    private MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private ADMIN_AUTH_HEADER_VALUE = "Basic " + Base64Utils.encodeToString("ADMIN:ADMIN".getBytes())

    def 'order authorization not required'() {
        given:
        OrderRequest orderRequest = new OrderRequest(
                cheese: 'mozarella',
                sauce: 'red sauce',
                toppings: ['mushrooms'],
                size: Size.SMALL
        )

        expect:
        mockMvc.perform(post('/v1/order')
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
    }

    def 'create cheese authorized'() {
        given:
        String cheeseType = 'pepper jack'

        expect:
        mockMvc.perform(post("/v1/admin/cheese/$cheeseType")
                .header(HttpHeaders.AUTHORIZATION, ADMIN_AUTH_HEADER_VALUE))
                .andExpect(status().isCreated())
    }

    def 'create cheese unauthorized'() {
        given:
        String cheeseType = 'pepper jack'

        expect:
        mockMvc.perform(post("/v1/admin/cheese/$cheeseType"))
                .andExpect(status().isUnauthorized())
    }

    def 'create sauce authorized'() {
        given:
        String sauceType = 'light red sauce'

        expect:
        mockMvc.perform(post("/v1/admin/sauce/$sauceType")
                .header(HttpHeaders.AUTHORIZATION, ADMIN_AUTH_HEADER_VALUE))
                .andExpect(status().isCreated())
    }

    def 'create sauce unauthorized'() {
        given:
        String sauceType = 'light red sauce'

        expect:
        mockMvc.perform(post("/v1/admin/sauce/$sauceType"))
                .andExpect(status().isUnauthorized())
    }

    def 'create topping authorized'() {
        given:
        String toppingName = 'cauliflower'
        Double price = 1.49
        Topping topping = new Topping(toppingName: toppingName, price: price)

        expect:
        mockMvc.perform(post("/v1/admin/topping")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(topping))
                .header(HttpHeaders.AUTHORIZATION, ADMIN_AUTH_HEADER_VALUE))
                .andExpect(status().isCreated())
    }

    def 'create topping unauthorized'() {
        given:
        String toppingName = 'cauliflower'
        Double price = 1.49
        Topping topping = new Topping(toppingName: toppingName, price: price)

        expect:
        mockMvc.perform(post("/v1/admin/topping")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(topping)))
                .andExpect(status().isUnauthorized())
    }

    def 'set discount authorized'() {}
    def 'set discount unauthorized'() {}

    def 'delete cheese authorized'() {
        given:
        String cheeseType = 'mozarella'

        expect:
        mockMvc.perform(delete("/v1/admin/cheese/$cheeseType")
                .header(HttpHeaders.AUTHORIZATION, ADMIN_AUTH_HEADER_VALUE))
                .andExpect(status().isNoContent())
    }

    def 'delete cheese unauthorized'() {
        given:
        String cheeseType = 'mozarella'

        expect:
        mockMvc.perform(delete("/v1/admin/cheese/$cheeseType"))
                .andExpect(status().isUnauthorized())
    }

    def 'delete sauce authorized'() {
        given:
        String sauceType = 'red sauce'

        expect:
        mockMvc.perform(delete("/v1/admin/sauce/$sauceType")
                .header(HttpHeaders.AUTHORIZATION, ADMIN_AUTH_HEADER_VALUE))
                .andExpect(status().isNoContent())
    }

    def 'delete sauce unauthorized'() {
        given:
        String sauceType = 'red sauce'

        expect:
        mockMvc.perform(delete("/v1/admin/sauce/$sauceType"))
                .andExpect(status().isUnauthorized())
    }

    def 'delete topping authorized'() {
        given:
        String toppingName = 'onions'

        expect:
        mockMvc.perform(delete("/v1/admin/topping/$toppingName")
                .header(HttpHeaders.AUTHORIZATION, ADMIN_AUTH_HEADER_VALUE))
                .andExpect(status().isNoContent())
    }

    def 'delete topping unauthorized'() {
        given:
        String toppingName = 'onions'

        expect:
        mockMvc.perform(delete("/v1/admin/topping/$toppingName"))
                .andExpect(status().isUnauthorized())
    }

    def 'get cheese authorized'() {
        expect:
        mockMvc.perform(get('/v1/admin/cheese')
                .header(HttpHeaders.AUTHORIZATION, ADMIN_AUTH_HEADER_VALUE))
                .andExpect(status().isOk())
    }

    def 'get cheese unauthorized'() {
        expect:
        mockMvc.perform(get('/v1/admin/cheese'))
                .andExpect(status().isUnauthorized())
    }

    def 'get sauce authorized'() {
        expect:
        mockMvc.perform(get('/v1/admin/sauce')
                .header(HttpHeaders.AUTHORIZATION, ADMIN_AUTH_HEADER_VALUE))
                .andExpect(status().isOk())
    }

    def 'get sauce unauthorized'() {
        expect:
        mockMvc.perform(get('/v1/admin/sauce'))
                .andExpect(status().isUnauthorized())
    }

    def 'get topping authorized'() {
        expect:
        mockMvc.perform(get('/v1/admin/toppings')
                .header(HttpHeaders.AUTHORIZATION, ADMIN_AUTH_HEADER_VALUE))
                .andExpect(status().isOk())
    }

    def 'get topping unauthorized'() {
        expect:
        mockMvc.perform(get('/v1/admin/toppings'))
                .andExpect(status().isUnauthorized())
    }
}
