package com.example.projecte_simon_jordibonet

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // Variables para las vistas
    var redButton: ImageView? = null
    var greenButton: ImageView? = null
    var yellowButton: ImageView? = null
    var blueButton: ImageView? = null
    var startButton: Button? = null
    var scoreText: TextView? = null

    // Variables para la lógica del juego
    val colorSequence = mutableListOf<Int>()
    val playerSequence = mutableListOf<Int>()
    var currentRound = 0
    var playerTurn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar las vistas
        redButton = findViewById(R.id.red_button)
        greenButton = findViewById(R.id.green_button)
        yellowButton = findViewById(R.id.yellow_button)
        blueButton = findViewById(R.id.blue_button)
        startButton = findViewById(R.id.btn_start)
        scoreText = findViewById(R.id.tv_instructions)

        // Inicialmente desactivar los botones de colores
        disableButtons()

        // Click listener para el botón "Comenzar"
        startButton?.setOnClickListener { startGame() }

        // Establecer click listeners para los botones de colores
        setButtonListeners()
    }

    private fun setButtonListeners() {
        redButton?.setOnClickListener { onColorClicked(0) }
        greenButton?.setOnClickListener { onColorClicked(1) }
        yellowButton?.setOnClickListener { onColorClicked(2) }
        blueButton?.setOnClickListener { onColorClicked(3) }
    }

    private fun startGame() {
        // Reiniciar variables del juego
        colorSequence.clear()
        playerSequence.clear()
        currentRound = 0
        playerTurn = false
        scoreText?.text = "Ronda: 1"
        startButton?.visibility = View.GONE

        // Activar los botones de colores
        enableButtons()

        // Iniciar la primera ronda después de un pequeño retardo
        Handler(Looper.getMainLooper()).postDelayed({ nextRound() }, 1000)
    }

    private fun nextRound() {
        currentRound++
        scoreText?.text = "Ronda: $currentRound"
        playerSequence.clear()

        // Añadir un nuevo color a la secuencia
        colorSequence.add(Random.nextInt(4))

        // Mostrar la secuencia de colores al jugador
        showColorSequence()
    }

    private fun showColorSequence() {
        playerTurn = false
        var delay = 0L

        // Mostrar la secuencia con un retardo entre cada color
        colorSequence.forEach { color ->
            Handler(Looper.getMainLooper()).postDelayed({ highlightColor(color) }, delay)
            delay += 1000
        }

        // Una vez mostrada la secuencia, permitir al jugador interactuar
        Handler(Looper.getMainLooper()).postDelayed({ playerTurn = true }, delay)
    }

    private fun highlightColor(color: Int) {
        val button = getButtonByColor(color)
        val highlightedColor = getHighlightedColor(color)
        val normalColor = getNormalColor(color)

        // Cambiar el color al resaltado
        button?.setColorFilter(ContextCompat.getColor(this, highlightedColor))

        // Apagar la iluminación después de 500 ms
        Handler(Looper.getMainLooper()).postDelayed({
            button?.setColorFilter(ContextCompat.getColor(this, normalColor))
        }, 500)
    }

    private fun onColorClicked(color: Int) {
        if (!playerTurn) return  // Si no es el turno del jugador, ignorar el clic

        playerSequence.add(color)
        highlightPlayerColor(color)

        // Verificar si el jugador ha seguido correctamente la secuencia
        if (playerSequence.last() != colorSequence[playerSequence.size - 1]) {
            endGame()  // Si el jugador se equivoca, termina el juego
            return
        }

        // Si el jugador ha completado la secuencia correctamente
        if (playerSequence.size == colorSequence.size) {
            playerTurn = false
            Handler(Looper.getMainLooper()).postDelayed({ nextRound() }, 1000)
        }
    }

    private fun highlightPlayerColor(color: Int) {
        val button = getButtonByColor(color)
        val highlightedColor = getHighlightedColor(color)
        val normalColor = getNormalColor(color)

        // Cambiar el color al resaltado
        button?.setColorFilter(ContextCompat.getColor(this, highlightedColor))

        // Apagar la iluminación después de 100 ms
        Handler(Looper.getMainLooper()).postDelayed({
            button?.setColorFilter(ContextCompat.getColor(this, normalColor))
        }, 100)
    }

    private fun endGame() {
        disableButtons()
        scoreText?.text = "Juego terminado. Puntuación: $currentRound"
        startButton?.visibility = View.VISIBLE
    }

    private fun enableButtons() {
        setButtonClickable(true)
    }

    private fun disableButtons() {
        setButtonClickable(false)
    }

    private fun setButtonClickable(clickable: Boolean) {
        redButton?.isClickable = clickable
        greenButton?.isClickable = clickable
        yellowButton?.isClickable = clickable
        blueButton?.isClickable = clickable
    }

    private fun getButtonByColor(color: Int): ImageView? {
        return when (color) {
            0 -> redButton
            1 -> greenButton
            2 -> yellowButton
            3 -> blueButton
            else -> null
        }
    }

    // Obtener el color resaltado desde los recursos
    private fun getHighlightedColor(color: Int): Int {
        return when (color) {
            0 -> R.color.red
            1 -> R.color.green
            2 -> R.color.yellow
            3 -> R.color.blue
            else -> R.color.white
        }
    }

    // Obtener el color normal (pastel) desde los recursos
    private fun getNormalColor(color: Int): Int {
        return when (color) {
            0 -> R.color.red_light
            1 -> R.color.green_light
            2 -> R.color.yellow_light
            3 -> R.color.blue_light
            else -> R.color.white
        }
    }
}
