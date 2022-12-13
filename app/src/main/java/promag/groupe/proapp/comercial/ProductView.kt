package promag.groupe.proapp.comercial

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import promag.groupe.proapp.BaseComponentActivity
import promag.groupe.proapp.PRODUCT_EXTRA
import promag.groupe.proapp.global.ui.theme.*
import promag.groupe.proapp.models.commercial.Product
import promag.groupe.proapp.models.commercial.ProductComposition
import promag.groupe.proapp.views.CustomTextField


class ProductView : BaseComponentActivity() {

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
                            MainViewContent(vm, this, product)
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
fun MainViewContent(vm: ProductViewModel, activity: ProductView, product: Product) {


    Column(Modifier.fillMaxSize()) {
        ProductViewHeader(product, activity)
        Divider()
        ProductEditor(vm, product)

    }

}

//todo use translate between frensh and english

@Composable
fun ProductViewHeader(product: Product, activity: ProductView) {

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
            text = product.name,
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


public inline fun <T> Iterable<T>.contains(predicate: (T) -> Boolean): Boolean {
    for (element in this) if (predicate(element)) return true
    return false
}

@Composable
fun ProductEditor(vm: ProductViewModel, product: Product) {

    var name = rememberSaveable { mutableStateOf(product.name) }
    var qteStock = rememberSaveable { mutableStateOf("${product.qteStock}") }
    var valueStock = rememberSaveable { mutableStateOf("${product.value}") }
    var qte = rememberSaveable { mutableStateOf("") }

    val notesList = remember {
        mutableStateListOf<ProductComposition>()
    }


    LaunchedEffect(Unit, block = {
        notesList.addAll(product.compositions)
    })

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
                text = "Produit"
            )
            CustomTextField(
                trailingIcon = null,
                modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp),
                fontSize = 14.sp,
                text = name,
                placeholderText = "Produit"
            )
            Text(
                text = "Qauntité Stock"
            )
            CustomTextField(
                trailingIcon = null,
                modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp),
                fontSize = 14.sp,
                text = qteStock,
                placeholderText = "Qte Stock",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { /*todo*/ }
                )
            )
            Text(
                text = "Valeur Stock"
            )
            CustomTextField(
                trailingIcon = null,
                modifier = Modifier
                    .background(
                        Independence10, RoundedCornerShape(percent = 5)
                    )
                    .height(56.dp),
                fontSize = 14.sp,
                text = valueStock,
                placeholderText = "Valeur Stock",
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
                    name = name.value,
                    qte = qteStock.value,
                    value = valueStock.value,
                    product = product
                )
            }) {
                Text(text = "Sauvgarder")
            }

//            Compositions(vm, product)
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    DropdownDemo(vm)
                }
                CustomTextField(
                    trailingIcon = null,
                    modifier = Modifier
                        .weight(.8f)
                        .background(
                            Independence10, RoundedCornerShape(percent = 5)
                        )
                        .height(56.dp),
                    fontSize = 14.sp,
                    text = qte,
                    placeholderText = "Qte",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { /*todo*/ }
                    )
                )
                Button(onClick = {
                    if (vm.compositionSelected.value == null) return@Button

                    if (notesList.contains { it.composer == vm.compositionSelected.value!!.id })
                        return@Button


                    var qteDouble: Double = 0.0

                    try {
                        qteDouble = qte.value.toDouble()
                    } catch (ex: Exception) {
                        qteDouble = 0.0
                    }

                    notesList.add(
                        ProductComposition(
                            product = product.id,
                            composer = vm.compositionSelected.value!!.id,
                            qte = qteDouble,
                            composeName = vm.compositionSelected.value!!.name
                        )
                    )


                }) {
                    Text(text = "+")
                }

            }

            Divider()
            LazyColumn() {
                items(items = notesList, itemContent = {
                    CompositionItem(item = it)
                    Divider()
                })
            }
        }


    }
}


@Composable
fun DropdownDemo(vm: ProductViewModel) {


    LaunchedEffect(Unit, block = {
        vm.getProducts()
    })
    var expanded by remember { mutableStateOf(false) }


    var selected: Product? by remember { mutableStateOf(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Text(
            text = if (selected != null) selected!!.name else "Product", modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true })
                .background(
                    Independence10, RoundedCornerShape(percent = 5)
                )
                .padding(14.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Independence50
                )
        ) {
            vm.products.forEachIndexed { index, product ->
                DropdownMenuItem(onClick = {
                    selected = product
                    vm.compositionSelected.value = product
                    expanded = false
                }) {
                    Text(text = product.name)
                }
            }
        }
    }
}


@Composable
fun CompositionItem(item: ProductComposition) {


    Row(
        Modifier
            .fillMaxWidth()
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(48.dp)
                .clip(shape = RoundedCornerShape(28.dp))
                .background(Success)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(2.dp)
                    .clip(shape = RoundedCornerShape(28.dp))
                    .background(FlatWhite)

            ) {
                Text(
                    text = item.caption, modifier = Modifier.align(Alignment.Center),
                    fontSize = 11.sp

                )
            }


        }
        Text(
            text = item.composeName,
            style = MaterialTheme.typography.h6,
            fontSize = 14.sp,
            color = Independence
        )
        Text(
            text = "Qte ${item.qte}",
            style = MaterialTheme.typography.caption,
            color = Independence50
        )


    }
}
