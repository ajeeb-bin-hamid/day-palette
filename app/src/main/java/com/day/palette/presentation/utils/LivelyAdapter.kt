package com.day.palette.presentation.utils

typealias OnItemClickListener<T> = (position: Int, item: T, type: String) -> Unit
typealias OnItemLongPressListener<T> = (position: Int, item: T, type: String) -> Unit
typealias OnItemDoubleTapListener<T> = (position: Int, item: T, type: String) -> Unit

interface LivelyAdapter<T> {
    var onItemClickListener: OnItemClickListener<T>?
    var onItemLongPressListener: OnItemLongPressListener<T>?
    var onItemDoubleTapListener: OnItemDoubleTapListener<T>?
}