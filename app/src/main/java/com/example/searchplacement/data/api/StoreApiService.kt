package com.example.searchplacement.data.api

import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.store.StoreResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreApiService {
    @GET("/api/store/all")
    suspend fun getAllStores(
        @Header("Authorization") token: String,
        @Query("sortBy") sortBy: String
    ): Response<ApiResponse<List<StoreResponse>>>

    @GET("/api/store/{storeId}")
    suspend fun getStoreData(
        @Header("Authorization") token: String,
        @Path("storeId") storeId: Long
    ): Response<ApiResponse<StoreResponse>>

    /** 가게 정렬 (category) */
    @GET("/api/store/all/category/{category}")
    suspend fun getStoresByCategory(
        @Header("Authorization") token: String,
        @Path("category") category: String,
        @Query("sortBy") sortBy: String
    ): Response<ApiResponse<List<StoreResponse>>>

    /** 가게 검색 */
    @GET("/api/store/search")
    suspend fun searchStoresByName(
        @Header("Authorization") token: String,
        @Query("storeName") storeName: String
    ): Response<ApiResponse<List<StoreResponse>>>


    //사업자
    /** 내 가게 정보 가져오기 */
    @GET("/api/store/my")
    suspend fun getMyStores(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<StoreResponse>>>

    /** 가게 정보 수정 */
    @Multipart
    @PUT("/api/store/update/{storeId}")
    suspend fun updateStore(
        @Header("Authorization") token: String,
        @Path("storeId") storeId: Long,
        @Part("data") storeData: RequestBody,
        @Part images: List<MultipartBody.Part>? = null
    ): Response<ApiResponse<Map<String, Any>>>

    /** 가게 등록*/
    @Multipart
    @POST("/api/store/register")
    suspend fun registerStore(
        @Header("Authorization") token: String,
        @Part("data") storeData: RequestBody,
        @Part images: List<MultipartBody.Part>? = null
    ): Response<ApiResponse<Map<String, Any>>>
}