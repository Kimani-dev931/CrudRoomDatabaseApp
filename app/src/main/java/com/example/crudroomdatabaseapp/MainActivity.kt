package com.example.crudroomdatabaseapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.crudroomdatabaseapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appDb: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb = AppDatabase.getDatabase(this)
        binding.btnwriteData.setOnClickListener{
                writeData()
        }
        binding.btnReadData.setOnClickListener{
            readData()
        }
        binding.btnDeleteall.setOnClickListener {
            GlobalScope.launch {
                appDb.studentDao().deleteAll()
            }
        }
        binding.btnUpdate.setOnClickListener {
            updateData()
        }
    }

    private fun updateData() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val rollNo = binding.etRollNo.text.toString()

        if(firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.IO) {
                appDb.studentDao().update(firstName,lastName,rollNo.toInt())
            }
            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()

            Toast.makeText(this@MainActivity, "Successfully updated", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this@MainActivity,"Please Enter Data",Toast.LENGTH_SHORT).show()
        }

    }

    private fun readData() {
        val rollNo = binding.etRollNoRead.text.toString()
        if(rollNo.isNotEmpty()){
            lateinit var student: Student
            GlobalScope.launch{
                student = appDb.studentDao().findByRoll(rollNo.toInt())
                display(student)
            }
        }
    }

    private suspend fun display(student: Student) {
        withContext(Dispatchers.Main){
            binding.tvFirstName.text= student.firstName
            binding.tvLastName.text= student.lastName
            binding.tvRollNo.text = student.rollNo.toString()

        }

    }

    private fun writeData() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val rollNo = binding.etRollNo.text.toString()

        if(firstName.isNotEmpty() && lastName.isNotEmpty() && rollNo.isNotEmpty()) {
            val student = Student(
                null, firstName, lastName, rollNo.toInt()
            )
            GlobalScope.launch {
                appDb.studentDao().insert(student)
            }
            binding.etFirstName.text.clear()
            binding.etLastName.text.clear()
            binding.etRollNo.text.clear()

            Toast.makeText(this@MainActivity, "Successfully written", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this@MainActivity,"Please Enter Data",Toast.LENGTH_SHORT).show()
        }
    }
}