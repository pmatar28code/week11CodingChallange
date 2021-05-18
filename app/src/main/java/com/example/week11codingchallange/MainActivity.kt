package com.example.week11codingchallange

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week11codingchallange.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    companion object{
        var alphabetList = listOf<String>("A" ,"B" ,"C", "D", "E", "F", "G","H","I","J" ,"K","L" ,"M" ,"N","O" ,"P","Q","R" ,"S" ,"T" ,"U","V" ,"W" ,"X","Y","Z")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var alphabetList = listOf<String>("A" ,"B" ,"C", "D", "E", "F", "G","H","I","J" ,"K","L" ,"M" ,"N","O" ,"P","Q","R" ,"S" ,"T" ,"U","V" ,"W" ,"X","Y","Z")
        var bagMap = mutableMapOf("A" to 0,"B" to 0,"C" to 2, "D" to 4, "E" to 12, "F" to 2, "G" to 3,"H" to 2,"I" to 9,"J" to 1,"K" to 1,"L" to 4,"M" to 2,"N" to 6, "O" to 8,"P" to 2,"Q" to 1,"R" to 6,"S" to 4,"T" to 6,"U" to 4,"V" to 2,"W" to 2,"X" to 1,"Y" to 2,"Z" to 1)
        //A-9, B-2, C-2, D-4, E-12, F-2, G-3, H-2, I-9, J-1, K-1, L-4, M-2, N-6, O-8, P-2, Q-1, R-6, S-4, T-6, U-4, V-2, W-2, X-1, Y-2, Z-1
        val fileName = "dictionary.txt"
        val list = getDictionaryList(fileName)
        var letterFromBag = selectRandomOnMapAndSubstract(getRandomLetter(alphabetList),bagMap)


    }

    fun getDictionaryList(fileName:String):List<String> {
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText()}
        return inputString.split("\n")
    }

    fun getRandomLetter(list:List<String>):String{
        var newList = list
        return newList.random()
    }

    fun selectRandomOnMapAndSubstract(letter: String,map:MutableMap<String,Int>):String{
        var newLetter = letter
        var newMap = map
        var key : String ?= null
        var value : Int ?= null
        for((k,v) in map ){
            if(k == newLetter){
                if(map[k] == 0){
                    Toast.makeText(this,"key = $key ,value ${map[key]} ",Toast.LENGTH_LONG).show()

                    var alternativeRandomLetter = getRandomLetter(alphabetList)
                    return selectRandomOnMapAndSubstract(alternativeRandomLetter,map)

                }else {
                    map[k]?.minus(1)?.let { map.put(k, it) }
                    key = k
                    value = v
                }
            }
        }
        Toast.makeText(this,"key = $key ,value ${map.get(key)} ",Toast.LENGTH_LONG).show()

        return key!!
    }


}