package com.example.week11codingchallange

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var liveFoundWordsTable = MutableLiveData<MutableList<String>>()

    init {
       liveFoundWordsTable.postValue(emptyList<String>().toMutableList())
    }

    fun setLiveFoundWordstable(words:List<String>){
        liveFoundWordsTable.postValue(words.toMutableList())
    }
}