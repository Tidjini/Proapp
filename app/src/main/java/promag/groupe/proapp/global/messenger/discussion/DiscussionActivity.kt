package promag.groupe.proapp.global.messenger.discussion

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import promag.groupe.proapp.BaseActivity
import promag.groupe.proapp.DISCUSSION_EXTRA
import promag.groupe.proapp.MainActivity
import promag.groupe.proapp.adapters.DiscussionAdapter
import promag.groupe.proapp.databinding.ActivityDiscussionBinding
import promag.groupe.proapp.global.messenger.messages.MessagesActivity


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

    override fun onLocationPermissionGranted(
        fineLocationPermissionGranted: Boolean,
        coarseLocationPermissionGranted: Boolean
    ) {
        TODO("Not yet implemented")
    }

    override fun onOverlaySettingGranted() {
        TODO("Not yet implemented")
    }

    override fun onGpsActivated() {
        TODO("Not yet implemented")
    }

    override fun onGpsDeactivated() {
        TODO("Not yet implemented")
    }

    override fun onRequirementsChecked() {
        TODO("Not yet implemented")
    }

    fun createDisccusion(view: View) {

        val intent = Intent(this, Contacts::class.java)
        startActivity(intent)

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
        val discussion = adapter!!.getDiscussion(position)
//
//        Picasso.get().load(product!!.picture).into(viewBinder.productImage);

        val i = Intent(this, MessagesActivity::class.java)
        i.putExtra(DISCUSSION_EXTRA, discussion)

        startActivity(i)
    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}
