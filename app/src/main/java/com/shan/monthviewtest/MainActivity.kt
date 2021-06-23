package com.shan.monthviewtest

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shan.monthviewtest.databinding.ActivityMainBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private var currentDate = LocalDate.now()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.apply {
            fab.setOnClickListener {
                val localDate = LocalDate.now()
                if (currentDate != localDate) {
                    currentDate = localDate
                    monthView.updateDate(currentDate)
                    tvDateTitle.text = currentDate.format(DateTimeFormatter.BASIC_ISO_DATE)
                }
            }
            ivBackward.setOnClickListener {
                currentDate = currentDate.minusMonths(1)
                monthView.updateDate(currentDate)
                tvDateTitle.text = currentDate.format(DateTimeFormatter.BASIC_ISO_DATE)
            }
            ivForward.setOnClickListener {
                currentDate = currentDate.plusMonths(1)
                monthView.updateDate(currentDate)
                tvDateTitle.text = currentDate.format(DateTimeFormatter.BASIC_ISO_DATE)
            }
            monthViewWrapper.setOnDaySelectListener {
                val selectedDate = binding.monthView.getSelectedDate(it)
                val formattedDate = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
                Toast.makeText(applicationContext, formattedDate, Toast.LENGTH_SHORT).show()
            }
        }
    }
}