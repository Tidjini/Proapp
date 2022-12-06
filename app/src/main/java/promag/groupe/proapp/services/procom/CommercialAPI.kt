package promag.groupe.proapp.services.procom

import promag.groupe.proapp.models.PageResponse
import promag.groupe.proapp.models.commercial.Product
import promag.groupe.proapp.models.commercial.StockMovement
import retrofit2.Response
import retrofit2.http.*

interface CommercialAPI {

    @GET("products/")
    suspend fun getProducts(@Header("Authorization") token: String): Response<PageResponse<Product>>?

    @POST("products/")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body product: Product
    ): Response<Product>?

    @PUT("products/{id}/")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body product: Product
    ): Response<Product>?


    @POST("stock-movements/")
    suspend fun createStockMovement(
        @Header("Authorization") token: String,
        @Body movement: StockMovement
    ): Response<StockMovement>?


}