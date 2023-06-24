package com.example.comptest

import com.example.comptest.model.DataItem

object DummyData {

    fun generateDummyUserResponse(): List<DataItem> {
        val items: MutableList<DataItem> = arrayListOf()
        for (i in 0..100) {
            val quote = DataItem(
                "rayden $i",
                i,
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/56/Valentino_Rossi_2017.jpg/800px-Valentino_Rossi_2017.jpg",
                "szk $i",
                "rayden$i@gmail.com"
            )
            items.add(quote)
        }
        return items
    }
}