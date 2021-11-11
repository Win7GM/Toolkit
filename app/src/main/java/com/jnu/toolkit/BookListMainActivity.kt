package com.jnu.toolkit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
//import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception


class BookListMainActivity : AppCompatActivity() {
    private var bookList: MutableList<Book> = mutableListOf(
        Book("软件项目管理案例教程（第4版）", R.drawable.book_2),
        Book("创新工程实践", R.drawable.book_no_name),
        Book("信息安全数学基础（第2版）", R.drawable.book_1)
    )
    private val activityLauncher = registerForActivityResult(StartActivityForResult()) {
        //第二个页面关闭后回到第一个页面的回调方法
            result ->
        run {
            if (result.resultCode == RESULT_OK) {
                val it = result.data
                if (it != null) {
                    val position = it.extras?.get("pos") as Int
                    val bookName = it.extras?.get("name") as String
                    if (it.extras?.get("code") == 1) {
                        bookList.add(
                            position,
                            Book(
                                bookName,
                                R.drawable.book_no_name,
                            )
                        )

                        findViewById<RecyclerView>(R.id.recycle_view_books).adapter!!.notifyItemInserted(
                            position
                        )
                    } else if (it.extras?.get("code") == 2) {
                        bookList[position] = Book(
                            bookName,
                            R.drawable.book_no_name,
                        )

                        findViewById<RecyclerView>(R.id.recycle_view_books).adapter!!.notifyItemChanged(
                            position
                        )
                    }
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_list_activity_main)
        val container = findViewById<RecyclerView>(R.id.recycle_view_books)
        val manager = LinearLayoutManager(this)
        container.layoutManager = manager
        container.adapter = BookRecyclerViewAdapter(bookList)
    }

    fun createMenu(menu: Menu) {
        val groupID = 0
        val order = 0
        val itemID = intArrayOf(1, 2, 3)
        for (i in itemID.indices) {
            when (itemID[i]) {
                1 -> menu.add(groupID, itemID[i], order, "新建")
                2 -> menu.add(groupID, itemID[i], order, "编辑")
                3 -> menu.add(groupID, itemID[i], order, "删除")
            }
        }
    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position: Int = try {
            (findViewById<RecyclerView>(R.id.recycle_view_books).adapter as BookRecyclerViewAdapter).mPosition
        } catch (e: Exception) {
            Log.d("error", e.localizedMessage, e)
            return super.onContextItemSelected(item)
        }
        when (item.itemId) {
            1 -> {
                val intent = Intent(this, BookEditorActivity::class.java)
                intent.action = "android.intent.action.EDIT"
                intent.putExtra("pos", position)
                intent.putExtra("code", 1)
                activityLauncher.launch(intent)
//                val dialogueView: View = LayoutInflater.from(this@BookListMainActivity)
//                    .inflate(R.layout.dialogue_input_item, null)
//                val alertDialogBuilder = AlertDialog.Builder(this@BookListMainActivity)
//                alertDialogBuilder.setView(dialogueView)
//
//                alertDialogBuilder.setPositiveButton(
//                    "确定"
//                ) { dialogInterface, i ->
//                    val editName = dialogueView.findViewById<EditText>(R.id.edit_text_name)
//                    bookList.add(
//                        position,
//                        Book(
//                            editName.text.toString(),
//                            R.drawable.book_no_name,
//                        )
//                    )
//
//                    findViewById<RecyclerView>(R.id.recycle_view_books).adapter!!.notifyItemInserted(
//                        position
//                    )
//                }
//                alertDialogBuilder.setCancelable(false).setNegativeButton(
//                    "取消"
//                ) { dialogInterface, i-> }
//                alertDialogBuilder.create().show()
            }
            2 -> {
                val intent = Intent(this, BookEditorActivity::class.java)
                intent.action = "android.intent.action.EDIT"
                intent.putExtra("pos", position)
                intent.putExtra("code", 2)
                intent.putExtra("name", bookList[position].getTitle())
                activityLauncher.launch(intent)
            }
            3 -> {
                bookList.removeAt(position)
                findViewById<RecyclerView>(R.id.recycle_view_books).adapter!!.notifyItemRemoved(
                    position
                )
            }
        }
        return super.onContextItemSelected(item)
    }

    inner class BookRecyclerViewAdapter constructor(private val bookList: MutableList<Book>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        internal var mPosition: Int = -1

        private var mContext: Context? = null

        inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view),
            View.OnCreateContextMenuListener {
            val imageView: ImageView
            val textView: TextView

            init {
                imageView = view.findViewById(R.id.image_view_book_cover)
                textView = view.findViewById(R.id.text_view_book_title)

                view.setOnCreateContextMenuListener(this)
            }

            override fun onCreateContextMenu(
                menu: ContextMenu?,
                v: View?,
                menuInfo: ContextMenu.ContextMenuInfo?
            ) {
                val mSelectBook: Book = bookList[mPosition]
                menu!!.setHeaderTitle(mSelectBook.getTitle())
                (mContext as BookListMainActivity).createMenu(menu)
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (mContext == null) {
                mContext = parent.context
            }
            val view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_holder, parent, false)
            return BookViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            // 参数是抽象类，要换到实际实现的类
            val holder: BookViewHolder = holder as BookViewHolder
            holder.imageView.setImageResource(bookList[position].getCoverResourceId())
            holder.textView.text = bookList[position].getTitle()
            holder.itemView.setOnLongClickListener {
                mPosition = holder.layoutPosition
                false
            }
        }

        override fun getItemCount(): Int {
            return bookList.size
        }

        override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
            holder.itemView.setOnLongClickListener(null)
            super.onViewRecycled(holder)
        }
    }

    fun getListBooks(): List<Book> {
        return bookList
    }
}