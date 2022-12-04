package promag.groupe.proapp.infrabitume

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import promag.groupe.proapp.R
import promag.groupe.proapp.databinding.ActivityEncaissementBinding
import promag.groupe.proapp.databinding.ActivityLivraisonBinding
import promag.groupe.proapp.services.procom.ProcomAPI
import promag.groupe.proapp.services.procom.ProcomService

class Encaissement : AppCompatActivity() {

    private lateinit var viewBinder: ActivityEncaissementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = ActivityEncaissementBinding.inflate(layoutInflater)
        setContentView(viewBinder.root)
    }


    override fun onResume() {
        super.onResume()
        getResult()
    }

    fun getResult() {
        val quotesApi = ProcomService.getInstance().create(ProcomAPI::class.java)
        // launching a new coroutine
        viewBinder.loading = true
        viewBinder.emptyList = false

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val result = quotesApi.getEncaissements() ?: return@launch

                viewBinder.loading = false
                if (result.body()!!.isEmpty()) {
                    viewBinder.emptyList = true
                    return@launch
                }
                for (item in result.body()!!) {
                    if (item.reference == "day") {
                        viewBinder.todayCard.encaissement = item
                        viewBinder.todayCard.todayGrowth.setImageResource(item.icon)
                    }
                    if (item.reference == "week") {
                        viewBinder.weekCard.encaissement = item

                        viewBinder.weekCard.weekGrowth.setImageResource(item.icon)
                    }

                    if (item.reference == "month") {
                        viewBinder.monthCard.encaissement = item

                        viewBinder.monthCard.monthGrowth.setImageResource(item.icon)
                    }


                }


//                adapter!!.setList(result.body()!!)
//                val item = result.body()!![0]
//                val df1: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
//                val result1: Date = df1.parse(item.update_at)
//                viewBinder.updatedAt.text = "Dernière MÀJ: ${result1.toString()}"


            } catch (e: Exception) {
                viewBinder.loading = false
                viewBinder.emptyList = true
//                viewBinder.updatedAt.visibility = View.GONE
                Log.e("Exception: ", e.toString())
            }

        }
    }
}