package promag.groupe.proapp.comercial

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import promag.groupe.proapp.BaseApplication
import promag.groupe.proapp.PRODUCT_EXTRA
import promag.groupe.proapp.models.commercial.Product
import promag.groupe.proapp.models.commercial.StockMovement
import java.lang.Double
import kotlin.Boolean
import kotlin.Exception
import kotlin.String
import kotlin.toString

class ProductViewModel(val app: BaseApplication) : ViewModel() {

    private val mProducts = mutableStateListOf<Product>()
    private val mProduct = mutableStateOf(Product())

    var errorMessage: String by mutableStateOf("")

    val products: List<Product>
        get() {
            return mProducts
        }

    val product: Product
        get() {
            return mProduct.value
        }

    fun getProducts() {
        viewModelScope.launch {
            try {
                mProducts.clear()
                val result = app.commercialApi.getProducts("token ${app.user.token}")
                    ?: return@launch


                mProducts.addAll(result.body()!!.results)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }


    fun save(name: String, qte: String, value: String, product: Product) {

        try {
            product.qteStock = Double.parseDouble(qte)
            product.value = Double.parseDouble(value)
            product.name = name

            if (product.id == null || product.id == 0) {
                return createProduct(product)
            }
            return updateProduct(product)

        } catch (e: Exception) {
            //todo display error message
        }
    }


    fun createStockMovement(
        document: String,
        qte: String,
        prixUnite: String,
        out: Boolean,
        product: Product
    ) {

        val q = Double.parseDouble(qte)
        val prix = Double.parseDouble(prixUnite)

        val move = StockMovement(
            document = document,
            qte = q,
            prixUnite = prix,
            product = product.id,
            out = out
        )

        viewModelScope.launch {
            try {

                val result = app.commercialApi.createStockMovement("token ${app.user.token}", move)


                gotoProductCollectionView()
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

    fun createProduct(product: Product) {
        viewModelScope.launch {
            try {

                app.commercialApi.createProduct("token ${app.user.token}", product)
                    ?: return@launch

                gotoProductCollectionView()
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            try {

                app.commercialApi.updateProduct("token ${app.user.token}", product.id!!, product)
                    ?: return@launch

                gotoProductCollectionView()
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }


    private fun gotoProductCollectionView() {
        val i = Intent(app, ProductCollectionView::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        app.startActivity(i)
    }

    fun createProductView() {

        val intent = Intent(app, ProductView::class.java)
        intent.putExtra(PRODUCT_EXTRA, Product())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS

        app.startActivity(intent)
    }

    fun editProductView(product: Product) {
        mProduct.value = product
        val intent = Intent(app, ProductView::class.java)
        intent.putExtra(PRODUCT_EXTRA, product)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        app.startActivity(intent)
    }

    fun setMovementView(product: Product) {
        mProduct.value = product
        val intent = Intent(app, StockMovementView::class.java)
        intent.putExtra(PRODUCT_EXTRA, product)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        app.startActivity(intent)
    }


}