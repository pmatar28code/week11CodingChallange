
/*
package com.example.week11codingchallange


import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.AsyncTaskLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.week11codingchallange.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        var alphabetList = listOf<String>("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
        var lettersSelectedFromBagList = mutableListOf<String>()
        var foundWord = ""
        var listOfFoundWords = mutableListOf<String>()
        var points = 0
        var globalPoints = 0
        var isEqual = false
        var foundWordFilter =""
        var stringNumberOne = ""
        var stringNumberTwo = ""
        private const val DATABASE_NAME = "Pedro-Database"
    }

    var database:Database ?=null
    var listOfWordsFromDatabase = emptyList<Words>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //var alphabetList = listOf<String>("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z")
        var bagMap = mutableMapOf("A" to 9, "B" to 2, "C" to 2, "D" to 4, "E" to 12, "F" to 2, "G" to 3, "H" to 2, "I" to 9, "J" to 1, "K" to 1, "L" to 4, "M" to 2, "N" to 6, "O" to 8, "P" to 2, "Q" to 1, "R" to 6, "S" to 4, "T" to 6, "U" to 4, "V" to 2, "W" to 2, "X" to 1, "Y" to 2, "Z" to 1)
        //A-9, B-2, C-2, D-4, E-12, F-2, G-3, H-2, I-9, J-1, K-1, L-4, M-2, N-6, O-8, P-2, Q-1, R-6, S-4, T-6, U-4, V-2, W-2, X-1, Y-2, Z-1
        val fileName = "dictionary.txt"

        database = Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()

        getDictionaryList(fileName)
        val dictionaryList = getWordsDatabase()
        //val dictionaryList = getDictionaryList(fileName)
        //var letterFromBag = selectRandomOnMapAndSubstract(getRandomLetter(alphabetList),bagMap)
        addToListOfLettersFromBag(alphabetList, bagMap)
        Toast.makeText(this, "$lettersSelectedFromBagList", Toast.LENGTH_LONG).show()
        binding.bagLetters.text = lettersSelectedFromBagList.toString()
        buildWordsWithLetters(listOfWordsFromDatabase, lettersSelectedFromBagList) {
            binding.recycler.apply {
                adapter = WordsAdapter(it)
                layoutManager = LinearLayoutManager(this@MainActivity)
                // setHasFixedSize(true)
                Toast.makeText(this@MainActivity, "found:${listOfFoundWords}", Toast.LENGTH_LONG).show()

            }

            //binding.test.text = listOfFoundWords[0]
            //Toast.makeText(this, "$listOfFoundWords", Toast.LENGTH_LONG).show()
        }

    }

    fun getWordsDatabase()= AsyncTask.execute{
        runOnUiThread {
            listOfWordsFromDatabase = database?.funDao()?.getAll()!!
        }
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

    fun selectRandomOnMapAndSubstract(letter: String, map: MutableMap<String, Int>): String {
        var newLetter = letter
        var newMap = map
        var key: String? = null
        var value: Int? = null
        for ((k, v) in map) {
            if (k == newLetter) {
                if (map[k] == 0) {
                    // Toast.makeText(this, "key = $key ,value ${map[key]} ", Toast.LENGTH_LONG).show()

                    //var alternativeRandomLetter = getRandomLetter(alphabetList)
                    //selectRandomOnMapAndSubstract(alternativeRandomLetter, map)
                    key = "#"
                    return key!!
                } else {
                    map[k]?.minus(1)?.let { map.put(k, it) }
                    key = k
                    value = v
                }
            }
        }
        // Toast.makeText(this, "key = $key ,value ${map.get(key)} ", Toast.LENGTH_LONG).show()

        return key!!
    }

    fun addToListOfLettersFromBag(alphabetListForRandom: List<String>, map: MutableMap<String, Int>) {
        while (lettersSelectedFromBagList.size < 7) {
            var newLetter = selectRandomOnMapAndSubstract(getRandomLetter(alphabetList),map).toLowerCase()
            if(newLetter !="#") {
                lettersSelectedFromBagList.add(newLetter)
            }
        }
    }

    fun buildWordsWithLetters(dictionaryListF:List<Words>,listOfLettersAvailable:MutableList<String>,CallBack:(List<String>) -> Unit){
        val newDictionary = dictionaryListF.toMutableList()
        val iterator = newDictionary.iterator()
        while(iterator.hasNext()){
            val item = iterator.next()
            if(item.word.length > 7){
                iterator.remove()
            }
        }
        for(word in newDictionary){
            for (letter in listOfLettersAvailable) {
                //Toast.makeText(this,"word:$word ",Toast.LENGTH_SHORT).show()

                if(word.word.contains(letter)) {
                    foundWord += letter
                    Log.e("PEDRO","$letter")
                    //Toast.makeText(this,"word:$word letter: $letter",Toast.LENGTH_SHORT).show()
                } else {
                    foundWord += "#"
                }
            }
            for(letter in foundWord){
                if(!letter.equals("#")){
                    stringNumberOne +=letter
                }
            }
            for(letterW in word.word){
                for(letterF in stringNumberOne){
                    if(letterW == letterF){
                        foundWordFilter +=letterF
                    }else{
                        foundWordFilter += "#"
                    }
                }
            }
            if(!foundWordFilter.contains("#")){
                listOfFoundWords.add(word.word)
                foundWordFilter =""
                foundWord=""
            }else{
                foundWordFilter =""
                foundWord=""
            }

            //listOfFoundWords.add(word)
            //Toast.makeText(this,"$word",Toast.LENGTH_SHORT).show()
            // foundWord = ""
            //}else{
            //    foundWord = ""
            // }
        }
        var newListOfFound = listOfFoundWords
        CallBack(newListOfFound)
    }
}

 */

