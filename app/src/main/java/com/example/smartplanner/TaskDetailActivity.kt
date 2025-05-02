package com.example.smartplanner

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.smartplanner.databinding.ActivityTaskDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import database.MainDb
import kotlinx.coroutines.launch
import models.Task
import java.text.SimpleDateFormat
import java.util.Locale
class TaskDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskDetailBinding
    private lateinit var currentTask: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentTask = intent.getParcelableExtra<Task>("TASK_EXTRA") ?: run {
            finish()
            return
        }

        setupToolbar()
        setupViews()
        setupCheckboxListener()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupViews() {
        binding.titleTextView.text = currentTask.title
        binding.descriptionTextView.text = currentTask.description
        binding.dateTextView.text = formatDate(currentTask.datetime)
        binding.doneCheckbox.isChecked = currentTask.isDone
        updateStatusUI(currentTask.isDone)
    }

    private fun setupCheckboxListener() {
        binding.doneCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                showCompletionDialog()
            } else {
                updateTaskStatus(isChecked)
            }
        }
    }

    private fun updateTaskStatus(isDone: Boolean) {
        // Обновляем текущую задачу
        currentTask = currentTask.copy(isDone = isDone)

        // Обновляем UI
        updateStatusUI(isDone)

        // Сохраняем в БД
        lifecycleScope.launch {
            MainDb.getDb(this@TaskDetailActivity).getDao().updateTask(currentTask)
        }
    }

    private fun updateStatusUI(isDone: Boolean) {
        // Обновляем текст и цвет статуса
        binding.statusTextView.text = if (isDone) "Выполнено" else "Не выполнено"
        val statusColor = if (isDone) R.color.green else R.color.red
        binding.statusTextView.setTextColor(ContextCompat.getColor(this, statusColor))

        // Анимация изменения статуса
        binding.statusTextView.animate()
            .alpha(0f)
            .setDuration(150)
            .withEndAction {
                binding.statusTextView.animate().alpha(1f).setDuration(150).start()
            }
            .start()
    }

    private fun showCompletionDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Подтверждение")
            .setMessage("Отметить задачу как выполненную?")
            .setPositiveButton("Да") { _, _ ->
                binding.doneCheckbox.isChecked = true // Важно обновить чекбокс
                updateTaskStatus(true)
            }
            .setNegativeButton("Нет") { _, _ ->
                binding.doneCheckbox.isChecked = false
            }
            .show()
    }

    private fun formatDate(dateString: String): String {
        return try {
            SimpleDateFormat("dd MMMM yyyy 'в' HH:mm", Locale.getDefault()).format(
                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).parse(dateString)!!
            )
        } catch (e: Exception) {
            dateString
        }
    }
}