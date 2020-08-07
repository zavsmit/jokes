package com.zavsmit.jokes.core

import android.util.Log
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.regex.Pattern


/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class ApiResponse<T> {
    companion object {
        private val LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(error.message ?: "unknown error", 0)
        }

        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == HttpURLConnection.HTTP_NO_CONTENT) {
                    ApiEmptyResponse()
                } else {
                    val linkHeader = response.headers().get("link")
                    ApiSuccessResponse(
                            body,
                            linkHeader?.extractLinks() ?: emptyMap(),
                            response.code()
                    )
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(errorMsg ?: "unknown error", response.code())
            }
        }

        private fun String.extractLinks(): Map<String, String> {
            val links = mutableMapOf<String, String>()
            val matcher = LINK_PATTERN.matcher(this)

            while (matcher.find()) {
                val count = matcher.groupCount()
                if (count == 2) {
                    val group2 = matcher.group(2)
                    val group1 = matcher.group(1)
                    if (group2 != null && group1 != null) links[group2] = group1
                }
            }
            return links
        }
    }
}

/**
 * separate class for HTTP 204 resposes so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(
        val body: T,
        val links: Map<String, String>,
        val code: Int
) : ApiResponse<T>() {
    val nextPage: Int? by lazy(LazyThreadSafetyMode.NONE) {
        links[NEXT_LINK]?.let { next ->
            val matcher = PAGE_PATTERN.matcher(next)
            if (!matcher.find() || matcher.groupCount() != 1) {
                null
            } else {
                try {
                    Integer.parseInt(matcher.group(1))
                } catch (ex: NumberFormatException) {
                    Log.e("nextPage", "cannot parse next page from $next")
                    null
                }
            }
        }
    }

    companion object {
        private val PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)")
        private const val NEXT_LINK = "next"
    }
}

data class ApiErrorResponse<T>(
        val errorMessage: String,
        val code: Int
) : ApiResponse<T>()