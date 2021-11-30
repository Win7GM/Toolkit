package com.jnu.toolkit

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng

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

        val centerPoint = LatLng(22.254895, 113.539458)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.jnu)
        val bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)

        val builder = MapStatus.Builder()
        builder.zoom(19.0f).target(centerPoint)
        mapView.map.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))

        val markerOptions = MarkerOptions()
        markerOptions.position(centerPoint).icon(bitmapDescriptor)

        val marker = mapView.map.addOverlay(markerOptions)

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