package database

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.smartplanner.R
import models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(
    private var tasks: List<Task>,
    private val onTaskClicked: (Task) -> Unit,
    private val onTaskStatusChanged: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardView)
        val title: TextView = itemView.findViewById(R.id.titleTextView)
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        val date: TextView = itemView.findViewById(R.id.dateTextView)
        val doneStatus: TextView = itemView.findViewById(R.id.donetextView)
        val doneCheckbox: CheckBox = itemView.findViewById(R.id.doneCheckbox)

        fun bind(task: Task) {
            title.text = task.title
            description.text = task.description
            date.text = formatDate(task.datetime)
            doneStatus.text = if (task.isDone) "Выполнено" else "Не выполнено"
            doneCheckbox.isChecked = task.isDone

            // Обработка клика по чекбоксу
            doneCheckbox.setOnCheckedChangeListener { _, isChecked ->
                onTaskStatusChanged(task.copy(isDone = isChecked))
            }

            // Обработка клика по всей карточке
            itemView.setOnClickListener {
                onTaskClicked(task)
            }
            // Устанавливаем цвет карточки в зависимости от статуса
            val bgColor = if (task.isDone) {
                ContextCompat.getColor(itemView.context, R.color.task_done2)
            } else {
                ContextCompat.getColor(itemView.context, R.color.white)
            }
            cardView.setCardBackgroundColor(bgColor)


            val textColor = if (task.isDone) {
                ContextCompat.getColor(itemView.context, R.color.task_done)
            } else {
                ContextCompat.getColor(itemView.context, R.color.black)
            }
            title.setTextColor(textColor)
            description.setTextColor(textColor)

            // Добавляем зачеркивание для выполненных задач
            if (task.isDone) {
                title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                description.paintFlags = description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                doneStatus.paintFlags = description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                date.paintFlags = description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                title.paintFlags = title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                description.paintFlags = description.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                doneStatus.paintFlags = description.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                date.paintFlags = description.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        private fun formatDate(dateString: String): String {
            return try {
                SimpleDateFormat("dd.MM.yyyy 'в' HH:mm", Locale.getDefault()).format(
                    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dateString)!!
                )
            } catch (e: Exception) {
                dateString
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        this.tasks = newTasks
        notifyDataSetChanged()
    }
}