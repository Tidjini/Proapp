package promag.groupe.proapp.services.procom




import promag.groupe.proapp.Environment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ProcomService {



    fun getInstance(): Retrofit {

        val env = Environment("prod")

        return Retrofit.Builder().baseUrl(env.BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }



}
