package com.jnu.toolkit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BookListMainActivity : AppCompatActivity(){
    private var bookList:List<Book> = listOf(
        Book("软件项目管理案例教程（第4版）", R.drawable.book_2),
        Book("创新工程实践", R.drawable.book_no_name),
        Book("信息安全数学基础（第2版）", R.drawable.book_1)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_list_activity_main)
        val container = findViewById<RecyclerView>(R.id.recycle_view_books)
        val manager = LinearLayoutManager(this)
        container.layoutManager = manager
        container.adapter=BookRecyclerViewAdapter(bookList)
    }

    class BookRecyclerViewAdapter constructor(private val bookList: List<Book>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imageView: ImageView
            val textView: TextView

            init {
                // Define click listener for the ViewHolder's View.
                imageView = view.findViewById(R.id.image_view_book_cover)
                textView = view.findViewById(R.id.text_view_book_title)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_holder,parent,false)
            return BookViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            // 参数是抽象类，要换到实际实现的类
            val holder:BookViewHolder = holder as BookViewHolder
            holder.imageView.setImageResource(bookList[position].getCoverResourceId())
            holder.textView.text = bookList[position].getTitle()
        }

        override fun getItemCount(): Int {
            return bookList.size
        }

    }

    fun getListBooks():List<Book>{
        return bookList
    }
}