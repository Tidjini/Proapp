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
import promag.groupe.proapp.comercial.models.Payment
import promag.groupe.proapp.comercial.viewmodels.TierViewModel
import promag.groupe.proapp.global.ui.theme.*
import promag.groupe.proapp.models.User


class PaymentCollectionView : BaseComponentActivity() {

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
                            PaymentCollectionContent(vm!!, mApplication.user, this)
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
fun PaymentCollectionContent(vm: TierViewModel, user: User, activity: PaymentCollectionView) {


    val listState = rememberLazyListState()


    LaunchedEffect(Unit, block = {
        vm.getPayments()
    })



    Column(Modifier.fillMaxSize()) {
        PaymentsViewHeader(activity)
        Divider()
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
        ) {
            items(items = vm.payments, itemContent = {
                PaymentItem(item = it, vm)
                Divider()
            })
        }

    }

}

//todo use translate between frensh and english

@Composable
fun PaymentsViewHeader(activity: PaymentCollectionView) {

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
            text = "Payments",
            style = MaterialTheme.typography.h6,
            fontSize = 18.sp,
            color = Independence,
            modifier = Modifier
                .padding(start = 14.dp)
                .align(Alignment.CenterVertically)
                .weight(1f)
        )

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                Icons.Outlined.FilterList,
                contentDescription = "Filter",
                modifier = Modifier.size(28.dp),
                tint = Success

            )
        }

    }
}


//Todo add symbole or shortcut
@Composable
fun PaymentItem(item: Payment, viewModel: TierViewModel) {


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
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "${item.tierItem!!.label}",
                    style = MaterialTheme.typography.h6,
                    fontSize = 14.sp,
                    color = Independence
                )
                Text(
                    text = " ${if (item.out) "Paiement" else "Encaissement"}",
                    style = MaterialTheme.typography.caption,
                    color = Independence50
                )


            }
            Text(
                text = "Montant: ${item.montant}",
                style = MaterialTheme.typography.h6,
                fontSize = 14.sp,
                color = Independence
            )


        }

        Row() {
            IconButton(onClick = { /*todo*/ }) {
                Icon(
                    Icons.Outlined.MoveToInbox,
                    contentDescription = "Movement",
                    modifier = Modifier.size(28.dp),
                    tint = Success
                )
            }

        }


    }
}