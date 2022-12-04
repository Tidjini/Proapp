package promag.groupe.proapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import promag.groupe.proapp.R
import promag.groupe.proapp.databinding.FactureLayoutBinding
import promag.groupe.proapp.models.Facture
import java.lang.ref.WeakReference


class FactureAdapter(private val context: Context, private val callback: Listener) :
    RecyclerView.Adapter<FactureAdapter.FactureViewHolder>() {


    private var list: List<Facture> = ArrayList<Facture>()


    fun initList() {
        list = ArrayList<Facture>()
        notifyDataSetChanged()

    }

    fun setList(list: List<Facture>) {

        this.list = list
        notifyDataSetChanged()
    }


    fun getFacture(position: Int): Facture? {
        if (position >= list.size || position < 0) return null
        return list[position]
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FactureViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemBinding: FactureLayoutBinding = DataBindingUtil.inflate(
            inflater, R.layout.facture_layout, parent, false
        )
        return FactureViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: FactureViewHolder, position: Int) {
        val task = list[position]
        holder.bind(task, callback)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class FactureViewHolder(private val viewBinder: FactureLayoutBinding) :
        RecyclerView.ViewHolder(viewBinder.root),
        View.OnClickListener {

        private var callback: WeakReference<Listener>? = null

        fun bind(facture: Facture, listener: Listener) {
            viewBinder.facture = facture
            viewBinder.facImag.setImageResource(facture.icon)


            viewBinder.infraStockItem.setOnClickListener(this)
            callback = WeakReference(listener)

        }

        override fun onClick(view: View?) {
            if (callback == null || view == null) return
            val callback = callback!!.get()

            if (view.id == R.id.infra_stock_item) callback!!.onItemClicked(adapterPosition)

        }


    }


    interface Listener {
        fun onItemClicked(position: Int)
    }


}