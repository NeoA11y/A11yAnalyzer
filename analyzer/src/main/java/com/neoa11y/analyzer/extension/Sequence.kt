package com.neoa11y.analyzer.extension

inline fun <reified T> Sequence<*>.first(): T {
    val iterator = iterator()

    while (iterator.hasNext()) {
        val item = iterator.next()

        if (item is T) {
            return item
        }
    }

    throw NoSuchElementException(
        "Sequence contains no element matching the predicate."
    )
}
