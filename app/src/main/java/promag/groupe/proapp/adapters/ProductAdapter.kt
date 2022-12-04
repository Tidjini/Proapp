package promag.groupe.proapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import promag.groupe.proapp.R
import promag.groupe.proapp.databinding.LivraisonItemBinding
import promag.groupe.proapp.models.Product
import java.lang.ref.WeakReference


class ProductAdapter(private val context: Context, private val callback: Listener) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    private var list: List<Product> = ArrayList<Product>()


    fun initList() {
        list = ArrayList<Product>()
        notifyDataSetChanged()

    }

    fun setList(list: List<Product>) {

        this.list = list
        notifyDataSetChanged()
    }


    fun getProduct(position: Int): Product? {
        if (position >= list.size || position < 0) return null
        return list[position]
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val itemBinding: LivraisonItemBinding = DataBindingUtil.inflate(
            inflater, R.layout.livraison_item, parent, false
        )
        return ProductViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val task = list[position]
        holder.bind(task, callback)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class ProductViewHolder(private val viewBinder: LivraisonItemBinding) :
        RecyclerView.ViewHolder(viewBinder.root),
        View.OnClickListener {

        private var callback: WeakReference<Listener>? = null

        fun bind(product: Product, listener: Listener) {
            viewBinder.product = product
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