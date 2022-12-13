package promag.groupe.proapp.comercial

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import promag.groupe.proapp.BaseComponentActivity
import promag.groupe.proapp.MainActivity
import promag.groupe.proapp.global.ui.theme.*
import promag.groupe.proapp.models.User
import promag.groupe.proapp.models.commercial.Product


class ProductCollectionView : BaseComponentActivity() {

    lateinit var vm: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(
            this, AppViewModelFactory(mApplication)
        )[ProductViewModel::class.java]

        setContent {
            ProappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Scaffold(

                        content = {
                            MainContent(vm!!, mApplication.user, this)
                        })
                }
            }
        }
    }

    override fun onBackPressed() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        this.finish()
    }


}


@Composable
fun MainContent(vm: ProductViewModel, user: User, activity: ProductCollectionView) {


    val listState = rememberLazyListState()


    LaunchedEffect(Unit, block = {
        vm.getProducts()
    })



    Column(Modifier.fillMaxSize()) {
        ProductsViewHeader(activity)
        Divider()
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
        ) {
            items(items = vm.products, itemContent = {
                ProductItem(item = it, vm)
                Divider()
            })
        }

    }

}

//todo use translate between frensh and english

@Composable
fun ProductsViewHeader(activity: ProductCollectionView) {

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
            text = "Produits",
            style = MaterialTheme.typography.h6,
            fontSize = 18.sp,
            color = Independence,
            modifier = Modifier
                .padding(start = 14.dp)
                .align(Alignment.CenterVertically)
                .weight(1f)
        )

        Row(
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Outlined.FilterList,
                    contentDescription = "Filter",
                    modifier = Modifier.size(28.dp),
                    tint = Success

                )
            }
            IconButton(onClick = {
                activity.vm.createProductView()
            }) {
                Icon(
                    Icons.Outlined.AddCircle,
                    contentDescription = "Add Products",
                    modifier = Modifier.size(28.dp),
                    tint = Success

                )
            }

        }

    }
}


//Todo add symbole or shortcut
@Composable
fun ProductItem(item: Product, viewModel: ProductViewModel) {


    Row(
        Modifier
            .fillMaxWidth()
            .padding(14.dp)
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
        Column(
            modifier = Modifier
                .padding(start = 14.dp)
                .align(Alignment.CenterVertically)
                .weight(1f)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.h6,
                fontSize = 14.sp,
                color = Independence
            )
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Qte ${item.stockQte}",
                    style = MaterialTheme.typography.caption,
                    color = Independence50
                )
                Text(
                    text = "Valeur ${item.stockValue}",
                    style = MaterialTheme.typography.caption,
                    color = Independence50
                )

            }


        }

        Row() {
            IconButton(onClick = { viewModel.setMovementView(item) }) {
                Icon(
                    Icons.Outlined.MoveToInbox,
                    contentDescription = "Movement",
                    modifier = Modifier.size(28.dp),
                    tint = Success
                )
            }
            IconButton (onClick = { viewModel.editProductView(item) }) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = "Add Contact",
                    modifier = Modifier.size(28.dp),
                    tint = Success
                )
            }

        }


    }
}