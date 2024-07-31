package com.crm.edu.core

import com.crm.edu.utils.Constants

sealed class EResult<out T : Any> {

    class Success<out T : Any>(val data: T) : EResult<T>()

    class SuccessAndLoading<out T : Any>(val data: T) : EResult<T>()

    class Error(
        val exception: Throwable,
        val message: String = exception.message ?: Constants.ErrorMessage.UNKNOWN_ERROR
    ) : EResult<Nothing>()

    object Loading : EResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is SuccessAndLoading<*> -> "SuccessAndLoading[data=$data]"
            is Error -> "Error[exception=$exception, message=$message]"
            Loading -> "Loading"
        }
    }

    inline fun <Mapped : Any> map(transform: (data: T) -> Mapped): EResult<Mapped> {
        return when (this) {
            is Success -> Success(transform(data))
            is SuccessAndLoading -> SuccessAndLoading(transform(data))
            is Error -> this
            is Loading -> this
        }
    }

    // TODO: inline is not working here due to compiler error. Will be fixed in kotlin 1.4
    // We can then remove suspend modifier too
    suspend fun <Mapped : Any> mapOrError(transform: suspend (data: T) -> Mapped): EResult<Mapped> {
        return try {
            map { transform(it) }
        } catch (e: Exception) {
            Error(e)
        }
    }
}

/*
* R :- if success then value will be returned
*       if value null then NULL Pointer Exception
*       else throw appropriate error
**/
inline fun <R : Any> tryCatching(block: () -> R): EResult<R> {
    return try {
        EResult.Success(block())
    } catch (e: Throwable) {
        EResult.Error(e)
    }
}
