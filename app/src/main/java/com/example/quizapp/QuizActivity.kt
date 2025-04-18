package com.example.quizapp

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizapp.databinding.ActivityMainBinding
import com.example.quizapp.databinding.ActivityQuizBinding
import com.example.quizapp.databinding.ScoreDialogBinding

class QuizActivity : AppCompatActivity() ,View.OnClickListener{

    companion object{
        var questionModelList : List<QuestionModel> = listOf()
        var time : String =""
    }

    lateinit var binding: ActivityQuizBinding
    var currentQuestionIndex = 0;
    var selectedAnswer = ""
    var score =0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding =ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            btn0.setOnClickListener(this@QuizActivity)
            btn1.setOnClickListener(this@QuizActivity)
            btn2.setOnClickListener(this@QuizActivity)
            btn3.setOnClickListener(this@QuizActivity)
            nextBtn.setOnClickListener(this@QuizActivity)

        }
        loadQuestions()
        startTimer()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private  fun startTimer(){
        val totalTimeInMillis = time.toInt() *60 *1000L
        object : CountDownTimer(totalTimeInMillis,1000L){
            override fun onTick(millisUnitFinished :Long){
                val seconds =millisUnitFinished /1000
                val minutes =seconds/60
                val remainingSeconds =seconds % 60
                binding.timerIndicatorTextview.text=String.format("%02d :%02d" , minutes , remainingSeconds)
            }

            override fun onFinish() {
                //finish the Quiz
            }
        }.start()
    }
    private fun loadQuestions(){
        selectedAnswer =  ""
        if (currentQuestionIndex== questionModelList.size){
            finishQuiz()
            return
        }
        binding.apply {
            questionIndicatorTextview.text = "Question ${currentQuestionIndex+1}/ ${questionModelList.size} "
            questionProgressIndicator.progress=
                (currentQuestionIndex.toFloat() / questionModelList.size.toFloat() * 100).toInt()
            questionTextview.text = questionModelList[currentQuestionIndex].question
            btn0.text = questionModelList[currentQuestionIndex].options[0]
            btn1.text = questionModelList[currentQuestionIndex].options[1]
            btn2.text = questionModelList[currentQuestionIndex].options[2]
            btn3.text = questionModelList[currentQuestionIndex].options[3]
        }

    }

    override fun onClick(view: View) {

        binding.apply {
            btn0.setBackgroundColor(getColor(R.color.grey))
            btn1.setBackgroundColor(getColor(R.color.grey))
            btn2.setBackgroundColor(getColor(R.color.grey))
            btn3.setBackgroundColor(getColor(R.color.grey))
        }
        val  clickBtn = view as Button
        if(clickBtn.id ==R.id.next_btn){
            //next button is clicked
            if(selectedAnswer == questionModelList[currentQuestionIndex].correct){
                
                score++
                Log.i("Score of quiz",  score.toString())

            }
            currentQuestionIndex++
            loadQuestions()
        }else{
            //options are clicked
            selectedAnswer =clickBtn.text.toString()
            clickBtn.setBackgroundColor(getColor(R.color.orange))


        }

    }

    private fun finishQuiz(){
        val totalQuestions = questionModelList.size
        val percentage = ((score.toFloat() / totalQuestions.toFloat())*100).toInt()
        val dialogBinding =ScoreDialogBinding.inflate(layoutInflater)
        dialogBinding.apply {
            scoreProgressIndicator.progress =percentage
            scoreProgressText.text = "$percentage %"
            if(percentage >60){
                scoreTitle.text ="Congrtas! You have passed"
                scoreTitle.setTextColor(Color.BLUE)
            }else{
                scoreTitle.text ="Oops! You have Failed"
                scoreTitle.setTextColor(Color.RED)
            }
            scoreSubtitle.text ="$score out of $totalQuestions are correct "
            finishBtn.setOnClickListener{
                finish()
            }
        }

        AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .show()

    }
}