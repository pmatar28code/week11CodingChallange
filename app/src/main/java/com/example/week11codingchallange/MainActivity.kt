package com.example.week11codingchallange

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.week11codingchallange.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

//var alphabetList = listOf<String>("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
//A-9, B-2, C-2, D-4, E-12, F-2, G-3, H-2, I-9, J-1, K-1, L-4, M-2, N-6, O-8, P-2, Q-1, R-6, S-4, T-6, U-4, V-2, W-2, X-1, Y-2, Z-1
class MainActivity : AppCompatActivity() {

    companion object {
        var alphabetList = listOf<String>("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
        var bagMap = mutableMapOf("A" to 9, "B" to 2, "C" to 2, "D" to 4, "E" to 12, "F" to 2, "G" to 3, "H" to 2, "I" to 9, "J" to 1, "K" to 1, "L" to 4, "M" to 2, "N" to 6, "O" to 8, "P" to 2, "Q" to 1, "R" to 6, "S" to 4, "T" to 6, "U" to 4, "V" to 2, "W" to 2, "X" to 1, "Y" to 2, "Z" to 1)
        var listOfFoundWords = mutableListOf<Words>()

        var wordsFormedWithLettersFromTheBagWithRemove = mutableListOf<Words>()
        private const val DATABASE_NAME = "Pedro-Database"
    }

    var mapOfWordTimes = mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0, "e" to 0, "f" to 0, "g" to 0, "h" to 0, "i" to 0, "j" to 0, "k" to 0, "l" to 0, "m" to 0, "n" to 0, "o" to 0, "p" to 0, "q" to 0, "r" to 0, "s" to 0, "t" to 0, "u" to 0, "v" to 0, "w" to 0, "x" to 0, "y" to 0, "z" to 0)
    var mapOfLettersBagTimes = mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0, "e" to 0, "f" to 0, "g" to 0, "h" to 0, "i" to 0, "j" to 0, "k" to 0, "l" to 0, "m" to 0, "n" to 0, "o" to 0, "p" to 0, "q" to 0, "r" to 0, "s" to 0, "t" to 0, "u" to 0, "v" to 0, "w" to 0, "x" to 0, "y" to 0, "z" to 0)

    var wordLenght = 0
    var check = false
    var newWord = ""
    var listForString = mutableListOf<String>()

    var lettersSelectedFromBagList = mutableListOf<String>()
    var database: Database? = null
    var listOfWordsFromDatabase = emptyList<Words>()
    val wordsAdapter = WordsAdapter()
    lateinit var mainViewModel: MainViewModel
    var words = emptyList<Words>()
    var stringLettersBag = ""
    var zero = ""
    var one = ""
    var two = ""
    var three = ""
    var four = ""
    var five = ""
    var six = ""
    var seven = ""
    var eight = ""
    var nine = ""
    var ten = ""
    var eleven = ""
    var twelve = ""
    var thirteen = ""
    var fourteen = ""
    var fifteen = ""
    var sixteen = ""
    var seventeen = ""
    var eighteen = ""
    var nineteen = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        var list = mainViewModel.liveFoundWordsTable
        list.observe(this, androidx.lifecycle.Observer {
            if (it.isEmpty()) {
                binding.bagLetters.text = ""
            } else {
                binding.bagLetters.text = it.toString()
            }

        })
        val fileName = "dictionary.txt"

        applicationContext.deleteDatabase(DATABASE_NAME)


        database = Room.databaseBuilder(
                applicationContext,
                Database::class.java,
                DATABASE_NAME
        ).fallbackToDestructiveMigration()
                .build()

        getRandomLetterAndSubstract()
        lettersFromBagToString()
        //getWordsDatabase()
        getDictionaryList(fileName)

        binding.recycler.apply {
            adapter = wordsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        binding.floatingActionButton.setOnClickListener {
            AsyncTask.execute {
                applicationContext.deleteDatabase(DATABASE_NAME)
                database?.clearAllTables()
            }
            restart()
            mainViewModel.setLiveFoundWordstable(lettersSelectedFromBagList)
            recreate()

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        lettersSelectedFromBagList.clear()
        listOfFoundWords.clear()
        //applicationContext.deleteDatabase(DATABASE_NAME)
       // database?.clearAllTables()
    }

    override fun onResume() {
        super.onResume()
        AsyncTask.execute {
            listOfFoundWords.clear()
            //words = database?.funDao()?.getAll()?: emptyList()
            listOfFoundWords = database?.funDao()?.findWordExclude(zero, one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen, seventeen, eighteen, nineteen)!!.sortedBy { it.word.length }.toMutableList()
            wordsMadeOfLettersAndRemoveLetterIfUsed(lettersSelectedFromBagList)

            runOnUiThread {
                //Toast.makeText(this,"from search: ${Repository().repoListWords?.get(0)?.word}",Toast.LENGTH_LONG).show()
                mainViewModel.setLiveFoundWordstable(lettersSelectedFromBagList)
                wordsAdapter.submitList(wordsFormedWithLettersFromTheBagWithRemove)
            }
        }
    }


    fun getWordsDatabase() = AsyncTask.execute {
        Repository.repoListWords = database?.funDao()?.getAll()?.toMutableList()!!
    }

    fun getDictionaryList(fileName: String) = AsyncTask.execute {

        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        inputString.split("\n").apply {

            for (word in this) {
                val newWord = Words(word = word)
                database?.funDao()?.add(newWord)
            }
        }
    }

    fun getRandomLetter(list: List<String>): String {
        var newList = list
        return newList.random()
    }

    fun getRandomLetterAndSubstract() {
        var counter = 0
        while (counter < 7) {
            var randomLetter = getRandomLetter(alphabetList)
            for ((k, v) in bagMap) {
                if (k == randomLetter) {
                    if (bagMap[k] == 0) {
                    } else {
                        lettersSelectedFromBagList.add(k)
                        bagMap[k]?.minus(1).let {
                            if (it != null) {
                                bagMap[k] = it
                                ++counter
                            }
                        }
                    }
                }
            }
        }
        counter = 0
        Toast.makeText(this, "list of letters selected $lettersSelectedFromBagList", Toast.LENGTH_LONG).show()
    }

    fun lettersFromBagToString() {
        var newAlph = alphabetList.toMutableList()
        for (letter in lettersSelectedFromBagList) {
            if (newAlph.contains(letter)) {
                newAlph.remove(letter)
            }
        }
        for (letter in newAlph) {
            if (zero == "") {
                zero = "%" + letter.toLowerCase() + "%"
            } else if (one == "") {
                one = "%" + letter.toLowerCase() + "%"
            } else if (two == "") {
                two = "%" + letter.toLowerCase() + "%"
            } else if (three == "") {
                three = "%" + letter.toLowerCase() + "%"
            } else if (four == "") {
                four = "%" + letter.toLowerCase() + "%"
            } else if (five == "") {
                five = "%" + letter.toLowerCase() + "%"
            } else if (six == "") {
                six = "%" + letter.toLowerCase() + "%"
            } else if (seven == "") {
                seven = "%" + letter.toLowerCase() + "%"
            } else if (eight == "") {
                eight = "%" + letter.toLowerCase() + "%"
            } else if (nine == "") {
                nine = "%" + letter.toLowerCase() + "%"
            } else if (ten == "") {
                ten = "%" + letter.toLowerCase() + "%"
            } else if (eleven == "") {
                eleven = "%" + letter.toLowerCase() + "%"
            } else if (twelve == "") {
                twelve = "%" + letter.toLowerCase() + "%"
            } else if (thirteen == "") {
                thirteen = "%" + letter.toLowerCase() + "%"
            } else if (fourteen == "") {
                fourteen = "%" + letter.toLowerCase() + "%"
            } else if (fifteen == "") {
                fifteen = "%" + letter.toLowerCase() + "%"
            } else if (sixteen == "") {
                sixteen = "%" + letter.toLowerCase() + "%"
            } else if (seventeen == "") {
                seventeen = "%" + letter.toLowerCase() + "%"
            } else if (eighteen == "") {
                eighteen = "%" + letter.toLowerCase() + "%"
            } else if (nineteen == "") {
                nineteen = "%" + letter.toLowerCase() + "%"
            }
        }
    }

    fun wordsMadeOfLettersAndRemoveLetterIfUsed(lettersSelectedFromBagListFun: MutableList<String>) {
        var lastWordNotFound : Words ?= null
        var wordEqualsWordList = mutableListOf<Words>()

        var lettersSelectedFromBagListCopy = mutableListOf<String>()
        for (letter in lettersSelectedFromBagListFun) {
            lettersSelectedFromBagListCopy.add(letter)
        }
        var countersl = 0

        var counterLetters = 0

        for(letter in lettersSelectedFromBagListCopy){
            var temp = mapOfLettersBagTimes[letter.toLowerCase()]!!
            temp+=1
            mapOfLettersBagTimes[letter.toLowerCase()] = temp
        }

        Log.v("mapBagTimes first:","$mapOfLettersBagTimes")

            listOfFoundWords.sortedBy { it.word.length }

            for (word in listOfFoundWords.indices.reversed()) {




                for((k,v) in mapOfLettersBagTimes){
                    if (listOfFoundWords[word].word.contains(k) && v > 0) {
                        listForString.add(k)
                        var temp = v

                        if(v > 0) {
                            temp -= 1
                            mapOfLettersBagTimes[k] = temp
                            Log.d("REMOViNG from Map:","$mapOfLettersBagTimes")

                        }else {
                            mapOfLettersBagTimes[k] = 0
                        }


                    }
                }


                Log.v("mapBagTimes end:","$mapOfLettersBagTimes")



                check = false
                newWord = ""
                wordLenght = listOfFoundWords[word].word.length
                Log.v("list for string:","$listForString")


                if (listForString.isNotEmpty() && listForString.size > 1) {
                    var checkS= false

                    //Log.e("NOOO", "letters to build: $listForString")
                    while (check == false && countersl < 150) {

                        while (newWord.length < listForString.size) {
                            newWord += listForString.random()
                        }



                        if (newWord == listOfFoundWords[word].word) {

                            wordsFormedWithLettersFromTheBagWithRemove.add(listOfFoundWords[word])
                            Log.d("Word added to equal:", "$newWord")

                            check = true
                            listForString.clear()
                            countersl = 200
                            checkS = true
                        } else {

                            check = false
                            newWord = ""
                            ++countersl
                        }




                    }

                    if( !check){
                        for(letter in listForString){
                            for((k,v) in mapOfLettersBagTimes){
                                if(letter.toString() == k){
                                    var temp = v
                                    temp +=1
                                    mapOfLettersBagTimes[k] = temp
                                    Log.v("ADDED NOT USED","${k}${mapOfLettersBagTimes[k]}")
                                }
                            }
                        }
                    }
                    Log.v("MAP AFTER ADDED","$mapOfLettersBagTimes")




                }


                listForString.clear()
                counterLetters = 0
                check = false
                newWord = ""
                wordLenght = 0
                countersl = 0
                //counterString = 0



                Log.v("WE START AGAIN WORD:","NEXT")


            }

    }

    fun restart(){
        listForString.clear()
        check = false
        newWord = ""
        wordLenght = 0

        mapOfLettersBagTimes.clear()

        lettersSelectedFromBagList.clear()
        wordsFormedWithLettersFromTheBagWithRemove.clear()
        listOfFoundWords.clear()
        var stringLettersBag = ""
        var zero = ""
        var one = ""
        var two = ""
        var three = ""
        var four = ""
        var five = ""
        var six = ""
        var seven = ""
        var eight = ""
        var nine = ""
        var ten = ""
        var eleven = ""
        var twelve = ""
        var thirteen = ""
        var fourteen = ""
        var fifteen = ""
        var sixteen = ""
        var seventeen = ""
        var eighteen = ""
        var nineteen = ""




    }

}

