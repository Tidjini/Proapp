package promag.groupe.proapp.infrabitume

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import promag.groupe.proapp.adapters.ProductAdapter
import promag.groupe.proapp.databinding.ActivityLivraisonBinding
import promag.groupe.proapp.services.procom.ProcomAPI
import promag.groupe.proapp.services.procom.ProcomService
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class LivraisonActivity : AppCompatActivity(), ProductAdapter.Listener {
    private lateinit var viewBinder: ActivityLivraisonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = ActivityLivraisonBinding.inflate(layoutInflater)
        setContentView(viewBinder.root)
        intializeAdapter()
    }

    fun onRefresh(view: View) {
        getResult()
    }
    fun onReturn(view: View){
        viewBinder.productImageContainer.visibility = View.GONE
    }

    var adapter: ProductAdapter? = null


    fun intializeAdapter() {
        adapter = ProductAdapter(this, this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        viewBinder.productList.layoutManager = layoutManager
        viewBinder.productList.setHasFixedSize(true)
        viewBinder.productList.adapter = adapter

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
        viewBinder.updatedAt.visibility = View.GONE

        adapter!!.initList()
        GlobalScope.launch(Dispatchers.Main){
            try {
                val result = quotesApi.getProducts() ?: return@launch

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
                viewBinder.updatedAt.visibility = View.GONE
                Log.e("Exception: ", e.toString())
            }

        }
    }




    override fun onItemClicked(position: Int) {
        viewBinder.productImageContainer.visibility = View.VISIBLE
        val product = adapter!!.getProduct(position)

        Picasso.get().load(product!!.picture).into(viewBinder.productImage);
    }
}