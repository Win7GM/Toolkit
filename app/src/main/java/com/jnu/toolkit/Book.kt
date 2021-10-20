package com.jnu.toolkit

class Book constructor(private var title:String,private var coverResourceId:Int)
{
    fun getCoverResourceId():Int{
        return this.coverResourceId
    }
    fun getTitle():String{
        return this.title
    }
}
