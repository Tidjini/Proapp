package promag.groupe.proapp.services.procom




import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProcomService {

    val baseUrl = "https://procom-tracker.herokuapp.com/api/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}