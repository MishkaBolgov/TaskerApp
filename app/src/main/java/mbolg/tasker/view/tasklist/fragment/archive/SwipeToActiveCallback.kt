package mbolg.tasker.view.tasklist.fragment.archive

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import mbolg.tasker.view.tasklist.fragment.SwipeToChangeTypeCallback
import mbolg.tasker.view.tasklist.fragment.TaskViewHolder

abstract class SwipeToActiveCallback: SwipeToChangeTypeCallback(0, ItemTouchHelper.RIGHT) {
    override fun showBackground(viewHolder: TaskViewHolder) {
        viewHolder.showActiveBackground()
    }
}