/*

fun wordsMadeOfLettersAndRemoveLetterIfUsed(lettersSelectedFromBagListFun: MutableList<String>) {

    var wordEqualsWordList = mutableListOf<Words>()
    var wordsBySizeList = mutableListOf<Words>()

    var lettersSelectedFromBagListCopy = mutableListOf<String>()
    for (letter in lettersSelectedFromBagListFun) {
        lettersSelectedFromBagListCopy.add(letter)
    }
    var countersl = 0
    var one = ""
    var two = ""
    var three = ""
    var four = ""
    var five = ""
    var six = ""
    var seven = ""
    var counterLetters = 0
    var lettersUsed = mutableListOf<String>()
    var lettersNotUsed = mutableListOf<String>()



    for (word in listOfFoundWords) {
        while (counterLetters < lettersSelectedFromBagListCopy.size) {
            Log.v("selected start : ", "$lettersSelectedFromBagListCopy")

            if (word.word.contains(lettersSelectedFromBagListCopy[counterLetters].toLowerCase())) {
                listForString.add(lettersSelectedFromBagListCopy[counterLetters].toLowerCase())
                ++counterLetters
            } else {
                //listForString.add(".")
                ++counterLetters
            }
        }


        check = false
        newWord = ""
        wordLenght = word.word.length

        if (listForString.isNotEmpty() && listForString.size > 1) {

            //Log.e("NOOO", "letters to build: $listForString")
            while (check == false && countersl < 80) {

                while (newWord.length < wordLenght) {
                    newWord += listForString.random()
                }



                if (newWord == word.word) {
                    var newList = mutableListOf<String>()
                    wordEqualsWordList.add(word)
                    //if (lettersSelectedFromBagListCopy.isNotEmpty()) {
                    // var iterator = lettersSelectedFromBagListCopy.iterator()
                    // while (iterator.hasNext()) {
                    //    var ite = iterator.next()
                    //     for (letter in newWord) {
                    //         if (ite.toLowerCase() == letter.toString()) {
                    //            iterator.remove()
                    //        }
                    //     }
                    //  }


                    // }


                    //Log.v("letters Left: ", "$lettersSelectedFromBagListCopy")


                    check = true
                    listForString.clear()
                    countersl = 100
                } else {
                    check = false
                    newWord = ""
                    ++countersl
                    //listForString.clear()
                }
            }

        }
        listForString.clear()
        counterLetters = 0
        check = false
        newWord = ""
        wordLenght = 0
        countersl = 0
        //counterString = 0

    }


    var longestWord : Words ?=null
    var listForStringLast = mutableListOf<String>()
    var counterLettersLast = 0

    while(wordEqualsWordList.size > 1 ) {
        for (word in wordEqualsWordList) {
            if(wordEqualsWordList[0].word > word.word){
                longestWord = wordEqualsWordList[0]
            }else{
                longestWord = word
            }
        }

        wordsBySizeList.add(longestWord!!)
        wordEqualsWordList.remove(longestWord)
        if(wordEqualsWordList.size == 1 ){
            wordsBySizeList.add(wordEqualsWordList[0])
        }
    }
    Log.v("lastList:" ,"$wordsBySizeList")


    for (word in wordsBySizeList) {
        mapOfLettersBagTimes.clear()
        mapOfLettersBagTimes = mutableMapOf("a" to 0, "b" to 0, "c" to 0, "d" to 0, "e" to 0, "f" to 0, "g" to 0, "h" to 0, "i" to 0, "j" to 0, "k" to 0, "l" to 0, "m" to 0, "n" to 0, "o" to 0, "p" to 0, "q" to 0, "r" to 0, "s" to 0, "t" to 0, "u" to 0, "v" to 0, "w" to 0, "x" to 0, "y" to 0, "z" to 0)
        for(letter in lettersSelectedFromBagListCopy){
            var temp = mapOfLettersBagTimes[letter.toLowerCase()]!!
            temp += 1
            mapOfLettersBagTimes[letter.toLowerCase()]= temp

        }
        Log.v("map of bag times start:" ,"$mapOfLettersBagTimes")

        while (counterLettersLast < lettersSelectedFromBagListCopy.size) {
            Log.v("selected start : ", "$lettersSelectedFromBagListCopy")

            var graterThanCeroAtMap = mapOfLettersBagTimes[lettersSelectedFromBagListCopy[counterLettersLast].toLowerCase()]!!

            if (word.word.contains(lettersSelectedFromBagListCopy[counterLettersLast].toLowerCase()) && graterThanCeroAtMap > 0) {
                listForStringLast.add(lettersSelectedFromBagListCopy[counterLettersLast].toLowerCase())
                var temp = mapOfLettersBagTimes[lettersSelectedFromBagListCopy[counterLettersLast].toLowerCase()]!!
                temp -=1
                mapOfLettersBagTimes[lettersSelectedFromBagListCopy[counterLettersLast].toLowerCase()] = temp
                ++counterLettersLast
            } else {
                //listForString.add(".")
                ++counterLettersLast
            }
        }
        Log.v("map of bag times end:" ,"$mapOfLettersBagTimes")



        var checkLast = false
        var newWordLast = ""
        var wordLenghtLast = word.word.length
        var counterslLast = 0

        if (listForStringLast.isNotEmpty() && listForStringLast.size > 1) {

            Log.e("NOOO", "letters to build: $listForStringLast")
            while (checkLast == false && counterslLast < 80) {

                while (newWordLast.length < wordLenghtLast) {
                    newWordLast += listForStringLast.random()
                }



                if (newWordLast == word.word) {
                    var newListLast = mutableListOf<String>()
                    wordsFormedWithLettersFromTheBagWithRemove.add(word)
                    if (lettersSelectedFromBagListCopy.isNotEmpty()) {
                        for(letter in newWordLast){
                            if (lettersSelectedFromBagListCopy.contains(letter.toString().toUpperCase())){
                                lettersSelectedFromBagListCopy.remove(letter.toString().toUpperCase())
                            }
                        }


                    }


                    Log.v("letters Left: ", "$lettersSelectedFromBagListCopy")


                    checkLast = true
                    listForStringLast.clear()
                    counterslLast = 100
                    newWordLast = ""
                } else {
                    checkLast = false
                    newWordLast = ""
                    ++counterslLast
                    //listForString.clear()
                }
            }

        }
        listForStringLast.clear()
        counterLettersLast = 0
        checkLast = false
        newWordLast = ""
        wordLenghtLast = 0
        countersl = 0
        //counterString = 0

    }



}

 */