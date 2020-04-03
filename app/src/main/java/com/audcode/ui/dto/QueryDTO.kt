package com.audcode.ui.dto

data class QueryDTO(val query:String? =null, val skip: Long = 0, val limit: Int = 20)