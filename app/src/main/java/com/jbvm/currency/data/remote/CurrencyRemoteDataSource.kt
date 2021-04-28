package com.jbvm.currency.data.remote

import javax.inject.Inject

class CurrencyRemoteDataSource @Inject constructor(
    private val webservice: Webservice
    ) {
    fun getSymbols() = webservice.getSymbols()
    fun getRates(base: String) = webservice.getRates(base=base)

}