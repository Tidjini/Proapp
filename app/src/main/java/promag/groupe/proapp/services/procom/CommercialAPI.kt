package promag.groupe.proapp.services.procom

import promag.groupe.proapp.models.Product
import retrofit2.Response
import retrofit2.http.*

interface CommercialAPI {

    @GET("products/")
    suspend fun getProducts(@Header("Authorization") token: String): Response<List<Product>>?

    @POST("products/")
    suspend fun createProduct(
        @Header("Authorization") token: String,
        @Body product: Product
    ): Response<Product>?

    @PUT("products/{id}/")
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Body product: Product
    ): Response<Product>?


}