package com.jbvm.currency.ui.symbols

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jbvm.currency.R
import com.jbvm.currency.data.entities.Symbol


class SymbolsAdapter(var symbols: List<Symbol>): RecyclerView.Adapter<SymbolsAdapter.ViewHolder>() {

    var onItemClick: ((Symbol) -> Unit)? = null

    inner class ViewHolder(item: View): RecyclerView.ViewHolder(item){
        val symbolTextView: TextView = item.findViewById(R.id.tv_symbol)
        val nameTextView: TextView = item.findViewById(R.id.tv_name)

        init {
            item.setOnClickListener {
                onItemClick?.invoke(symbols[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val contactView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_symbol, parent, false)

        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: SymbolsAdapter.ViewHolder, position: Int) {
        val symbol = symbols[position]
        holder.symbolTextView.text = symbol.abbreviation
        holder.nameTextView.text = symbol.name
    }

    override fun getItemCount(): Int {
        return symbols.size
    }

}