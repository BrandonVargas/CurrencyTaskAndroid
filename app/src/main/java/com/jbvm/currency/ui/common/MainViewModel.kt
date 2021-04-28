package com.jbvm.currency.ui.common

import androidx.lifecycle.ViewModel
import com.jbvm.currency.data.entities.Symbol
import com.jbvm.currency.data.repository.CurrencyRepository
import com.jbvm.currency.data.model.Rate
import io.reactivex.Observable
import javax.inject.Inject


class MainViewModel @Inject constructor(private val currencyRepository: CurrencyRepository) : ViewModel() {
    fun getSymbols(): Observable<List<Symbol>> = currencyRepository.getSymbols()
//        .onErrorReturn {
//            //UsersList(emptyList(), "An error occurred", it)
//        }

    fun getRates(base: String): Observable<List<Rate>> = currencyRepository.getRates(base)
}