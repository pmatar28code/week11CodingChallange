package com.example.week11codingchallange

import android.database.CharArrayBuffer
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.week11codingchallange.databinding.ActivityMainBinding
import org.w3c.dom.Text
import java.util.*

//var alphabetList = listOf<String>("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
//A-9, B-2, C-2, D-4, E-12, F-2, G-3, H-2, I-9, J-1, K-1, L-4, M-2, N-6, O-8, P-2, Q-1, R-6, S-4, T-6, U-4, V-2, W-2, X-1, Y-2, Z-1
class MainActivity : AppCompatActivity() {

    companion object {
        var alphabetList = listOf<String>("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
        var bagMap = mutableMapOf("A" to 9, "B" to 2, "C" to 2, "D" to 4, "E" to 12, "F" to 2, "G" to 3, "H" to 2, "I" to 9, "J" to 1, "K" to 1, "L" to 4, "M" to 2, "N" to 6, "O" to 8, "P" to 2, "Q" to 1, "R" to 6, "S" to 4, "T" to 6, "U" to 4, "V" to 2, "W" to 2, "X" to 1, "Y" to 2, "Z" to 1)
        var listOfFoundWords = mutableListOf<String>()
        var lettersSelectedFromBagList = mutableListOf<String>()


        private const val DATABASE_NAME = "Pedro-Database"
    }

    var database:Database ?=null
    var listOfWordsFromDatabase = emptyList<Words>()
    val wordsAdapter = WordsAdapter()
   lateinit var mainViewModel: MainViewModel
   // var foundWords= mutableListOf<Words>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        var list =mainViewModel.liveFoundWordsTable
        list.observe(this, androidx.lifecycle.Observer {
            if(it.isEmpty()){
                binding.bagLetters.text = ""
            }else{
                binding.bagLetters.text = it[0].word
            }

        })
        val fileName = "dictionary.txt"

        database = Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()

        getWordsDatabase()
        getDictionaryList(fileName)

        binding.recycler.apply {
            adapter = wordsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }



    }

    override fun onResume() {
        super.onResume()
        AsyncTask.execute {
            val words = database?.funDao()?.getAll()
            Repository.repoListWords = database?.funDao()?.findWord("abiogenic")!!
            runOnUiThread {
                //Toast.makeText(this,"from search: ${Repository().repoListWords?.get(0)?.word}",Toast.LENGTH_LONG).show()
                mainViewModel.setLiveFoundWordstable(Repository.repoListWords)
                wordsAdapter.submitList(words)
            }
        }
    }


    fun getWordsDatabase()= AsyncTask.execute{
        Repository.repoListWords = database?.funDao()?.getAll()?.toMutableList()!!
    }

    fun getDictionaryList(fileName: String) = AsyncTask.execute {

        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        inputString.split("\n").apply {

            for(word in this){
               val newWord = Words(word = word)
                database?.funDao()?.add(newWord)
            }
        }
    }

    fun getRandomLetter(list: List<String>): String {
        var newList = list
        return newList.random()
    }
}