package com.example.reciperoulette.repositories.apiCallHandling

import com.example.reciperoulette.api.request.Request
import com.example.reciperoulette.api.response.Resource
import com.example.reciperoulette.dependencyInjection.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class ApiHandleRepository (
    // TODO: idk find a better way
    @IoDispatcher protected val ioDispatcher: CoroutineDispatcher
) {

    companion object {
        const val UNEXPECTED_ERROR = "An unexpected error has occurred."
        const val SERVER_ERROR = "Unable to access server. Please ensure you are connected to the network."
        const val UNKNOWN_ERROR = "Something went wrong."
    }

    suspend fun <T> apiPostReq(
        apiPostReq: ApiPostReqFunc<T>,
        userData: Request
    ): Resource<T> {
        return withContext(ioDispatcher) {
            try {
                val response = apiPostReq(userData)
                if (response.isSuccessful) {
                    response.body()?.let {
                        return@withContext Resource.Success(it)
                    }
                }
                Resource.Error(response.message())
            } catch (e: HttpException) {
                Resource.Error(e.localizedMessage ?: UNEXPECTED_ERROR)
            } catch (e: IOException) {
                // TODO: check error returned to see if it is useful, if so, use the localizedMessage
                Resource.Error(SERVER_ERROR)
            } catch (e: Exception) {
                // TODO: check error returned to see if it is useful, if so, use the localizedMessage
                Resource.Error(UNKNOWN_ERROR)
            }
        }
    }
}

typealias ApiPostReqFunc<T> = suspend (userData: Request) -> Response<T>