package com.example.week11codingchallange

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    var liveFoundWordsTable = MutableLiveData<MutableList<Words>>()

    init {
       liveFoundWordsTable.postValue(emptyList<Words>().toMutableList())
    }

    fun setLiveFoundWordstable(words:List<Words>){
        liveFoundWordsTable.postValue(words.toMutableList())
    }
}