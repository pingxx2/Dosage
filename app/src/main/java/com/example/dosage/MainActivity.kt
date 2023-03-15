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

var get_korName = ""
var get_engName = ""
var get_max = ""

var search_text = ""

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    var searchList = arrayListOf<SearchList>()

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

        //버튼 클릭 이벤트
        binding.searchBtn.setOnClickListener{
            search_text = binding.search.text.toString()
            searchList.clear()

            val thread = Thread(NetWorkThread(site))
            thread.start()
            thread.join()
            var searchList = arrayListOf<SearchList>(
                SearchList(get_korName, get_engName, get_max)
            )
            // 어댑터와 연결해서 mainListView에 나타내기
            val searchAdapter = SearchListAdapter(this, searchList)
            binding.mainListView.adapter = searchAdapter
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

                            if("${elem.getElementsByTagName("DRUG_CPNT_KOR_NM").item(0).textContent}".contains(search_text)){
                                get_korName = "${elem.getElementsByTagName("DRUG_CPNT_KOR_NM").item(0).textContent}"
                                get_engName = "${elem.getElementsByTagName("DRUG_CPNT_ENG_NM").item(0).textContent}"
                                get_max = "${elem.getElementsByTagName("DAY_MAX_DOSG_QY").item(0).textContent}"

                                var searchList = arrayListOf<SearchList>(
                                    SearchList(get_korName, get_engName, get_max)
                                )

                                break
                            }
                        }
                    }

                } catch (e: Exception){
                    Log.d("MAIN", "오픈API"+e.toString())
                }

          }

    }

}