package me.omico.core

/**
 * @author Omico 2020/12/16
 */
abstract class Singleton<T> {

    private var instance: T? = null

    protected abstract fun create(): T

    fun get(): T = synchronized(this) {
        return instance ?: create().also { instance = it }
    }
}
