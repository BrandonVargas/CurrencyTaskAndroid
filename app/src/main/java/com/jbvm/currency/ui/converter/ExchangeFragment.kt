package com.jbvm.currency.ui.converter

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jbvm.currency.ui.common.BaseFragment
import com.jbvm.currency.R
import com.jbvm.currency.data.model.Rate
import com.jbvm.currency.ui.common.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_exchange.*
import javax.inject.Inject

@AndroidEntryPoint
class ExchangeFragment : BaseFragment() {

    companion object {
        fun newInstance(symbol: String): Fragment {
            val fragment = ExchangeFragment()
            val args = Bundle()
            args.putString("BASE", symbol)
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    lateinit var viewModel: MainViewModel
    var base: String? = null
    private val adapter = RateAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exchange, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        base = arguments?.getString("BASE", "EUR");
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showKeyboard()
        etAmount.setOnKeyListener { v, keyCode, event ->
            if (keyCode == EditorInfo.IME_ACTION_GO
                || (event.action == KeyEvent.ACTION_DOWN)
                || (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    hideKeyboard()
                    adapter.factor = etAmount.text.toString().toDouble()
                    adapter.notifyDataSetChanged()
            }
            false
        }
    }

    override fun onStart() {
        super.onStart()
        subscribe(viewModel.getRates(base ?: "")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                showRates(it)
            }, {
                showError(it.localizedMessage ?: "Something when wrong")
            })
        )
    }

    private fun showRates(data: List<Rate>) {
        when {
            data.isNotEmpty() -> {
                adapter.rates = data
                rvExchange.adapter = adapter
                rvExchange.layoutManager = LinearLayoutManager(this.context)
            }
            else -> {
                showError("Something when wrong")
            }
        }
    }

    private fun hideKeyboard(){
        val inputMethodManager: InputMethodManager = context
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(etAmount.windowToken, 0)
    }

    private fun showKeyboard(){
        etAmount.requestFocus()
        val inputMethodManager: InputMethodManager = context
            ?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(etAmount, 0)
    }
}