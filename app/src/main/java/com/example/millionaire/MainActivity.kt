package com.example.millionaire

import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        funcButtonEnable()
    }

    val question1 = "/posts?qType=1&count=5"
    val question2 = "/posts?qType=2&count=5"
    val question3 = "/posts?qType=3&count=5"
    val questionChildren = "/posts?qType=4&count=5"
    var countQuestion = 0;
    val listData = ArrayList<Question>()
    val arrayMoney = arrayOf("500","1.000","2.000","3.000","5.000","10.000","15.000","25.000",
                        "50.000","100.000","200.000","400.000","800.000","1.500.000","3.000.000")

    inner class AsyncTaskHandleJson:AsyncTask<String, String, String>(){
        override fun doInBackground(vararg url: String?): String {
            val text: String
            var str = ""
            when(countQuestion){
                in 1..5 -> str = question1
                in 6..10 -> str = question2
                in 11..15 -> str = question3
            }
            val connection = URL(url[0]+str).openConnection() as HttpURLConnection

            try{
                connection.connect()
                text = connection.inputStream.use { it.reader().use{reader -> reader.readText()} }
            }finally {
                connection.disconnect()
            }
            return text
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            handleJson(result)
        }
    }

    fun handleJson(jsonString: String?) {

        val jsonObject = JSONObject(jsonString)
        val str = jsonObject.getString("data").toString()
        val obj = JSONArray(str)
        for (i in 0 until obj.length()) {
            val obj: JSONObject = obj.getJSONObject(i)
            val text = obj.getString("question")
            var answers = obj.getString("answers")
            listData.add(Question(countQuestion+i-1,text,answers.split("[\"","\"]","\",\"")))
        }
        nextQuistion()
    }

    fun nextQuistion(){
        if (countQuestion<15) {
            text_count.text = "Вопрос №" + countQuestion.toString() + ". Сумма: " + arrayMoney[countQuestion - 1] + " Р."
            if (listData.size >= countQuestion) {
                randomButton()
                countQuestion++
            } else startRequest()
        }else{
            text_view.text ="Вы Победитель!"
            buttonA.text = "$$$"
            buttonB.text = "$$$"
            buttonC.text = "$$$"
            buttonD.text = "$$$"
            funButtonDisabled()
            listData.clear()
        }
    }

    fun randomButton(){
        text_view.text = listData[countQuestion-1].text
        var n = Random.nextInt(1,5)
        when(n){
            1 -> {
                buttonA.text = listData[countQuestion-1].answer[1]
                buttonB.text = listData[countQuestion-1].answer[2]
                buttonC.text = listData[countQuestion-1].answer[3]
                buttonD.text = listData[countQuestion-1].answer[4]
            }
            2 -> {
                buttonA.text = listData[countQuestion-1].answer[2]
                buttonB.text = listData[countQuestion-1].answer[1]
                buttonC.text = listData[countQuestion-1].answer[3]
                buttonD.text = listData[countQuestion-1].answer[4]
            }
            3 -> {
                buttonA.text = listData[countQuestion-1].answer[3]
                buttonB.text = listData[countQuestion-1].answer[2]
                buttonC.text = listData[countQuestion-1].answer[1]
                buttonD.text = listData[countQuestion-1].answer[4]
            }
            4 -> {
                buttonA.text = listData[countQuestion-1].answer[4]
                buttonB.text = listData[countQuestion-1].answer[2]
                buttonC.text = listData[countQuestion-1].answer[3]
                buttonD.text = listData[countQuestion-1].answer[1]
            }
        }
    }

    private fun startRequest(){
        if (countQuestion<16) {
            val url = "https://engine.lifeis.porn/api/millionaire.php"
            val arrayMoney = arrayOf(countQuestion.toString(), question1,question2,question3)
            AsyncTaskHandleJson().execute(url)
        }
    }

    fun buttonSt(view: View){
        countQuestion=1
        if (listData.isNotEmpty()) listData.clear()
        buttonA.isEnabled = true
        buttonB.isEnabled = true
        buttonC.isEnabled = true
        buttonD.isEnabled = true
        buttonStart.isEnabled = false
        startRequest()
    }

    fun error(){
        text_view.text = "Вы проиграли, нажмите Старт для начала игры."
        countQuestion=1
        if (listData.isNotEmpty()) listData.clear()
        buttonA.text = "A:"
        buttonB.text = "B:"
        buttonC.text = "C:"
        buttonD.text = "D:"
        funButtonDisabled()
    }

    fun button1(view: View) {
        if (buttonA.text.equals(listData[countQuestion - 2].answer[1])) {
            val myToast = Toast.makeText(this, "Ответ верный!", Toast.LENGTH_SHORT)
            myToast.show()
            nextQuistion()
        } else error()
    }

    fun button2(view: View) {
        if (buttonB.text.equals(listData[countQuestion - 2].answer[1])) {
            val myToast = Toast.makeText(this, "Ответ верный!", Toast.LENGTH_SHORT)
            myToast.show()
            nextQuistion()
        } else error()
    }

    fun button3(view: View) {
        if (buttonC.text.equals(listData[countQuestion - 2].answer[1])) {
            val myToast = Toast.makeText(this, "Ответ верный!", Toast.LENGTH_SHORT)
            myToast.show()
            nextQuistion()
        } else error()
    }

    fun button4(view: View) {
        if (buttonD.text.equals(listData[countQuestion - 2].answer[1])) {
            val myToast = Toast.makeText(this, "Ответ верный!", Toast.LENGTH_SHORT)
            myToast.show()
            nextQuistion()
        } else error()
    }

    fun funButtonDisabled(){
        buttonA.isEnabled = false
        buttonB.isEnabled = false
        buttonC.isEnabled = false
        buttonD.isEnabled = false
        buttonStart.isEnabled = true
    }

    fun funcButtonEnable()
    {
        buttonA.isEnabled = false
        buttonB.isEnabled = false
        buttonC.isEnabled = false
        buttonD.isEnabled = false
        buttonStart.isEnabled = true
    }
}
