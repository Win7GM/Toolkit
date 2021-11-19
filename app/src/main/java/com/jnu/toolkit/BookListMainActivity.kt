package com.jnu.toolkit

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jnu.toolkit.data.Book
import com.jnu.toolkit.data.DataBank


class BookListMainActivity : AppCompatActivity() {
    private lateinit var dataBank: DataBank
    private lateinit var bookList: MutableList<Book>


    private val activityLauncher = registerForActivityResult(StartActivityForResult()) {
        //第二个页面关闭后回到第一个页面的回调方法
            result ->
        run {
            if (result.resultCode == RESULT_OK) {
                val it = result.data ?: return@run
                val position = it.getIntExtra("pos", 0)
                val bookName = it.getStringExtra("name")
                when (it.extras?.get("code")) {
                    1 -> {
                        bookList.add(
                            position,
                            Book(
                                bookName!!,
                                R.drawable.book_no_name,
                            )
                        )

                        findViewById<RecyclerView>(R.id.recycle_view_books).adapter!!.notifyItemInserted(
                            position
                        )
                    }
                    2 -> {
                        bookList[position] = Book(
                            bookName!!,
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

    private lateinit var container: RecyclerView
    private lateinit var adapter: BookRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.book_list_activity_main)
        dataBank= DataBank(this)
        bookList=dataBank.loadData()
        container = findViewById(R.id.recycle_view_books)
        val manager = LinearLayoutManager(this)
        container.layoutManager = manager
        adapter = BookRecyclerViewAdapter(bookList)
        container.adapter = adapter
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener {
            val intent = Intent(this, BookEditorActivity::class.java)
            intent.putExtra("pos", bookList.size)
            intent.putExtra("code", 1)
            activityLauncher.launch(intent)
        }
    }

    override fun onStop() {
        dataBank.saveData()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    // creating context menu by simply calling a function and passing the menu to it
    fun createMenu(menu: Menu) {
        val groupID = 0
        val order = 0
        val itemID = intArrayOf(1, 2, 3)
        for (i in itemID.indices) {
            when (itemID[i]) {
                1 -> menu.add(groupID, itemID[i], order, R.string.menu_insert)
                2 -> menu.add(groupID, itemID[i], order, R.string.menu_edit)
                3 -> menu.add(groupID, itemID[i], order, R.string.menu_delete)
            }
        }
    }

    // triggers when menu item is selected
    override fun onContextItemSelected(item: MenuItem): Boolean {
        // get position of long clicked ViewHolder
        val position: Int = adapter.mPosition
        val intent = Intent(this, BookEditorActivity::class.java)
        when (item.itemId) {
            1 -> {
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
                intent.action = "android.intent.action.EDIT"
                intent.putExtra("pos", position)
                intent.putExtra("code", 2)
                intent.putExtra("name", bookList[position].getTitle())
                activityLauncher.launch(intent)
            }
            3 -> {
                val alertDB: AlertDialog.Builder = AlertDialog.Builder(this)
                alertDB.setPositiveButton(
                    this.resources.getString(R.string.alert_yes)
                ) { _: DialogInterface, _: Int ->
                    bookList.removeAt(position)
                    dataBank.saveData()
                    adapter.notifyItemRemoved(position)

                }
                alertDB.setNegativeButton(
                    this.resources.getString(R.string.alert_no)
                ) { _: DialogInterface, _: Int ->
                }
                alertDB.setMessage(this.resources.getString(R.string.delete_confirmation))
                alertDB.setTitle(this.resources.getString(R.string.hint)).show()
            }
        }
        return super.onContextItemSelected(item)
    }

    // Adapter for fitting data into RecyclerView
    inner class BookRecyclerViewAdapter constructor(private val bookList: MutableList<Book>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        internal var mPosition: Int = -1

        private var mContext: Context? = null


        // ViewHolder for actual showing data of certain book on view
        inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view),
            View.OnCreateContextMenuListener {
            val imageView: ImageView = view.findViewById(R.id.image_view_book_cover)
            val textView: TextView = view.findViewById(R.id.text_view_book_title)

            init {
                // set the function below as callback
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

        // init view of ViewHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (mContext == null) {
                mContext = parent.context
            }
            val view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_holder, parent, false)
            return BookViewHolder(view)
        }

        // init data for views in ViewHolder
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            // 参数是抽象类，要换到实际实现的类
            val holderCasted: BookViewHolder = holder as BookViewHolder
            holderCasted.imageView.setImageResource(bookList[position].getCoverResourceId())
            holderCasted.textView.text = bookList[position].getTitle()

            // set LongClick callback to get layout position of the long clicked ViewHolder (save it in var of adapter)
            holderCasted.itemView.setOnLongClickListener {
                mPosition = holderCasted.layoutPosition
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
