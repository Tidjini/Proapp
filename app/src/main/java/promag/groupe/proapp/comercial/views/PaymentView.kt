package promag.groupe.proapp.comercial.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import promag.groupe.proapp.BaseComponentActivity
import promag.groupe.proapp.PRODUCT_EXTRA
import promag.groupe.proapp.comercial.AppViewModelFactory
import promag.groupe.proapp.comercial.models.Tier
import promag.groupe.proapp.comercial.viewmodels.TierViewModel
import promag.groupe.proapp.global.ui.theme.Independence
import promag.groupe.proapp.global.ui.theme.Independence10
import promag.groupe.proapp.global.ui.theme.ProappTheme
import promag.groupe.proapp.global.ui.theme.Success
import promag.groupe.proapp.views.CustomTextField

class PaymentView : BaseComponentActivity() {

    lateinit var vm: TierViewModel
    lateinit var tier: Tier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        vm =
            ViewModelProvider(this, AppViewModelFactory(mApplication))[TierViewModel::class.java]

        tier = intent.getSerializableExtra(PRODUCT_EXTRA) as Tier

        setContent {
            ProappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Scaffold(

                        content = {
                            PaymentViewContent(vm, this, tier)
                        })
                }
            }
        }
    }

    override fun onBackPressed() {
        val i = Intent(this, TierCollectionView::class.java)
        startActivity(i)
        this.finish()
    }


}


@Composable
fun PaymentViewContent(
    vm: TierViewModel,
    activity: PaymentView,
    tier: Tier
) {


    Column(Modifier.fillMaxSize()) {
        PaymentViewHeader(tier, activity)
        Divider()
        PaymentEditor(vm, tier)

    }

}

//todo use translate between frensh and english

@Composable
fun PaymentViewHeader(tier: Tier, activity: PaymentView) {

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {

        IconButton(onClick = { activity.onBackPressed() }) {
            Icon(
                Icons.Outlined.ArrowBack,
                contentDescription = "Favorite",
                modifier = Modifier.size(28.dp),
                tint = Success

            )
        }

        Text(
            text = "Payment: ${tier.label.uppercase()}",
            style = MaterialTheme.typography.h6,
            fontSize = 18.sp,
            color = Independence,
            modifier = Modifier
                .padding(start = 14.dp)
                .align(Alignment.CenterVertically)
                .weight(1f)
        )

        IconButton(onClick = { /*TODO*/ }, modifier = Modifier.align(Alignment.CenterVertically)) {
            Icon(
                Icons.Outlined.Photo,
                contentDescription = "Menu",
                modifier = Modifier.size(28.dp),
                tint = Success

            )
        }


    }
}


@Composable
fun PaymentEditor(vm: TierViewModel, tier: Tier) {

    var label = rememberSaveable { mutableStateOf("") }
    var montant = rememberSaveable { mutableStateOf("") }
    val out = remember { mutableStateOf(false) }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Switch(

                    checked = out.value, onCheckedChange = { out.value = it })
                Text(
                    text = if (out.value) "Encaissement" else
                        "Payement (Rembourssement)",
                )
            }

            Text(
                text = "Intitulé"
            )
            CustomTextField(
                trailingIcon = null, modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp), fontSize = 14.sp, text = label, placeholderText = "Document"
            )
            Text(
                text = "Montant"
            )
            CustomTextField(
                trailingIcon = null,
                modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp),
                fontSize = 14.sp,
                text = montant,
                placeholderText = "Montant",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { /*todo*/ })
            )

            Button(onClick = {
                vm.createPayment(
                    label = label.value,
                    montant = montant.value,
                    tier = tier,
                    out = out.value
                )
            }) {
                Text(text = "Sauvgarder")
            }
        }


    }
}