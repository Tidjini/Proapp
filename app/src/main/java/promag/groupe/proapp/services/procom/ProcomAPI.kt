package promag.groupe.proapp.services.procom

import promag.groupe.proapp.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ProcomAPI {
    @GET("products/")
    suspend fun getProducts(): Response<List<Product>?>?

    @GET("encaissements/")
    suspend fun getEncaissements(): Response<List<Encaissement>?>?

    @GET("charges/")
    suspend fun getFactures(): Response<List<Facture>?>?

    @POST("charges/")
    fun postFacture(@Body facture: Facture?): Response<Facture?>?

    @POST("charges/")
    fun createFacture(@Body facture: Facture?): Call<Facture?>?

//    @PUT("charges/")
//    fun updateFacture(@Body facture: Facture?): Call<Facture?>?

    @PUT("charges/{id}/")
    fun updateFacture(@Path("id") id: Int, @Body facture: Facture?): Call<Facture?>?

    @DELETE("charges/{id}/")
    fun deleteFacture(@Path("id") id: Int): Call<Any?>?


    @POST("auth/username/")
    fun authUsername(@Body auth: Auth?): Call<User?>?

    @GET("auth/token/")
    fun authToken(@Header("Authorization") token: String): Call<User?>?


    @GET("discussions/")
    suspend fun getDiscussions(@Header("Authorization") token: String): Response<PageResponse<Discussion>>?

    @POST("discussions/{id}/")
    suspend fun getDiscussion(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Discussion?>?

    @GET("messages/")
    suspend fun getMessages(
        @Header("Authorization") token: String,
        @Query("discussion") id: Int
    ): Response<PageResponse<Message>>?

    @POST("messages/")
    fun sendMessage(@Header("Authorization") token: String, @Body message: Message): Call<Message?>?


    @POST("discussions/")
    suspend fun createDiscussion(
        @Header("Authorization") token: String,
        @Body discussion: DiscussionCreator
    ): Response<Discussion?>?

    @GET("utilisateurs/")
    suspend fun getUsers(@Header("Authorization") token: String): Response<List<User>>?

}