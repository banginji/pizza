package com.fs.app.pizza.view

import com.fasterxml.jackson.annotation.JsonInclude
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
class Status {
    String status
}
