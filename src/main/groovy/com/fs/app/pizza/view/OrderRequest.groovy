package com.fs.app.pizza.view

import com.fasterxml.jackson.annotation.JsonInclude
import groovy.transform.EqualsAndHashCode

import javax.validation.constraints.NotNull

@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
class OrderRequest {
    String sauce

    List<String> toppings

    String cheese

    @NotNull
    Size size
}
