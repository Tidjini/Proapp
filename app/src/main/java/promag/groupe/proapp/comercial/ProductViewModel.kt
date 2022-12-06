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
import promag.groupe.proapp.global.messenger.messages.MessagesActivity
import promag.groupe.proapp.models.commercial.Product

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

    fun createProduct(product: Product) {
        viewModelScope.launch {
            try {

                app.commercialApi.createProduct("token ${app.user.token}", product)
                    ?: return@launch

                gotoProductssActivity()
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            try {

                app.commercialApi.updateProduct("token ${app.user.token}", product)
                    ?: return@launch

                gotoProductssActivity()
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }


    private fun gotoProductssActivity() {
        val i = Intent(app, MessagesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        app.startActivity(i)
    }


}