package com.jbvm.currency.ui.converter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jbvm.currency.R
import com.jbvm.currency.data.model.Rate
import java.text.DecimalFormat
import java.util.*


class RateAdapter(var rates: List<Rate>): RecyclerView.Adapter<RateAdapter.ViewHolder>() {

    var factor: Double = 1.0

    inner class ViewHolder(item: View): RecyclerView.ViewHolder(item){
        val symbolTextView: TextView = item.findViewById(R.id.tv_symbol)
        val valueTextView: TextView = item.findViewById(R.id.tv_value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val contactView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_rate,
            parent,
            false
        )

        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: RateAdapter.ViewHolder, position: Int) {
        val rate = rates[position]
        val currency: Currency = Currency.getInstance(rate.symbol)
        var symbol = currency.symbol
        if (symbol.length > 1){
            symbol = ""
        }
        val dec = DecimalFormat("##.##")
        val value = dec.format(rate.value*factor)
        val valueFormated = "$symbol $value"
        holder.symbolTextView.text = rate.symbol
        holder.valueTextView.text = valueFormated
    }

    override fun getItemCount(): Int {
        return rates.size
    }

}