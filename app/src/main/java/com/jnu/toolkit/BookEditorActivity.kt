package com.jnu.toolkit

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class BookEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_editor)
        val btnOk = findViewById<Button>(R.id.button_ok)
        val btnCancel = findViewById<Button>(R.id.button_cancel)
        val inputBookName = findViewById<EditText>(R.id.editor_bookname)

        if (intent.extras?.get("code") as Int == 2) {
            inputBookName.text=Editable.Factory.getInstance().newEditable(intent.extras?.get("name") as String)
        }

        btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
        btnOk.setOnClickListener {
            val data = Intent().apply {
                putExtra("name", inputBookName.text.toString())
                putExtra("pos", intent.extras?.get("pos") as Int)
            }

            when (intent.extras?.get("code")) {
                1 -> data.putExtra("code", 1)
                2 -> data.putExtra("code", 2)
            }
            setResult(RESULT_OK, data)
            finish()
        }
    }
}