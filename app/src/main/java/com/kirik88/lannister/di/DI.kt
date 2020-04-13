package com.kirik88.lannister.di

import kotlin.reflect.KClass

object DI {

    val components = mutableMapOf<KClass<*>, Any>()

    inline fun <reified T: Any> set(component: T) {
        components[T::class] = component
    }

    inline fun <reified T: Any> get() : T {
        return components[T::class] as T
    }
}