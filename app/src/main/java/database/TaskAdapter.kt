package database

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartplanner.R
import models.Task
import java.text.SimpleDateFormat
import java.util.Locale

class TaskAdapter(private var tasks: List<Task>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTextView)
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        val date: TextView = itemView.findViewById(R.id.dateTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        // Устанавливаем текст с обработкой переносов
        holder.title.text = task.title
        holder.description.text = task.description

        // Форматирование даты
        val formattedDate = try {
            SimpleDateFormat("dd.MM.yyyy 'в' HH:mm", Locale.getDefault())
                .format(SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(task.datetime)!!)
        } catch (e: Exception) {
            task.datetime
        }

        holder.date.text = formattedDate
    }

    override fun getItemCount() = tasks.size

    // Метод для обновления списка
    fun updateTasks(newTasks: List<Task>) {
        this.tasks = newTasks
        notifyDataSetChanged()
    }
}