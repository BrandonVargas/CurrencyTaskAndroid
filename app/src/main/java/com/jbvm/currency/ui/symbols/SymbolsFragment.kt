package com.jbvm.currency.ui.symbols

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jbvm.currency.ui.common.BaseFragment
import com.jbvm.currency.R
import com.jbvm.currency.data.entities.Symbol
import com.jbvm.currency.ui.common.MainViewModel
import com.jbvm.currency.ui.converter.ExchangeFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.symbols_fragment.*
import javax.inject.Inject

@AndroidEntryPoint
class SymbolsFragment : BaseFragment() {

    companion object {
        fun newInstance() = SymbolsFragment()
    }

    @Inject lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.symbols_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        subscribe(viewModel.getSymbols()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showSymbols(it)
            }, {
                showError(it.localizedMessage ?: "Something went wrong")
            }))
    }

    private fun showSymbols(data: List<Symbol>) {
        when {
            data.isNotEmpty() -> {
                val adapter = SymbolsAdapter(data)
                rv_symbols.adapter = adapter
                rv_symbols.layoutManager = LinearLayoutManager(this.context)
                adapter.onItemClick = { symbol -> loadFragment(ExchangeFragment.newInstance(symbol.abbreviation))}
            }
            else -> {
                showError("Something went wrong")
            }
        }
    }
}