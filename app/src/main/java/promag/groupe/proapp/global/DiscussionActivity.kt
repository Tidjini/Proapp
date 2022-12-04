package promag.groupe.proapp.global

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import promag.groupe.proapp.BaseActivity
import promag.groupe.proapp.adapters.DiscussionAdapter
import promag.groupe.proapp.databinding.ActivityDiscussionBinding


class DiscussionActivity : BaseActivity(), DiscussionAdapter.Listener {


    private lateinit var viewBinder: ActivityDiscussionBinding
    var adapter: DiscussionAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinder = ActivityDiscussionBinding.inflate(layoutInflater)
        setContentView(viewBinder.root)
        intializeAdapter()

    }

    fun intializeAdapter() {
        adapter = DiscussionAdapter(this, this)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        viewBinder.productList.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        viewBinder.productList.layoutManager = layoutManager
        viewBinder.productList.setHasFixedSize(true)
        viewBinder.productList.itemAnimator = DefaultItemAnimator()
        viewBinder.productList.adapter = adapter

    }


    override fun onResume() {
        super.onResume()
        getResult()
    }


    fun getResult() {
        // launching a new coroutine
        viewBinder.loading = true
        viewBinder.emptyList = false
//        viewBinder.updatedAt.visibility = View.GONE

        adapter!!.initList()
        GlobalScope.launch(Dispatchers.Main) {
            try {


                val token = mApplication.user.token ?: return@launch

                val result = mApplication.quotesApi.getDiscussions("token $token") ?: return@launch

                viewBinder.loading = false
                if (result.body() == null) {
                    viewBinder.emptyList = true
                    return@launch
                }

                val list = result.body()!!.results
                adapter!!.setList(list)


            } catch (e: Exception) {
                viewBinder.loading = false
                viewBinder.emptyList = true
                Log.e("Exception: ", e.toString())
            }

        }
    }


    override fun onItemClicked(position: Int) {
//        viewBinder.productImageContainer.visibility = View.VISIBLE
//        val product = adapter!!.getProduct(position)
//
//        Picasso.get().load(product!!.picture).into(viewBinder.productImage);
    }

}
