package promag.groupe.proapp.infrabitume

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import promag.groupe.proapp.R
import promag.groupe.proapp.adapters.FactureAdapter
import promag.groupe.proapp.databinding.ActivityFactureBinding
import promag.groupe.proapp.databinding.ActivityLivraisonBinding
import promag.groupe.proapp.services.procom.ProcomAPI
import promag.groupe.proapp.services.procom.ProcomService

class FactureActivity : AppCompatActivity(), FactureAdapter.Listener {
    private lateinit var viewBinder: ActivityFactureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = ActivityFactureBinding.inflate(layoutInflater)

        setContentView(viewBinder.root)
        intializeAdapter()
    }

    fun onRefresh(view: View) {
        getResult()
    }

    fun onReturn(view: View) {
//        viewBinder.factureImageContainer.visibility = View.GONE
    }

    fun onAddNew(view : View?){
        val intent = Intent(this, FactureDetailActivity::class.java)
        startActivity(intent)
    }

    var adapter: FactureAdapter? = null


    fun intializeAdapter() {
        adapter = FactureAdapter(this, this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        viewBinder.factureList.layoutManager = layoutManager
        viewBinder.factureList.setHasFixedSize(true)
        viewBinder.factureList.adapter = adapter

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
//        viewBinder.updatedAt.visibility = View.GONE

        adapter!!.initList()
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val result = quotesApi.getFactures() ?: return@launch

                viewBinder.loading = false
                if (result.body()!!.isEmpty()) {
                    viewBinder.emptyList = true
                    return@launch
                }


                adapter!!.setList(result.body()!!)
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


    override fun onItemClicked(position: Int) {
//        viewBinder.factureImageContainer.visibility = View.VISIBLE
        val facture = adapter!!.getFacture(position)
        val intent = Intent(this, FactureDetailActivity::class.java)
        intent.putExtra("FACTURE_DATA", facture);
        startActivity(intent)

//        Picasso.get().load(facture!!.picture).into(viewBinder.factureImage);
    }
}