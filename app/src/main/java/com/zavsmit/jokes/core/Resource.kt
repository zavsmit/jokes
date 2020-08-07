package com.zavsmit.jokes.core

class Resource<T>(
        var status: Status,
        var data: T?,
        var message: String? = null,
        var responseCode: Int? = null) {

    companion object {
        fun <T> success(data: T, responseCode: Int? = null): Resource<T> {
            return Resource(
                    Status.SUCCESS,
                    data,
                    responseCode = responseCode
            )
        }

        fun <T> error(message: String, data: T, responseCode: Int): Resource<T> {
            return Resource(
                    Status.ERROR,
                    data,
                    message,
                    responseCode
            )
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(
                    Status.LOADING,
                    data
            )
        }
    }

    enum class Status { SUCCESS, ERROR, LOADING }
}