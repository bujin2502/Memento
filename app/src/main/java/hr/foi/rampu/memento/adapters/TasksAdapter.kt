package hr.foi.rampu.memento.adapters

import android.content.Intent
import android.graphics.Color
import android.view.SurfaceView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import hr.foi.rampu.memento.R
import hr.foi.rampu.memento.database.TasksDatabase
import hr.foi.rampu.memento.entities.Task
import hr.foi.rampu.memento.services.TaskTimerService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TasksAdapter(
    private val tasksList: MutableList<Task>,
    private val onTaskCompleted: ((taskId: Int) -> Unit)? = null
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val taskView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.task_list_item, parent, false)
        return TaskViewHolder(taskView)
    }

    fun addTask(newTask: Task) {
        var newIndexInList = tasksList.indexOfFirst { task ->
            task.dueDate > newTask.dueDate
        }
        if (newIndexInList == -1) {
            newIndexInList = tasksList.size
        }
        tasksList.add(newIndexInList, newTask)
        notifyItemInserted(newIndexInList)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {
        holder.bind(tasksList[position])
    }


    override fun getItemCount() = tasksList.size


    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val sdf: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm", Locale.ENGLISH)
        private val taskName: TextView
        private val taskDueDate: TextView
        private val taskCategoryColor: SurfaceView
        private val taskTimerIcon: ImageView = view.findViewById(R.id.iv_task_timer)
        private var isTimerActive = false


        init {
            view.setOnClickListener {
                if (Date() < tasksList[adapterPosition].dueDate) {
                    val intent = Intent(view.context, TaskTimerService::class.java).apply {
                        putExtra("task_id", tasksList[adapterPosition].id)
                    }

                    isTimerActive = !isTimerActive

                    if (isTimerActive) {
                        taskTimerIcon.visibility = View.VISIBLE
                    } else {
                        intent.putExtra("cancel", true)
                        taskTimerIcon.visibility = View.GONE
                    }

                    view.context.startService(intent)
                } else if (taskTimerIcon.visibility == View.VISIBLE) {
                    taskTimerIcon.visibility = View.GONE
                }
            }
            taskName = view.findViewById(R.id.tv_task_name)
            taskDueDate = view.findViewById(R.id.tv_task_due_date)
            taskCategoryColor = view.findViewById(R.id.sv_task_category_color)
            view.setOnLongClickListener {
                val currentTask = tasksList[adapterPosition]
                val alertDialogBuilder = AlertDialog.Builder(view.context)
                    .setTitle(taskName.text)
                    .setNeutralButton("Delete task") { _, _ ->
                        TasksDatabase.getInstance().getTasksDao().removeTask(currentTask)
                        removeTaskFromList()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel();
                    }
                if (!currentTask.completed) {
                    alertDialogBuilder.setPositiveButton("Mark as completed") { _, _ ->
                        val completedTask = tasksList[adapterPosition]
                        completedTask.completed = true
                        TasksDatabase.getInstance().getTasksDao().insertTask(completedTask)
                        removeTaskFromList()
                        onTaskCompleted?.invoke(completedTask.id)
                    }
                }

                alertDialogBuilder.show()

                return@setOnLongClickListener true
            }
        }

        private fun removeTaskFromList() {
            tasksList.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }

        fun bind(task: Task) {
            taskName.text = task.name
            taskDueDate.text = sdf.format(task.dueDate)
            taskCategoryColor.setBackgroundColor(Color.parseColor(task.category.color))
        }
    }

    fun removeTaskWithId(deletedTaskId: Int) {
        val deletedIndex = tasksList.indexOfFirst { task ->
            task.id == deletedTaskId
        }
        if (deletedIndex != -1) {
            tasksList.removeAt(deletedIndex)
            notifyItemRemoved(deletedIndex)
        }
    }
}