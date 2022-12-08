package promag.groupe.proapp.comercial.viewmodels

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
import promag.groupe.proapp.comercial.TierCollectionView
import promag.groupe.proapp.comercial.StockMovementView
import promag.groupe.proapp.comercial.models.Payment
import promag.groupe.proapp.comercial.models.Tier
import promag.groupe.proapp.models.commercial.StockMovement



class TierViewModel(val app: BaseApplication) : ViewModel() {

    private val mTiers = mutableStateListOf<Tier>()
    private val mTier = mutableStateOf(Tier())

    var errorMessage: String by mutableStateOf("")

    val tiers: List<Tier>
        get() {
            return mTiers
        }

    val tier: Tier
        get() {
            return mTier.value
        }

    fun getTiers() {
        viewModelScope.launch {
            try {
                mTiers.clear()
                val result = app.commercialApi.getTiers("token ${app.user.token}")
                    ?: return@launch


                mTiers.addAll(result.body()!!.results)
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }


    fun save(label: String, type: String, debit: Double, credit: Double, tier: Tier) {

        try {
            tier.label = label
            tier.debit = debit
            tier.credit = credit
            tier.type = type


            if (tier.id == null || tier.id == 0) {
                return createTier(tier)
            }
            return updateTier(tier)

        } catch (e: Exception) {
            //todo display error message
        }
    }


    fun createPayment(
        label: String,
        montant: String,
        out: Boolean,
        tier: Tier
    ) {


        val mtn = montant.toDouble()

        val payment = Payment(
            document = document,
            qte = q,
            prixUnite = prix,
            tier = tier.id,
            out = out
        )

        viewModelScope.launch {
            try {

                val result = app.commercialApi.createStockMovement("token ${app.user.token}", move)


                gotoTierCollectionView()
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

    fun createTier(tier: Tier) {
        viewModelScope.launch {
            try {

                app.commercialApi.createTier("token ${app.user.token}", tier)
                    ?: return@launch

                gotoTierCollectionView()
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }

    fun updateTier(tier: Tier) {
        viewModelScope.launch {
            try {

                app.commercialApi.updateTier("token ${app.user.token}", tier.id!!, tier)
                    ?: return@launch

                gotoTierCollectionView()
            } catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }


    private fun gotoTierCollectionView() {
        val i = Intent(app, TierCollectionView::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        app.startActivity(i)
    }

    fun createTierView() {

        val intent = Intent(app, TierView::class.java)
        intent.putExtra(PRODUCT_EXTRA, Tier())
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS

        app.startActivity(intent)
    }

    fun editTierView(tier: Tier) {
        mTier.value = tier
        val intent = Intent(app, TierView::class.java)
        intent.putExtra(PRODUCT_EXTRA, tier)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        app.startActivity(intent)
    }

    fun setMovementView(tier: Tier) {
        mTier.value = tier
        val intent = Intent(app, StockMovementView::class.java)
        intent.putExtra(PRODUCT_EXTRA, tier)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        app.startActivity(intent)
    }


}