package com.jnu.toolkit

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BookEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_editor)
        val btnOk = findViewById<Button>(R.id.button_ok)
        val btnCancel = findViewById<Button>(R.id.button_cancel)
        val inputBookName = findViewById<EditText>(R.id.editor_bookname)

        when (intent.getIntExtra("code", -1)) {
            -1 -> {
                Toast.makeText(this, "menuItem code invalid", Toast.LENGTH_SHORT).show()
                finish()
            }
            2 -> {
                inputBookName.text =
                    Editable.Factory.getInstance().newEditable(intent.getStringExtra("name"))
            }
        }

        btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        btnOk.setOnClickListener {
            val data = Intent().apply {
                putExtra("name", inputBookName.text.toString())
                // default to top
                putExtra("pos", intent.getIntExtra("pos", 0))
                // default to insert
                putExtra("code", intent.getIntExtra("code", 1))
            }

            setResult(RESULT_OK, data)
            finish()
        }
    }
}