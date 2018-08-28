package mbolg.tasker.view.tasklist.fragment.active

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import mbolg.tasker.view.tasklist.fragment.SwipeToChangeTypeCallback
import mbolg.tasker.view.tasklist.fragment.TaskViewHolder

abstract class SwipeToArchiveCallback: SwipeToChangeTypeCallback(0, ItemTouchHelper.LEFT) {
    override fun showBackground(viewHolder: TaskViewHolder) {
        viewHolder.showArchiveBackground()
    }
}