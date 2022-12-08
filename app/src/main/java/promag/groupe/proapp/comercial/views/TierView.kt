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


class TierView : BaseComponentActivity() {

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
                            TierViewContent(vm, this, tier)
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
fun TierViewContent(vm: TierViewModel, activity: TierView, tier: Tier) {


    Column(Modifier.fillMaxSize()) {
        TierViewHeader(tier, activity)
        Divider()
        TierEditor(vm, tier)

    }

}

//todo use translate between frensh and english

@Composable
fun TierViewHeader(tier: Tier, activity: TierView) {

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
            text = tier.label,
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

//
//fun getValidatedNumber(text: String): String {
//    // Start by filtering out unwanted characters like commas and multiple decimals
//    val filteredChars = text.filterIndexed { index, c ->
//        c in "0123456789" ||                      // Take all digits
//                (c == '.' && text.indexOf('.') == index)  // Take only the first decimal
//    }
//    // Now we need to remove extra digits from the input
//    return if (filteredChars.contains('.')) {
//        val beforeDecimal = filteredChars.substringBefore('.')
//        val afterDecimal = filteredChars.substringAfter('.')
//        beforeDecimal.take(3) + "." + afterDecimal.take(2)    // If decimal is present, take first 3 digits before decimal and first 2 digits after decimal
//    } else {
//        filteredChars.take(3)                     // If there is no decimal, just take the first 3 digits
//    }
//}

@Composable
fun TierEditor(vm: TierViewModel, tier: Tier) {

    var label = rememberSaveable { mutableStateOf(tier.label) }
    var credit = rememberSaveable { mutableStateOf("${tier.credit}") }
    var debit = rememberSaveable { mutableStateOf("${tier.debit}") }

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
            Text(
                text = "Raison Social"
            )
            CustomTextField(
                trailingIcon = null,
                modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp),
                fontSize = 14.sp,
                text = label,
                placeholderText = "Raison Social"
            )
            Text(
                text = "Débit:"
            )
            CustomTextField(
                trailingIcon = null,
                modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp),
                fontSize = 14.sp,
                text = debit,
                placeholderText = "Débit",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /*todo*/ }
                )
            )
            Text(
                text = "Crédit"
            )
            CustomTextField(
                trailingIcon = null,
                modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp),
                fontSize = 14.sp,
                text = credit,
                placeholderText = "Crédit",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /*todo*/ }
                )
            )
            Button(onClick = {

                vm.save(
                    label = label.value,
                    type = "t",
                    credit = credit.value,
                    debit = debit.value,
                    tier = tier
                )
            }) {
                Text(text = "Sauvgarder")
            }
        }


    }
}