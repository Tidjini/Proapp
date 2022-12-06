package promag.groupe.proapp.comercial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import promag.groupe.proapp.BaseApplication


class AppViewModelFactory(val mApplication: BaseApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(mApplication) as T
    }


}

