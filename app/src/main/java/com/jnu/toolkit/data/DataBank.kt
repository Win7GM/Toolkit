package com.jnu.toolkit.data

import android.content.Context
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception

class DataBank(private val context: Context) {
    companion object {
        const val DATA_FILE_NAME = "data"
    }

    private lateinit var bookList: MutableList<Book>

    fun loadData(): MutableList<Book> {
        bookList = mutableListOf()
        try {
            val objectInputStream = ObjectInputStream(context.openFileInput(DATA_FILE_NAME))
            bookList = objectInputStream.readObject() as MutableList<Book>
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bookList
    }

    fun saveData() {
        var objectOutputStream: ObjectOutputStream? = null
        try {
            objectOutputStream =
                ObjectOutputStream(context.openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE))
            objectOutputStream.writeObject(bookList)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                objectOutputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}