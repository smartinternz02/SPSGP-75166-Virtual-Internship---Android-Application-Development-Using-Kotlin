package com.nikhilesh1997sinha.progrocery

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.alpha
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import pl.droidsonroids.gif.GifImageView
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), GroceryRVAdapter.GroceryItemClickInterface  {

    lateinit var itemsRV: RecyclerView
    lateinit var addFAB: FloatingActionButton
    lateinit var list: List<GroceryItems>
    lateinit var groceryRVAdapter: GroceryRVAdapter
    lateinit var groceryViewModal: GroceryViewModal
    lateinit var grandTotal: TextView
    lateinit var doSomethingGif: GifImageView
    lateinit var shareFAB: FloatingActionButton
    lateinit var shareObject: List<GroceryItems>
    var gt by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grandTotal = findViewById(R.id.idGrandTotal)
        doSomethingGif = findViewById(R.id.doSomething)
        var ShareText = "Name : Quantity x Price = Total"

        itemsRV = findViewById(R.id.idRVItems)
        addFAB = findViewById(R.id.idFABAdd)
        shareFAB = findViewById(R.id.idFABShare)

        list = ArrayList<GroceryItems>()
        groceryRVAdapter = GroceryRVAdapter(list, this)
        itemsRV.layoutManager = LinearLayoutManager(this)
        itemsRV.adapter = groceryRVAdapter
        val groceryRepository = GroceryRepository(GroceryDatabase(this))
        val factory = GroceryViewModalFactory(groceryRepository)
        groceryViewModal = ViewModelProvider(this, factory).get(GroceryViewModal::class.java)
        groceryViewModal.getAllGroceryItems().observe(this, Observer {
            groceryRVAdapter.list = it
            gt = 0
            for(i in it){
                //var tol =0
                //tol = i.itemQuantity*i.itemPrice
                //ShareText = ShareText + "\n${i.itemName} : ${i.itemQuantity} x ${i.itemPrice} = ${tol}"
                gt +=i.itemPrice * i.itemQuantity
            }
            shareObject = it
            //ShareText = ShareText+"\n\nGrand Total : "+gt
            Log.d("Grand total MA", "$gt")
            grandTotal.text = "   Grand total: Rps "+gt+"    "
            if(gt == 0){
                doSomethingGif.visibility = View.VISIBLE
                shareFAB.visibility = View.INVISIBLE
            }
            else{
                doSomethingGif.visibility = View.INVISIBLE
                shareFAB.visibility = View.VISIBLE
            }
            groceryRVAdapter.notifyDataSetChanged()
        })
        addFAB.setOnClickListener {
            openDialog()
        }
        shareFAB.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                for(i in shareObject){
                    var tol =0
                    tol = i.itemQuantity*i.itemPrice
                    ShareText = ShareText + "\n${i.itemName} : ${i.itemQuantity} x ${i.itemPrice} = ${tol}"
                }
                putExtra(Intent.EXTRA_TEXT, ShareText+"\nGrand Total : $gt\n\nThanks for using Pro Grocery 😊😊")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

        fun openDialog() {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.grocery_add_dialog)
            val cancelBtn = dialog.findViewById<Button>(R.id.idBtnCancel)
            val addBtn = dialog.findViewById<Button>(R.id.idBtnAdd)
            val itemEdt = dialog.findViewById<EditText>(R.id.idEdtItemName)
            val itemPriceEdt = dialog.findViewById<EditText>(R.id.idEdtItemPrice)
            val itemQuantityEdt = dialog.findViewById<EditText>(R.id.idEdtItemQuantity)
            cancelBtn.setOnClickListener {
                Log.d("A","Done")
                dialog.dismiss()
            }
            addBtn.setOnClickListener {
                Log.d("B","Done")
                val itemName: String = itemEdt.text.toString()
                val itemPrice: String = itemPriceEdt.text.toString()
                val itemQuantity: String = itemQuantityEdt.text.toString()
                val qty: Int = itemQuantity.toInt()
                val pr: Int = itemPrice.toInt()
                if (itemName.isNotEmpty() && itemPrice.isNotEmpty() && itemQuantity.isNotEmpty()) {
                    val items = GroceryItems(itemName, qty, pr)
                    groceryViewModal.insert(items)
                    Toast.makeText(applicationContext, "Item Inserted..", Toast.LENGTH_SHORT).show()
                    groceryRVAdapter.notifyDataSetChanged()
                    dialog.dismiss()
                } else {
                    Toast.makeText(applicationContext, "Please Enter all the data..", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show()
        }

        override fun onItemClick(groceryItems: GroceryItems) {
            groceryViewModal.delete(groceryItems)
            groceryRVAdapter.notifyDataSetChanged()
            Toast.makeText(applicationContext, "Item Deleted..", Toast.LENGTH_SHORT).show()
        }
}