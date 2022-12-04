package promag.groupe.proapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import promag.groupe.proapp.R
import promag.groupe.proapp.databinding.DiscussionItemBinding
import promag.groupe.proapp.models.Discussion
import java.lang.ref.WeakReference


class DiscussionAdapter(private val context: Context, private val callback: Listener) :
    RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder>() {


    private var list: List<Discussion> = ArrayList<Discussion>()


    fun initList() {
        list = ArrayList<Discussion>()
        notifyDataSetChanged()

    }

    fun setList(list: List<Discussion>) {

        this.list = list
        notifyDataSetChanged()
    }


    fun getDiscussion(position: Int): Discussion? {
        if (position >= list.size || position < 0) return null
        return list[position]
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscussionViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemBinding: DiscussionItemBinding = DataBindingUtil.inflate(
            inflater, R.layout.discussion_item, parent, false
        )
        return DiscussionViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: DiscussionViewHolder, position: Int) {
        val task = list[position]
        holder.bind(task, callback)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class DiscussionViewHolder(private val viewBinder: DiscussionItemBinding) :
        RecyclerView.ViewHolder(viewBinder.root),
        View.OnClickListener {

        private var callback: WeakReference<Listener>? = null

        fun bind(discussion: Discussion, listener: Listener) {
            viewBinder.discussion = discussion
            viewBinder.discussionItem.setOnClickListener(this)
            callback = WeakReference(listener)

        }

        override fun onClick(view: View?) {
            if (callback == null || view == null) return
            val callback = callback!!.get()

            if (view.id == R.id.discussion_item) callback!!.onItemClicked(adapterPosition)

        }


    }


    interface Listener {
        fun onItemClicked(position: Int)
    }


}