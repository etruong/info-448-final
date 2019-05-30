package edu.uw.ischool.elisat15.boba_stop

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RandomizerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_randomizer)

        val button = findViewById<Button>(R.id.button)
        val myIntent = Intent(this, MainActivity::class.java)
        button.setOnClickListener {
            startActivity(myIntent)
        }
    }
}
