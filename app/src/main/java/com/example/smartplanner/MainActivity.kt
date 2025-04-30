package com.example.smartplanner

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartplanner.databinding.ActivityMainBinding
import database.MainDb
import database.TaskAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskAdapter(emptyList()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Инициализация ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка RecyclerView
        setupRecyclerView()

        // Настройка кнопки добавления
        setupAddButton()

        // Загрузка данных из БД
        loadTasksFromDb()
    }

    private fun setupRecyclerView() {

        binding.taskRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            setHasFixedSize(true)

            // Добавляем разделитель между элементами
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

    override fun onResume() {
        super.onResume()
        // Обновляем данные при возвращении на экран
        loadTasksFromDb()
    }
}