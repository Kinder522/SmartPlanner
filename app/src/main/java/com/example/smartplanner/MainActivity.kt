package com.example.smartplanner

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartplanner.databinding.ActivityMainBinding
import database.MainDb
import database.TaskAdapter
import kotlinx.coroutines.launch
import models.Task

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TaskAdapter(
            tasks = listOf(
                Task(1, "Купить молоко", "2 литра", "2023-10-20 18:00", false),
                Task(2, "Сделать уроки", "Математика", "2023-10-21 14:00", true)
            ), //TODO исправить listof на emptylist 02.05.2025
            onTaskClicked = { task ->
                // 2. Что происходит при клике на задачу
                openTaskDetails(task)
            },
            onTaskStatusChanged = { updatedTask ->
                // 3. Что происходит при изменении статуса (чекбокса)
                updateTaskInDatabase(updatedTask)
            }
        )

        setupRecyclerView()
        setupAddButton()
        loadTasksFromDb()
    }

    private fun openTaskDetails(task: Task) {
        val intent = Intent(this, TaskDetailActivity::class.java).apply {
            putExtra("TASK_EXTRA", task) // Корректная передача Parcelable объекта
        }
        startActivity(intent)
    }
    private fun updateTaskInDatabase(task: Task) {
        // Обновляем задачу в базе данных
        lifecycleScope.launch {
            MainDb.getDb(this@MainActivity).getDao().updateTask(task)
        }
    }

    private fun setupRecyclerView() {
        binding.taskRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            setHasFixedSize(true)

            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL
                ).apply {
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.divider)?.let {
                        setDrawable(it)
                    }
                }
            )
        }
    }

    private fun setupAddButton() {
        binding.addTaskButton.setOnClickListener {
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }

    private fun loadTasksFromDb() {
        MainDb.getDb(this).getDao().getalltask().asLiveData().observe(this) { tasks ->
            adapter.updateTasks(tasks)
        }
    }

    private fun updateTaskInDb(task: Task) {
        lifecycleScope.launch {
            try {
                MainDb.getDb(this@MainActivity).getDao().updateTask(task)
            } catch (e: Exception) {
                e.printStackTrace()
                // Можно показать Toast или Snackbar с ошибкой
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadTasksFromDb()
    }
}