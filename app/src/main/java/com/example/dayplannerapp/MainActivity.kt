package com.example.dayplannerapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

class MainActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var optionButtons: List<Button>
    private lateinit var restartButton: Button

    private val questions = listOf(
        Question(
            text = "Which type of cat has stripes all over the body?",
            options = listOf("Tabby cat", "Persian cat", "Siamese cat", "Ragdoll cat"),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Which cat is known to have a luxurious coat?",
            options = listOf("Tabby cat", "Persian cat", "Siamese cat", "Ragdoll cat"),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which cat is known for having no fur at all?",
            options = listOf("Persian cat", "Maine Coon cat", "British Shorthair cat", "Sphynx cat"),
            correctAnswerIndex = 3
        ),
        Question(
            text = "Which cat is known for being the biggest domesticated cat breed?",
            options = listOf("Persian cat", "Maine Coon cat", "British Shorthair cat", "Sphynx cat"),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which cat breed is famous for its blue eyes and color point coat?",
            options = listOf("Persian cat", "Siamese cat", "British Shorthair cat", "Sphynx cat"),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which cat is known for its quiet and relaxed 'floppy' nature?",
            options = listOf("Ragdoll cat", "Maine Coon cat", "British Shorthair cat", "Sphynx cat"),
            correctAnswerIndex = 0
        ),
        Question(
            text = "Which cat breed originated in the UK and has a dense, plush coat?",
            options = listOf("Persian cat", "Maine Coon cat", "British Shorthair cat", "Sphynx cat"),
            correctAnswerIndex = 2
        ),
        Question(
            text = "Which cat is famous for its hairless appearance?",
            options = listOf("Persian cat", "Maine Coon cat", "British Shorthair cat", "Sphynx cat"),
            correctAnswerIndex = 3
        ),
        Question(
            text = "Which cat breed is the most expensive?",
            options = listOf("Persian cat", "Ashera cat", "Siamese cat", "Sphynx cat"),
            correctAnswerIndex = 1
        ),
        Question(
            text = "Which cat breed is known for its distinct folded ears?",
            options = listOf("Scottish Fold", "Maine Coon cat", "British Shorthair cat", "Sphynx cat"),
            correctAnswerIndex = 0
        )
    )

    private var currentQuestionIndex = 0
    private var score = 0
    private val defaultButtonColor by lazy { Color.parseColor("#6200EE") }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainactivity)

        questionTextView = findViewById(R.id.questionTextView)
        restartButton = findViewById(R.id.restartButton)

        optionButtons = listOf(
            findViewById(R.id.option1Button),
            findViewById(R.id.option2Button),
            findViewById(R.id.option3Button),
            findViewById(R.id.option4Button)
        )

        optionButtons.forEachIndexed { index, button ->
            button.setOnClickListener { checkAnswer(index) }
        }

        restartButton.setOnClickListener { restartQuiz() }

        displayQuestion()
    }

    private fun displayQuestion() {
        val currentQuestion = questions[currentQuestionIndex]

        resetButtonColors()
        enableButtons(true)

        questionTextView.text = currentQuestion.text
        optionButtons.forEachIndexed { index, button ->
            button.text = currentQuestion.options[index]
        }
    }

    private fun checkAnswer(selectedIndex: Int) {
        val currentQuestion = questions[currentQuestionIndex]
        val correctIndex = currentQuestion.correctAnswerIndex

        if (selectedIndex == correctIndex) {
            score++
            setButtonColor(optionButtons[selectedIndex], Color.GREEN)
        } else {
            setButtonColor(optionButtons[selectedIndex], Color.RED)
            setButtonColor(optionButtons[correctIndex], Color.GREEN)
        }

        enableButtons(false)

        lifecycleScope.launch {
            delay(1000)
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                displayQuestion()
            } else {
                showResults()
            }
        }
    }

    private fun setButtonColor(button: Button, color: Int) {
        button.backgroundTintList = ColorStateList.valueOf(color)
    }

    private fun resetButtonColors() {
        optionButtons.forEach { setButtonColor(it, defaultButtonColor) }
    }

    private fun enableButtons(enabled: Boolean) {
        optionButtons.forEach { it.isEnabled = enabled }
    }

    private fun showResults() {
        questionTextView.text = getString(R.string.quiz_over_score, score, questions.size)
        optionButtons.forEach { it.visibility = View.GONE }
        restartButton.visibility = View.VISIBLE
    }

    private fun restartQuiz() {
        currentQuestionIndex = 0
        score = 0
        restartButton.visibility = View.GONE
        optionButtons.forEach { it.visibility = View.VISIBLE }
        displayQuestion()
    }
}
