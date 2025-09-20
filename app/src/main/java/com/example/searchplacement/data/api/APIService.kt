package com.example.searchplacement.data.api

import com.example.searchplacement.data.map.MapPinDetailResponse
import com.example.searchplacement.data.map.MapPinResponse
import com.example.searchplacement.data.member.ApiResponse
import com.example.searchplacement.data.member.FindPasswordRequest
import com.example.searchplacement.data.member.LoginRequest
import com.example.searchplacement.data.member.LoginResponse
import com.example.searchplacement.data.menu.MenuResponse
import com.example.searchplacement.data.menu.OutOfStockRequest
import com.example.searchplacement.data.placement.PlacementRequest
import com.example.searchplacement.data.placement.PlacementResponse
import com.example.searchplacement.data.placement.PlacementUpdateRequest
import com.example.searchplacement.data.reserve.ReservationRequest
import com.example.searchplacement.data.reserve.ReservationResponse
import com.example.searchplacement.data.review.ReviewResponse
import com.example.searchplacement.data.section.MenuSectionBulkUpdateRequest
import com.example.searchplacement.data.section.MenuSectionRequest
import com.example.searchplacement.data.section.MenuSectionResponse
import com.example.searchplacement.data.store.FavoriteResponse
import com.example.searchplacement.data.store.StoreResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @Multipart
    @POST("/api/auth/signup")
    suspend fun registerUser(
        @Part("data") userData: RequestBody,
        @Part image: MultipartBody.Part?
    ): ApiResponse<Map<String, Any>>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST("/api/auth/forgot-password")
    suspend fun forgotPassword(@Body request: FindPasswordRequest): Response<ApiResponse<Map<String, String>>>

    @PATCH("api/member/{userId}/password")
    suspend fun updatePassword(
        @Header("Authorization") token: String,
        @Path("userId") userId: Long,
        @Body passwordMap: Map<String, String>
    ): Response<ApiResponse<String>>

    @Multipart
    @PATCH("api/member/{userId}")
    suspend fun updateUserInfo(
        @Path("userId") userId: Long,
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part? = null,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Map<String, Any>>>



    /** 가게 */
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




    /** 즐겨찾기 */
    @POST("/api/favorites/{storeId}")
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Path("storeId") storeId: Long
    ): Response<ApiResponse<FavoriteResponse>>

    @DELETE("/api/favorites/{storeId}")
    suspend fun removeFavorite(
        @Header("Authorization") token: String,
        @Path("storeId") storeId: Long
    ): Response<ApiResponse<String>>

    @GET("/api/favorites/{userId}")
    suspend fun getFavoriteList(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<ApiResponse<List<FavoriteResponse>>>


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


    //메뉴 관련
    /** 메뉴 추가 */
    @Multipart
    @POST("/api/menu/add")
    suspend fun addMenu(
        @Header("Authorization") token: String,
        @Part("data") menuData: RequestBody,
        @Part image: MultipartBody.Part? = null
    ): Response<ApiResponse<Map<String, Any>>>


    /** 메뉴 수정 */
    @Multipart
    @PUT("/api/menu/update/{menuId}")
    suspend fun updateMenu(
        @Header("Authorization") token: String,
        @Path("menuId") menuId: Long,
        @Part("data") data: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ApiResponse<Map<String, Any>>>

    /** 메뉴 삭제 */
    @DELETE("/api/menu/del/{menuId}")
    suspend fun deleteMenu(
        @Header("Authorization") token: String,
        @Path("menuId") menuId: Long
    ): Response<ApiResponse<Map<String, Any>>>

    /** 전체 메뉴 조회 */
    @GET("/api/menu/store/{storePK}")
    suspend fun getMenus(
        @Header("Authorization") token: String,
        @Path("storePK") storePK: Long
    ): Response<ApiResponse<List<MenuResponse>>>


    /** 품절 */
    @PATCH("/api/menu/outofstock")
    suspend fun updateMenusStock(
        @Header("Authorization") token: String,
        @Body request: OutOfStockRequest
    ): Response<ApiResponse<List<Map<String, Any>>>>

    //자리 배치

    /** 자리 배치 생성 */
    @POST("/api/placement")
    suspend fun createPlacement(
        @Header("Authorization") token: String,
        @Body request: PlacementRequest
    ): Response<ApiResponse<PlacementResponse>>

    /** 매장별 자리배치 조회 */
    @GET("/api/placement/store/{storePK}")
    suspend fun getPlacementByStore(
        @Header("Authorization") token: String,
        @Path("storePK") storePK: Long
    ): Response<ApiResponse<PlacementResponse>>

    /** 자리 배치 업데이트 (테이블 상태 변경) */
    @PUT("/api/placement/{placementPK}")
    suspend fun updatePlacement(
        @Header("Authorization") token: String,
        @Path("placementPK") placementPK: Long,
        @Body request: PlacementUpdateRequest
    ): Response<ApiResponse<PlacementResponse>>

    //메뉴 섹션
    // 메뉴 섹션 전체 조회
    @GET("/api/menu-section/store/{storePK}")
    suspend fun getMenuSectionsByStore(
        @Header("Authorization") token: String,
        @Path("storePK") storePK: Long
    ): Response<ApiResponse<List<MenuSectionResponse>>>

    //섹션 단일 추가
    @POST("/api/menu-section/add/{storePK}")
    suspend fun addSection(
        @Header("Authorization") token: String,
        @Path("storePK") storePK: Long,
        @Body request: MenuSectionRequest
    ): Response<ApiResponse<Map<String, Any>>>

    // 메뉴 섹션 개별 업데이트
    @PUT("/api/menu-section/update/{sectionPK}")
    suspend fun updateMenuSection(
        @Header("Authorization") token: String,
        @Path("sectionPK") sectionPK: Long,
        @Body request: MenuSectionRequest
    ): Response<ApiResponse<Map<String, Any>>>

    // 메뉴 섹션 삭제
    @DELETE("/api/menu-section/delete/{sectionPK}")
    suspend fun deleteMenuSection(
        @Header("Authorization") token: String,
        @Path("sectionPK") sectionPK: Long
    ): Response<ApiResponse<Map<String, Any>>>

    // 메뉴 섹션 일괄 업데이트
    @PUT("/api/menu-section/bulk-update/{storePK}")
    suspend fun bulkUpdateMenuSections(
        @Header("Authorization") token: String,
        @Path("storePK") storePK: Long,
        @Body request: List<MenuSectionBulkUpdateRequest>
    ): Response<ApiResponse<List<MenuSectionResponse>>>



    //예약
    @POST("/api/reservations/create")
    suspend fun createReservation(
        @Header("Authorization") token: String,
        @Body request: ReservationRequest
    ): Response<ApiResponse<ReservationResponse>>

    @DELETE("/api/reservations/cancel/{reservationId}")
    suspend fun cancelReservation(
        @Header("Authorization") token: String,
        @Path("reservationId") reservationId: Long
    ): Response<ApiResponse<String>>

    @GET("/api/reservations/owner/{storeId}")
    suspend fun getOwnerReservations(
        @Header("Authorization") token: String,
        @Path("storeId") storeId: Long
    ): Response<ApiResponse<List<ReservationResponse>>>

    @GET("/api/reservations/user")
    suspend fun getUserReservations(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<ReservationResponse>>>

    @GET("/api/reservations/details/{reservationId}")
    suspend fun getReservationDetails(
        @Header("Authorization") token: String,
        @Path("reservationId") reservationId: Long
    ): Response<ApiResponse<ReservationResponse>>

    @Multipart
    @POST("/api/review/add")
    suspend fun addReview(
        @Header("Authorization") token: String,
        @Part("data") reviewData: RequestBody,
        @Part images: List<MultipartBody.Part>? = null
    ): Response<ApiResponse<Map<String, Any>>>

    @GET("/api/review/store/{storePK}")
    suspend fun getReviewsByStore(
        @Header("Authorization") token: String,
        @Path("storePK") storePK: Long
    ): Response<ApiResponse<List<ReviewResponse>>>


    //Pin 관련
    @GET("/api/map/pins")
    suspend fun getMapPins(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<MapPinResponse>>>

    @GET("/api/map/pin/{storePK}")
    suspend fun getMapPinDetail(
        @Header("Authorization") token: String,
        @Path("storePK") storePK: Long
    ): Response<ApiResponse<MapPinDetailResponse>>



}