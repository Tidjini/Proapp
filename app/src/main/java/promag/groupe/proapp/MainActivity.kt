package promag.groupe.proapp

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import promag.groupe.proapp.comercial.ProductCollectionView
import promag.groupe.proapp.comercial.views.TierCollectionView
import promag.groupe.proapp.global.messenger.discussion.DiscussionActivity
import promag.groupe.proapp.infrabitume.Encaissement
import promag.groupe.proapp.infrabitume.FactureActivity
import promag.groupe.proapp.infrabitume.LivraisonActivity
import promag.groupe.proapp.utils.CacheHelper.userToken
import promag.groupe.proapp.views.AppAlertDialog
import promag.groupe.proapp.views.AppToast


class MainActivity : BaseActivity() {


    private lateinit var mNotificationManager: NotificationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    }


    fun onInfraStockClicked(view: View) {

        val intent = Intent(this, LivraisonActivity::class.java)
        startActivity(intent)

    }

    fun onInfraEncaissementClicked(view: View) {
        val intent = Intent(this, Encaissement::class.java)
        startActivity(intent)
    }

    fun onMessageClicked(view: View) {
        val intent = Intent(this, DiscussionActivity::class.java)
        startActivity(intent)
    }

    fun onFactureClicked(view: View) {
        val intent = Intent(this, FactureActivity::class.java)
        startActivity(intent)
    }

    fun onOthersClicked(view: View) {

        AppToast(this, "This Application is not available now. retry later ;)", true)

    }

    fun onStockClicked(view: View) {
        val intent = Intent(this, ProductCollectionView::class.java)
        startActivity(intent)
    }
    fun onTiersClicked(view: View) {
        val intent = Intent(this, TierCollectionView::class.java)
        startActivity(intent)
    }



    fun onLogout(view: View) {
        AppAlertDialog.showYesNoDialog(
            this,
            "Information",
            "Voulez vous vraiment quitter l'application ?",
            {
                mApplication.userPreferences.userToken = null
                finishAffinity()
                mApplication.onTerminate()
            },
            null
        )
    }

    override fun onBackPressed() {
        this.finishAffinity()
    }

// TODO
//    - DONE create new one / copie to new project reanme it to procom
//    - DONE bitume simple application
//    - DONE Stock Reception
//    - DONE Product - Qte - Value - Each Reception, Date
//    - Bon Livraison
//    - Tier - Produit - Qte - Prix - Value, date
//    - Deduire Stock formule
//    - Les Encaissements
//    - Tier - Montant - Type - Date
//    - Chauffeur Tasks also:
//    - Task -> chauffeur, DONE / CANCEL /
//    - Instance de Facture:
//    - Simple, NO UI designing
//    - Notifications  for Task
//    - Notifications  for stock changes admins

}