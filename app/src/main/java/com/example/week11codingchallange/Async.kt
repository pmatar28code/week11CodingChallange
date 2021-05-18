import android.os.AsyncTask
import android.util.Log
import com.example.week11codingchallange.Repository
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection

class Async() : AsyncTask<String, Int, Void>() {
    var resultEnd:String ? =Repository().result
    override fun doInBackground(vararg params: String?): Void? {

        var url =
            URL("https://s3.amazonaws.com/thinkific/file_uploads/88925/attachments/fcd/686/a82/dictionary.txt")

        val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.setRequestMethod("GET")
        conn.connect()
        val InputStream = conn.getInputStream()
        val bo = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        InputStream.read(buffer) // Read from Buffer.

        bo.write(buffer)

        var testomg: String?
        testomg = bo.toString()
        resultEnd = testomg
        Repository().result = testomg!!
        //Log.v("REPO LINE", "$resultEnd")
        Log.v("LINE", "$testomg")

        return null


        // Write Into Buffer.

        /*
        conn.connect()

        var inputStream = conn.inputStream

        val bufferedReader: BufferedReader? = BufferedReader(InputStreamReader(inputStream))
         var line = bufferedReader?.readLine()
        Log.e("First LINE","${line}")


        var result:String ? = null
        while (line != null) {
            result += line
            line = bufferedReader?.readLine()
        }

        inputStream.close()
        Log.e("result","${result}")
        return null;

    }
    */

    }



}