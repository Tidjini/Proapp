package promag.groupe.proapp.services.procom

import promag.groupe.proapp.models.PageResponse
import promag.groupe.proapp.tasks.models.Task
import retrofit2.Response
import retrofit2.http.*


interface TasksAPI {


    //Products
    @GET("tasks/")
    suspend fun getTasks(
        @Header("Authorization") token: String,
        @Query("type") type: Int
    ): Response<PageResponse<Task>>?

    @POST("tasks/")
    suspend fun createTask(
        @Header("Authorization") token: String,
        @Body task: Task
    ): Response<Task>?

    @PUT("tasks/{id}/")
    suspend fun updateTask(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body task: Task
    ): Response<Task>?


}