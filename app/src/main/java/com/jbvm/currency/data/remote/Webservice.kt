package com.jbvm.currency.data.remote

import com.jbvm.currency.data.model.RatesResponse
import com.jbvm.currency.data.model.SymbolsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "85a201a8879c32013f2328795c13888c"

interface Webservice {
    @GET("symbols")
    fun getSymbols(@Query("access_key") access_key: String = API_KEY): Observable<SymbolsResponse>

    @GET("latest")
    fun getRates(@Query("access_key") access_key: String = API_KEY, @Query("base") base: String): Observable<RatesResponse>
}