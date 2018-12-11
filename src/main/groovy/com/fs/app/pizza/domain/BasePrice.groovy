package com.fs.app.pizza.domain

import com.fs.app.pizza.view.Size

class BasePrice {
    Double price

    BasePrice(Size size) {
        switch (size) {
            case size.SMALL:
                price = 9.75
                break

            case size.MEDIUM:
                price = 13.75
                break

            case size.LARGE:
                price = 17.75
                break

            default:
                break
        }
    }
}
