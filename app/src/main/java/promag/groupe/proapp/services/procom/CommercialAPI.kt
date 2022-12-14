package promag.groupe.proapp.services.procom

import promag.groupe.proapp.comercial.models.Payment
import promag.groupe.proapp.comercial.models.Tier
import promag.groupe.proapp.models.PageResponse
import promag.groupe.proapp.models.commercial.Product
import promag.groupe.proapp.models.commercial.ProductComposition
import promag.groupe.proapp.models.commercial.StockMovement
import retrofit2.Response
import retrofit2.http.*

interface CommercialAPI {


    //Products
    @GET("products/")
    suspend fun getProducts(@Header("Authorization") token: String): Response<PageResponse<Product>>?

    @POST("products/")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body product: Product
    ): Response<Product>?

    @POST("compositions/")
    suspend fun createProoductCompostion(
        @Header("Authorization") token: String,
        @Body composition: ProductComposition
    ): Response<ProductComposition>?

    @PUT("products/{id}/")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body product: Product
    ): Response<Product>?

    //Stock
    @POST("stock-movements/")
    suspend fun createStockMovement(
        @Header("Authorization") token: String,
        @Body movement: StockMovement
    ): Response<StockMovement>?

    //Tier
    @GET("thirds/")
    suspend fun getTiers(@Header("Authorization") token: String): Response<PageResponse<Tier>>?

    @POST("thirds/")
    suspend fun createTier(
        @Header("Authorization") token: String,
        @Body tier: Tier
    ): Response<Tier>?

    @PUT("thirds/{id}/")
    suspend fun updateTier(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body tier: Tier
    ): Response<Tier>?
    //Payment
    @POST("payments/")
    suspend fun createPayment(
        @Header("Authorization") token: String,
        @Body payment: Payment
    ): Response<Payment>?
    @GET("payments/")
    suspend fun getPayments(@Header("Authorization") token: String): Response<PageResponse<Payment>>?



}