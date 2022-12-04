package promag.groupe.proapp.adapters

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter


@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean?) {
    var isGone = isGone
    if (isGone == null) isGone = false
    view.visibility = if (isGone) View.GONE else View.VISIBLE
}

@BindingAdapter("isGoneGroup")
fun bindIsGoneGroup(view: ViewGroup, isGone: Boolean?) {
    var isGone = isGone
    if (isGone == null) isGone = false
    view.visibility = if (isGone) View.GONE else View.VISIBLE
}