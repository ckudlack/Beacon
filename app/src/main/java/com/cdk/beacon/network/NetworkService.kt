package com.cdk.beacon.network

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    @FormUrlEncoded
    @POST("some/url/appended/to/base")
    fun postStringToServer(@Field("aString") string: String): Call<Any>

    @POST("some/url/{pathName}/to/base")
    fun postImageWithOtherParams(@Path("pathName") param: Long,
                                 @QueryMap params: Map<String, String>,
                                 @Body image: RequestBody): Call<Any>

    @GET("some/url/appended/to/base")
    fun getStringFromServer(@Query("paramName") someParameter: String): Call<String>

    @GET("some/url/appended/to/base")
    fun getItemFromServerWithLotsOfParams(@QueryMap params: Map<String, String>): Call<Any>
}
