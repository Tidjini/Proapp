package promag.groupe.proapp.infrabitume

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import promag.groupe.proapp.databinding.ActivityFactureDetailBinding
import promag.groupe.proapp.models.Facture
import promag.groupe.proapp.services.procom.ProcomAPI
import promag.groupe.proapp.services.procom.ProcomService
import promag.groupe.proapp.views.AppAlertDialog
import promag.groupe.proapp.views.AppToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FactureDetailActivity : AppCompatActivity() {
    private lateinit var viewBinder: ActivityFactureDetailBinding
    private lateinit var quotesApi: ProcomAPI
    public var facture: Facture? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_facture_detail)

        viewBinder = ActivityFactureDetailBinding.inflate(layoutInflater)
        quotesApi = ProcomService.getInstance().create(ProcomAPI::class.java)

        setContentView(viewBinder.root)
    }


    override fun onResume() {
        super.onResume()
        facture = intent.getSerializableExtra("FACTURE_DATA") as Facture?
        if (facture == null) {
            facture = Facture()

        }
        viewBinder.facture = facture
        viewBinder.numberEdt.setText(facture!!.number)
        viewBinder.remarqueEdt.setText(facture!!.remarque)
        viewBinder.montantEdt.setText(facture!!.montant.toString())
        viewBinder.isNew = facture!!.id == 0
    }


    fun onSave(view: View?) {
        setFacture()
        if (this.facture!!.id == 0)
            createFacture()
        else {
            updateFacture()
        }
    }

    fun setFacture() {
        val number = viewBinder.numberEdt.text.toString()
        val remarque = viewBinder.remarqueEdt.text.toString()
        var montant = 0f
        try {
            montant = viewBinder.montantEdt.text.toString().toFloat()
        } catch (e: Exception) {

        }


        facture!!.number = number
        facture!!.remarque = remarque
        facture!!.montant = montant

    }

    fun createFacture() {
        val result = quotesApi.createFacture(this.facture) ?: return
        result.enqueue(object : Callback<Facture?> {
            override fun onResponse(call: Call<Facture?>, response: Response<Facture?>) {
                if (response.errorBody() != null || response.body()!!.id == 0) {
                    AppToast(
                        this@FactureDetailActivity,
                        "Problem de sauvgarde, verifier vos champs",
                        true
                    )
                    return
                }
                AppToast(this@FactureDetailActivity, "Sauvgarde with success", true)
                returnToFactures()
            }

            override fun onFailure(call: Call<Facture?>, t: Throwable) {
                Log.e("Exception: ", t.toString())
                AppToast(
                    this@FactureDetailActivity,
                    "Problem de sauvgarde, verifier vos champs",
                    true
                )
            }

        })
    }

    fun updateFacture() {

        val result = quotesApi.updateFacture(facture!!.id, facture) ?: return
        result.enqueue(object : Callback<Facture?> {
            override fun onResponse(call: Call<Facture?>, response: Response<Facture?>) {
                if (response.errorBody() != null || response.body()!!.id == 0) {
                    AppToast(
                        this@FactureDetailActivity,
                        "Problem de sauvgarde, verifier vos champs",
                        true
                    )
                    return
                }

                AppToast(this@FactureDetailActivity, "Sauvgarde with success", true)
                returnToFactures()
            }

            override fun onFailure(call: Call<Facture?>, t: Throwable) {
                Log.e("Exception: ", t.toString())
            }

        })
    }
    fun deleteFacture() {

        val result = quotesApi.deleteFacture(facture!!.id) ?: return
        result.enqueue(object : Callback<Any?> {
            override fun onResponse(call: Call<Any?>, response: Response<Any?>) {

                if (response.errorBody() != null) {
                    AppToast(
                        this@FactureDetailActivity,
                        "Problem de suppression...",
                        true
                    )
                    return
                }

                AppToast(this@FactureDetailActivity, "Delete with success", true)
                returnToFactures()
            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {
                Log.e("Exception: ", t.toString())
            }

        })
    }

    fun returnToFactures() {
        val intent = Intent(this, FactureActivity::class.java)
        startActivity(intent)
    }

    fun onConfime(view: View?) {
        AppAlertDialog.showYesNoDialog(
            this,
            "Information",
            "Voulez vous vraiment confimer cette instance ?",
            {
                facture!!.confirm = true
                viewBinder.facture = facture
            },
            null
        )
    }
    fun onDelete(view: View?) {
        AppAlertDialog.showYesNoDialog(
            this,
            "Information",
            "Voulez vous vraiment supprimer cette entité ?",
            {
               if(facture!!.id== 0) return@showYesNoDialog
                deleteFacture()
            },
            null
        )
    }


}