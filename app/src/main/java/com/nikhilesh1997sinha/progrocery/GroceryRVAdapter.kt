package com.nikhilesh1997sinha.progrocery


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class GroceryRVAdapter(var list: List<GroceryItems>, val groceryItemClickInterface: GroceryItemClickInterface): RecyclerView.Adapter<GroceryRVAdapter.GroceryViewHolder>() {

    var gt: Int = 0
    inner class GroceryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameTV = itemView.findViewById<TextView>(R.id.idTVItemName)
        val quantityTV = itemView.findViewById<TextView>(R.id.idTVQuantity)
        val rateTV = itemView.findViewById<TextView>(R.id.idTVRate)
        val amountTV = itemView.findViewById<TextView>(R.id.idTVToatalAmt)
        val deleteTV = itemView.findViewById<ImageView>(R.id.idTVDelete)

    }

    interface GroceryItemClickInterface{
        fun onItemClick(groceryItems: GroceryItems)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grocery_rv_item, parent, false)
        return GroceryViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        holder.nameTV.text = list.get(position).itemName
        holder.quantityTV.text = "Quantity: "+list.get(position).itemQuantity.toString()
        holder.rateTV.text = "Rate/item: Rs. "+ list.get(position).itemPrice.toString()
        val itemTotal: Int = list.get(position).itemPrice * list.get(position).itemQuantity
        holder.amountTV.text = "Rs. "+ itemTotal.toString()
        gt += itemTotal
        holder.deleteTV.setOnClickListener{
            val itemTotal: Int = list.get(position).itemPrice * list.get(position).itemQuantity
            gt -= itemTotal
            groceryItemClickInterface.onItemClick(list.get(position))
        }
        //Log.d("ADAPTER GRAND TOTAL: ", "#############$gt")
    }

    override fun getItemCount(): Int {
        return list.size
    }
}



























