package promag.groupe.proapp.comercial

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
import promag.groupe.proapp.global.ui.theme.Independence
import promag.groupe.proapp.global.ui.theme.Independence10
import promag.groupe.proapp.global.ui.theme.ProappTheme
import promag.groupe.proapp.global.ui.theme.Success
import promag.groupe.proapp.models.commercial.Product
import promag.groupe.proapp.views.CustomTextField


class StockMovementView : BaseComponentActivity() {

    lateinit var vm: ProductViewModel
    lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        vm =
            ViewModelProvider(this, AppViewModelFactory(mApplication))[ProductViewModel::class.java]

        product = intent.getSerializableExtra(PRODUCT_EXTRA) as Product

        setContent {
            ProappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Scaffold(

                        content = {
                            StockViewContent(vm, this, product)
                        })
                }
            }
        }
    }

    override fun onBackPressed() {
        val i = Intent(this, ProductCollectionView::class.java)
        startActivity(i)
        this.finish()
    }


}


@Composable
fun StockViewContent(
    vm: ProductViewModel,
    activity: StockMovementView,
    product: Product
) {


    Column(Modifier.fillMaxSize()) {
        StockViewHeader(product, activity)
        Divider()
        StockEditor(vm, product)

    }

}

//todo use translate between frensh and english

@Composable
fun StockViewHeader(product: Product, activity: StockMovementView) {

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
            text = "Mouvement Stock: ${product.name.uppercase()}",
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
fun StockEditor(vm: ProductViewModel, product: Product) {

    var document = rememberSaveable { mutableStateOf("") }
    var qte = rememberSaveable { mutableStateOf("") }
    var prixUnite = rememberSaveable { mutableStateOf("") }
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
                    text = if (out.value) "Une Sortie depuis le stock (Livraison)" else
                        "Entrée Vers le stock (Reception)",
                )
            }

            Text(
                text = "Document"
            )
            CustomTextField(
                trailingIcon = null, modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp), fontSize = 14.sp, text = document, placeholderText = "Document"
            )
            Text(
                text = "Qauntité"
            )
            CustomTextField(
                trailingIcon = null,
                modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp),
                fontSize = 14.sp,
                text = qte,
                placeholderText = "Qte (Movement)",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { /*todo*/ })
            )
            Text(
                text = "Prix Unitaire"
            )
            CustomTextField(
                trailingIcon = null,
                modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp),
                fontSize = 14.sp,
                text = prixUnite,
                placeholderText = "Prix Unite",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { /*todo*/ })
            )
            Button(onClick = {
                vm.createStockMovement(
                    document = document.value,
                    qte = qte.value,
                    prixUnite = prixUnite.value,
                    product = product,
                    out = out.value
                )
            }) {
                Text(text = "Sauvgarder")
            }
        }


    }
}


