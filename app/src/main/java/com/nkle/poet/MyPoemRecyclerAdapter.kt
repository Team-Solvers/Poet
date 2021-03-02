package com.nkle.poet

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class MyPoemRecyclerAdapter() : RecyclerView.Adapter<MyPoemRecyclerAdapter.myPoemViewHolder>() {
   inner class myPoemViewHolder constructor(
             itemView: View
    ) : ViewHolder(itemView) {
        var poemTitle: TextView
        var background: CardView
//        var root: Context
        init {
//            root = itemView.findViewById(R.id.p_parrent).context
            background = itemView.findViewById(R.id.poem_title_card) as CardView
            poemTitle = itemView.findViewById(R.id.my_poem_title) as TextView
//            itemView.setOnClickListener {
//                if(poemTitle.text != "No Current Posts!") {
//
//                }
//            }
        }
    }

    private lateinit var items: MutableList<HashMap<*, *>>
    constructor(parcel: MutableList<HashMap<*, *>>) : this() {
        items = parcel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myPoemViewHolder {
       val v = LayoutInflater.from(parent.context).inflate(R.layout.layout_my_poem_cards,parent, false)
        return myPoemViewHolder(v)
    }


    override fun onBindViewHolder(holder: myPoemViewHolder, position: Int) {
//        holder.poemTitle.text = items["poems"][position][""]
//        for(poem in  items["poems"] as List<Any> ) {
//        var a =
        if(items[position]["posts"] == "No current users") {
            holder.poemTitle.text = "No current Posts!"
        } else {
            holder.poemTitle.text = items[position]["title"].toString()

//           var p =  poem as HashMap<String,*>
            Log.i("____________ohh_____________", items.toString())
//        Toast.makeText(holder.background.context, items[position]["poems"].toString(), Toast.LENGTH_SHORT).show()
            //
//            holder.poemTitle.text = p["asd"] as CharSequence?
//            break
//        }
            if (position % 2 == 0 ) {
                holder.background.setBackgroundColor( Color.parseColor("#635C5C"))
            }
            holder.poemTitle.setOnClickListener {
                var intent:Intent = Intent(holder.poemTitle.context, PostDetail::class.java)
//            intent.putExtra("user_data" , a[position]["user_data"] as HashMap<*,*> )
                val poem = hashMapOf(
                        "author" to items[position]["name"],
                        "title" to items[position]["title"],
                        "id" to items[position]["user_id"],
                        "uuid" to items[position]["uuid"],
                        "content" to items[position]["content"],
                        "img_url" to items[position]["img_url"],
                        "like_count" to items[position]["like_count"],
                        "like_poems" to items[position]["like_count"],
                        "password" to items[position]["password"],
//                "like" to items[position]["likes"] as ArrayList<*>
                )
                intent.putExtra("poem",poem)
                holder.poemTitle.context.startActivity(intent)
            }
        }

    }
    override fun getItemCount(): Int {

        return items.size

    }

}




