package com.example.user.tuzhong.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ViewUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerBase;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.user.tuzhong.R;

import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by user on 2016/10/19.
 */

public class RideFragment extends SupportMapFragment implements AMapLocationListener, LocationSource{
    private static RideFragment fragment = null;
    //@ViewInject(R.id.map)

    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    public static SupportMapFragment newInstance() {
        if (fragment == null) {
            synchronized (RideFragment.class) {

                if (fragment == null) {
                    fragment = new RideFragment();

                }

            }
        }
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        init();
        return view;
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
            }

    }

    private void setUpMap() {
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.getUiSettings().setScaleControlsEnabled(true);
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
        aMap.setMyLocationStyle(myLocationStyle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mapView.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null == mLocationClient) {
            mLocationClient.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        if(mLocationClient!=null){
            mLocationClient.onDestroy();
        }
        mapView.onDestroy();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);
                aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            mLocationClient.setLocationListener(this);
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient == null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }
}
