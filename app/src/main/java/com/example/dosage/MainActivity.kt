package com.example.dosage

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.dosage.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

var text = ""

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        // api 불러오기 위한 정보
        val key = ""
        val pageNo = "1"
        val numOfRows = "3"
        val type = "xml"
        val site = "http://apis.data.go.kr/1471000/DayMaxDosgQyByIngdService/getDayMaxDosgQyByIngdInq?serviceKey="+
                key+"&numOfRows="+numOfRows+"&pageNo="+pageNo+"&type="+type

        binding.searchBtn.setOnClickListener{
            val thread = Thread(NetWorkThread(site))
            thread.start()
            thread.join()
            binding.testText.text = text
        }

    }

    inner class NetWorkThread(var url: String): Runnable{
          @RequiresApi(Build.VERSION_CODES.N)
          override fun run(){
                try{
                    val xml: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)
                    xml.documentElement.normalize()

                    //찾고자 하는 데이터가 어느 노드 아래에 있는지 확인
                    val list: NodeList = xml.getElementsByTagName("item")

                    for(i in 0..list.length-1){
                        val n: Node = list.item(i)
                        if(n.getNodeType() == Node.ELEMENT_NODE){
                            val elem = n as Element
                            val map = mutableMapOf<String, String>()

                            for(j in 0..elem.attributes.length-1){
                                map.putIfAbsent(elem.attributes.item(j).nodeName, elem.attributes.item(j).nodeValue)
                            }
                             text = "${elem.getElementsByTagName("DRUG_CPNT_KOR_NM").item(0).textContent}"
                            break
                        }
                    }

                } catch (e: Exception){
                    Log.d("MAIN", "오픈API"+e.toString())
                }

          }

    }

    fun searchKeyword(txt: String){
        val textView = TextView(this)
        textView.text = txt
        binding.searchLayout.addView(textView, 0)
    }
}