package com.example.quizapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import org.intellij.lang.annotations.Language

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var quizModeList: MutableList<QuizModel>
    lateinit var adapter :QuizListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        quizModeList = mutableListOf()
        getDataFromFirebase()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
    private fun setupRecyclerView() {
        binding.progressBar.visibility= View.GONE
        adapter=QuizListAdapter(quizModeList)
        binding.recyclerView.layoutManager=LinearLayoutManager(this)
        binding.recyclerView.adapter=adapter
    }

    private fun getDataFromFirebase() {
        binding.progressBar.visibility= View.VISIBLE
        FirebaseDatabase.getInstance().reference
            .get()
            .addOnSuccessListener { dataSnapshots->
                if(dataSnapshots.exists()){
                    for (snapshot in dataSnapshots.children){
                        val  quizModel =snapshot.getValue(QuizModel::class.java)
                        if (quizModel !=null){
                            quizModeList.add(quizModel)
                        }
                    }
                }
                setupRecyclerView()
            }



    }
}