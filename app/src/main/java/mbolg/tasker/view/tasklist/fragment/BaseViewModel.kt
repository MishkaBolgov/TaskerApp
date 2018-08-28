package mbolg.tasker.view.tasklist.fragment

import androidx.lifecycle.ViewModel
import mbolg.tasker.data.datamanager.DataManager
import javax.inject.Inject

class BaseViewModel @Inject constructor(dataManager: DataManager): ViewModel()