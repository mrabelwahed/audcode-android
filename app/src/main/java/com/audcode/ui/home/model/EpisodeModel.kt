package com.audcode.ui.home.model

data class EpisodeModel (val name:String ,
                         val imgUrl:String,
                         val author:String ,
                         val rate:Float ,
                         val ratingCount:Int =0,
                         val mainStack:String)