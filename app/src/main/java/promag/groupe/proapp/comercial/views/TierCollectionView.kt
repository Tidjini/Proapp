package promag.groupe.proapp.comercial.views

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
import promag.groupe.proapp.comercial.AppViewModelFactory
import promag.groupe.proapp.comercial.models.Tier
import promag.groupe.proapp.comercial.models.toPrice
import promag.groupe.proapp.comercial.viewmodels.TierViewModel
import promag.groupe.proapp.global.ui.theme.*
import promag.groupe.proapp.models.User


class TierCollectionView : BaseComponentActivity() {

    lateinit var vm: TierViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProvider(
            this, AppViewModelFactory(mApplication)
        )[TierViewModel::class.java]

        setContent {
            ProappTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {
                    Scaffold(

                        content = {
                            TierCollectionContent(vm!!, mApplication.user, this)
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
fun TierCollectionContent(vm: TierViewModel, user: User, activity: TierCollectionView) {


    val listState = rememberLazyListState()


    LaunchedEffect(Unit, block = {
        vm.getTiers()
    })



    Column(Modifier.fillMaxSize()) {
        TiersViewHeader(activity)
        Divider()
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
        ) {
            items(items = vm.tiers, itemContent = {
                TierItem(item = it, vm)
                Divider()
            })
        }

    }

}

//todo use translate between frensh and english

@Composable
fun TiersViewHeader(activity: TierCollectionView) {

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
            text = "Tiers",
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
                activity.vm.createTierView()
            }) {
                Icon(
                    Icons.Outlined.AddCircle,
                    contentDescription = "Add Tiers",
                    modifier = Modifier.size(28.dp),
                    tint = Success

                )
            }

        }

    }
}


//Todo add symbole or shortcut
@Composable
fun TierItem(item: Tier, viewModel: TierViewModel) {


    Row(
        Modifier
            .fillMaxWidth()
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically

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
                    text = item.caption, modifier = Modifier.align(Alignment.Center)

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
                text = item.label,
                style = MaterialTheme.typography.h6,
                fontSize = 14.sp,
                color = Independence
            )
            Text(
                text = "D??bit: ${toPrice(item.debit)}",
                style = MaterialTheme.typography.caption,
                color = Independence50
            )
            Text(
                text = "Cr??dit: ${toPrice(item.credit)}",
                style = MaterialTheme.typography.caption,
                color = Independence50
            )

            Text(
                text = "Solde: ${toPrice(item.balance)}",
                style = MaterialTheme.typography.h6,
                fontSize = 14.sp,
                color = Independence
            )


        }

        Row() {
            IconButton(onClick = { viewModel.setPaymentView(item) }) {
                Icon(
                    Icons.Outlined.Payment,
                    contentDescription = "Movement",
                    modifier = Modifier.size(28.dp),
                    tint = Success
                )
            }
            IconButton(onClick = { viewModel.editTierView(item) }) {
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