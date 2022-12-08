package promag.groupe.proapp.comercial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import promag.groupe.proapp.BaseApplication
import promag.groupe.proapp.comercial.viewmodels.TierViewModel
import promag.groupe.proapp.tasks.viewmodels.TaskViewModel


class AppViewModelFactory(val mApplication: BaseApplication) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ProductViewModel::class.java -> ProductViewModel(mApplication) as T
            TierViewModel::class.java -> TierViewModel(mApplication) as T
            TaskViewModel::class.java -> TaskViewModel(mApplication) as T
            else -> ProductViewModel(mApplication) as T
        }

    }


}

