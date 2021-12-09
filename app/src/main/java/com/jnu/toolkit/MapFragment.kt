package com.jnu.toolkit

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        mapView = rootView.findViewById(R.id.baidu_map_view)

        var centerPoint = LatLng(22.254895, 113.539458)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.jnu)
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)

        val builder = MapStatus.Builder()
        builder.zoom(19.0f).target(centerPoint)
        mapView.map.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))

        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == 200) {
                    val content: String? = msg.data.getString("data")
                    if (null != content) {
                        try {
                            val jsonObject = JSONObject(content)
                            val shops = jsonObject.getJSONArray("shops")
                            for (index in 0 until shops.length()) {
                                val shop = shops.getJSONObject(index)
                                centerPoint =
                                    LatLng(shop.getDouble("latitude"), shop.getDouble("longitude"))
                                val markerOptions =
                                    MarkerOptions().icon(bitmapDescriptor).position(centerPoint)
                                mapView.map.addOverlay(markerOptions) as Marker
                                val textOptions =
                                    TextOptions().bgColor(0xAAFFFF00.toInt()).fontSize(50)
                                        .fontColor(0xFFFF00FF.toInt()).text(shop.getString("name"))
                                        .rotate(0F).position(centerPoint)
                                mapView.map.addOverlay(textOptions)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

        val runnable = Runnable {
            try {
                val url = URL("http://file.nidama.net/class/mobile_develop/data/bookstore.json")
                val httpURLConnection = url.openConnection() as HttpURLConnection
                httpURLConnection.useCaches = false
                httpURLConnection.connect()
                if (httpURLConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = httpURLConnection.inputStream
                    val inputStreamReader = InputStreamReader(inputStream)
                    val bufferedReader = BufferedReader(inputStreamReader)

                    val stringBuffer = StringBuffer()

                    bufferedReader.lineSequence().forEach {
                        stringBuffer.append(it)
                    }

                    val message = Message()
                    message.what = 200
                    val bundle = Bundle()
                    bundle.putString("data", stringBuffer.toString())
                    message.data = bundle

                    handler.sendMessage(message)
                    Log.i("test", "onCreateView$stringBuffer")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        Thread(runnable).start()

        mapView.map.setOnMarkerClickListener(BaiduMap.OnMarkerClickListener {
            Toast.makeText(
                requireContext().applicationContext,
                "Marker clicked",
                Toast.LENGTH_SHORT
            )
                .show()
            return@OnMarkerClickListener false
        })

        return rootView
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MapFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}