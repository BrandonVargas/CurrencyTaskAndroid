package com.jbvm.currency.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.jbvm.currency.data.entities.Symbol
import com.jbvm.currency.data.local.SymbolDao
import com.jbvm.currency.data.model.RatesResponse
import com.jbvm.currency.data.model.SymbolsResponse
import com.jbvm.currency.data.remote.CurrencyRemoteDataSource
import com.jbvm.currency.data.model.Rate
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val remoteDataSource: CurrencyRemoteDataSource,
    private val localDataSource: SymbolDao,
    private val sharedPreferences: SharedPreferences
) {

    private val ONE_MINUTE_IN_MILLIS = 60000

    fun getRates(base: String): Observable<List<Rate>> {
        return remoteDataSource.getRates(base)
            .map {  ratesResponseToRateList(it) }
            .doOnNext {
                Log.e("DB_SYMBOLS_NEXT", it.size.toString())
            }
            .doOnError {  Log.e("DB_SYMBOLS_ERROOORRR", it.message.toString()) }
    }

    fun getSymbols(): Observable<List<Symbol>> {
        val now = Date(System.currentTimeMillis())
        val updated = Date(sharedPreferences.getLong("UPDATED", 0))
        Log.e("SHARED", updated.toString())
        return if (updated.time == 0L){
            getSymbolsFromAPI()
        } else {
            val expiration = Date(updated.time + (30 * ONE_MINUTE_IN_MILLIS))
            if (now.time > expiration.time) {
                getSymbolsFromAPI()
            } else {
                getSymbolsFromDb()
            }
        }

    }


    private fun getSymbolsFromDb(): Observable<List<Symbol>> {
        return localDataSource.loadAll().toObservable()
            .doOnNext {
                Log.e("DB_SYMBOLS", "Dispatching ${it.size} symbols from DB...")
            }
    }

    private fun getSymbolsFromAPI(): Observable<List<Symbol>> {
        return remoteDataSource.getSymbols()
            .map {  symbolsResponseMapToEntity(it) }
            .doOnNext {
                storeSymbolsOnDB(it)
            }
            .doOnError {  Log.e("CurrencyRepository", it.message.toString()) }
    }

    private fun storeSymbolsOnDB(symbols: List<Symbol>) {
        Observable.fromCallable { localDataSource.saveAll(symbols) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe {
                Log.e("DB_SYMBOLS", "Inserted ${symbols.size} symbols from API in DB...")
                val date = Date(System.currentTimeMillis())
                sharedPreferences.edit().putLong("UPDATED", date.time).apply()
            }
    }

    private fun symbolsResponseMapToEntity(response: SymbolsResponse): List<Symbol> {
        val symbolList = arrayListOf<Symbol>()
        Log.e("DB_SYMBOLS", response.toString())
        if (response.success && response.symbols.isNotEmpty()) {
            response.symbols.entries.forEach {
                symbolList.add(Symbol(abbreviation = it.key, name = it.value))
            }
        }
        return symbolList
    }

    private fun ratesResponseToRateList(response: RatesResponse): List<Rate> {
        val rateList = arrayListOf<Rate>()
        Log.e("DB_RATES", response.toString())
        if (response.success && response.rates.isNotEmpty()) {
            response.rates.entries.forEach {
                rateList.add(Rate(symbol = it.key, value = it.value))
            }
        }
        Log.e("DB_RATES", rateList.toString())
        return rateList
    }
}