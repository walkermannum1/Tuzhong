package com.example.user.tuzhong.util;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.user.tuzhong.MainActivity;
import com.example.user.tuzhong.R;

/**
 * Created by Guang on 2016/10/23.
 */

public class LocationUtil {
    public synchronized static String getCityname(AMapLocation location) {
        if (location == null) {
            return null;
        }
        String cityname = null;
        if (location.getErrorCode() == 0) {
            cityname = location.getCity();
        }
        return cityname;
    }
}

