package mbolg.tasker.view.tasklist.fragment.active

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import mbolg.tasker.view.tasklist.fragment.SwipeToChangeTypeCallback
import mbolg.tasker.view.tasklist.fragment.TaskViewHolder

abstract class SwipeToDoneCallback : SwipeToChangeTypeCallback(0, ItemTouchHelper.RIGHT){
    override fun showBackground(viewHolder: TaskViewHolder) {
        viewHolder.showDoneBackground()
    }
}