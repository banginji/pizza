package com.fs.app.pizza.view

import groovy.transform.EqualsAndHashCode
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

import javax.validation.constraints.NotNull

@EqualsAndHashCode
@ApiModel(value = 'OrderRequest', description = 'Order Request body')
class OrderRequest {
    @ApiModelProperty(value = 'sauce', required = false)
    String sauce

    @ApiModelProperty(value = 'toppings', required = false)
    List<String> toppings

    @ApiModelProperty(value = 'cheese', required = false)
    String cheese

    @NotNull
    @ApiModelProperty(value = 'size', required = true)
    Size size
}
