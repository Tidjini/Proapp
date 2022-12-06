package promag.groupe.proapp.services.procom




import promag.groupe.proapp.BASE_URL_API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ProcomService {



    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }



}
