package co.javacafe.satoshirealconverter

class AsyncTaskResult<T> {
    val result: T?
    val error: Exception?

    constructor(result: T) : super() {
        this.result = result
        this.error = null
    }

    constructor(error: Exception) : super() {
        this.error = error
        this.result = null
    }
}