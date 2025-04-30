package com.example.smartplanner

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asLiveData
import androidx.lifecycle.observe
import com.example.smartplanner.databinding.ActivityMainBinding
import com.example.smartplanner.databinding.ActivityAddTaskBinding
import database.MainDb
import models.Task
import java.util.Calendar

class AddTaskActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddTaskBinding
    private var selectedDateTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = MainDb.getDb(this)

        binding.saveTaskButton.setOnClickListener {
            val task = Task(null,
                binding.titleEditText.text.toString(),
                binding.descriptionEditText.text.toString(),
                selectedDateTime
                ,false)
            Thread{
                db.getDao().inserttask(task)
                startActivity(Intent(this, MainActivity::class.java))
            }.start()
            Toast.makeText(this, "Ваша запись была успешно добавлена", Toast.LENGTH_SHORT).show()
        }

        binding.pickDateButton.setOnClickListener {
            showDateTimePicker()
        }

    }
    private fun showDateTimePicker() {
        // Сначала выбираем дату
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            // После выбора даты выбираем время
            TimePickerDialog(this, { _, hour, minute ->
                // Форматируем выбранную дату и время
                selectedDateTime = String.format(
                    "%04d-%02d-%02d %02d:%02d",
                    year,
                    month + 1, // Месяцы начинаются с 0
                    day,
                    hour,
                    minute
                )
                binding.selectedDateText.text = "Выбрано: $selectedDateTime"
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}