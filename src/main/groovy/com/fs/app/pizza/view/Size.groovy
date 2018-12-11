package com.fs.app.pizza.view

import io.swagger.annotations.ApiModel

@ApiModel(value = 'Size', description = 'Pizza size')
enum Size {
    SMALL, MEDIUM, LARGE
}