package com.zac4j.system.network

import com.zac4j.system.network.Result.Success
import com.zac4j.system.util.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

sealed class Result<out R> {
  data class Success<out T>(val data: T) : Result<T>()
  data class Error(val exception: Exception) : Result<Nothing>()
}

class LoginRepository() {

  @ExperimentalCoroutinesApi
  suspend fun login(json: String): Result<Response> {
    return withContext(Dispatchers.IO) {
      val httpClient = OkHttpClient.Builder()
          .build()

      // create request body
      val mediaType = json.toMediaType()
      val body = json.toRequestBody(mediaType)

      // create POST request
      val request = Request.Builder()
          .url(LOGIN_URL)
          .post(body)
          .build()

      // make request call
      Success(
          httpClient.newCall(request)
              .await()
      )
    }
  }

  companion object {
    private const val LOGIN_URL = ""
  }
}