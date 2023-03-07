package com.example.dosage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.dosage.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.searchBtn.setOnClickListener{
            val thread = NetWorkThread()
            thread.start()
            thread.join()
        }

    }

    inner class NetWorkThread: Thread(){
          override fun run(){
              //serviceKey
              val key = ""

              //pageNo
              val pageNo = "1"

              //numOfRows
              val numOfRows = "3"

              //type
              val type = "xml"

              // API 정보를 가지고 있는 주소
              val site = "http://apis.data.go.kr/1471000/DayMaxDosgQyByIngdService/getDayMaxDosgQyByIngdInq?serviceKey="+
                      key+"&numOfRows="+numOfRows+"&pageNo="+pageNo+"type="+type

              val url = URL(site)
              val conn = url.openConnection()
              val input = conn.getInputStream()
              val isr = InputStreamReader(input)
              // br 라인 단위로 데이터 읽어옴
              val br = BufferedReader(isr)

              var str: String? = null
              val buf = StringBuffer()

              do{
                  str = br.readLine()

                  if(str!=null){
                      buf.append(str)
                  }
              }while (str!=null)

              val root = JSONObject(buf.toString())
              val response = root.getJSONObject("response").getJSONObject("body").getJSONObject("items")
              val item = response.getJSONArray("item")

              //화면에 출력
              runOnUiThread {
                  //페이지 수 만큼 반복하여 데이터를 불러온다
                  for(i in 0 until item.length()){
                      val jObject = item.getJSONObject(i)
                      if(binding.search.text.toString() == "${ JSON_Parse(jObject,"addr1")}"){
                          searchKeyword(binding.search.text.toString())
                      }
                  }
              }

          }
          // 함수를 통해 데이터를 불러온다.
          fun JSON_Parse(obj:JSONObject, data : String): String {

                // 원하는 정보를 불러와 리턴받고 없는 정보는 캐치하여 "없습니다."로 리턴받는다.
                return try {
                    obj.getString(data)
                } catch (e: Exception) {
                    "없습니다."
                }
          }

    }

    fun searchKeyword(txt: String){
        val textView = TextView(this)
        textView.text = txt
        binding.searchLayout.addView(textView, 0)
    }
}