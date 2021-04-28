package com.jbvm.currency.data.model

data class SymbolsResponse(
    val success: Boolean,
    val symbols: Map<String, String>,
    val error: ResponseError
)