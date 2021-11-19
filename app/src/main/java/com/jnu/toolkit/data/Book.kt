package com.jnu.toolkit.data

import java.io.Serializable

class Book constructor(private var title: String, private var coverResourceId: Int) : Serializable {
    fun getCoverResourceId(): Int {
        return this.coverResourceId
    }

    fun getTitle(): String {
        return this.title
    }
}
