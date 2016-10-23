package com.example.user.tuzhong;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.example.user.tuzhong.ui.ToastUtil;

import java.util.List;

/**
 * Created by Guang on 2016/10/23.
 */

public class RideFragment extends Fragment implements WeatherSearch.OnWeatherSearchListener, AMapLocationListener{
    private TextView forecasttv;
    private android.widget.TextView reporttime1;
    private TextView reporttime2;
    private TextView weather;
    private TextView Temperature;
    private TextView wind;
    private TextView humidity;
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;
    private LocalWeatherForecast weatherforecast;
    private List<LocalDayWeatherForecast> forecastlist = null;
    private String cityname;
            //="上海市";
    private LocationSource.OnLocationChangedListener mListener;
    private TextView mLocationErrText;
    private static RideFragment frgment = null;

    public static Fragment newInstance() {
        if (frgment == null) {
            synchronized (RideFragment.class) {
                if (frgment == null) {
                    frgment = new RideFragment();
                }
            }
        }
        return frgment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment, container, false);
        TextView city =(TextView)view.findViewById(R.id.city);
        city.setText(cityname);
        forecasttv=(TextView)view.findViewById(R.id.forecast);
        reporttime1 = (TextView)view.findViewById(R.id.reporttime1);
        reporttime2 = (TextView)view.findViewById(R.id.reporttime2);
        weather = (TextView)view.findViewById(R.id.weather);
        Temperature = (TextView)view.findViewById(R.id.temp);
        wind=(TextView)view.findViewById(R.id.wind);
        humidity = (TextView)view.findViewById(R.id.humidity);
        searchliveweather();
        searchforcastsweather();
        return view;
    }

    private void searchforcastsweather() {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_FORECAST);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch = new WeatherSearch(getActivity());
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn();
    }

    private void searchliveweather() {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_LIVE);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch=new WeatherSearch(getActivity());
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn();
    }
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int rCode) {
        if (rCode == 1000) {
            if (localWeatherLiveResult != null && localWeatherLiveResult.getLiveResult() != null) {
                weatherlive = localWeatherLiveResult.getLiveResult();
                reporttime1.setText(weatherlive.getReportTime()+"发布");
                weather.setText(weatherlive.getWeather());
                Temperature.setText(weatherlive.getTemperature()+"°");
                wind.setText(weatherlive.getWindDirection()+"风     "+weatherlive.getWindPower()+"级");
                humidity.setText("湿度         "+weatherlive.getHumidity()+"%");
            }else {
                ToastUtil.show(getActivity(), R.string.no_result);
            }
        }else {
            ToastUtil.showerror(getActivity(), rCode);
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult weatherForecastResult, int rCode) {
        if (rCode == 1000) {
            if (weatherForecastResult!=null && weatherForecastResult.getForecastResult()!=null
                    && weatherForecastResult.getForecastResult().getWeatherForecast()!=null
                    && weatherForecastResult.getForecastResult().getWeatherForecast().size()>0) {
                weatherforecast = weatherForecastResult.getForecastResult();
                forecastlist= weatherforecast.getWeatherForecast();
                fillforecast();

            }else {
                ToastUtil.show(getActivity(), R.string.no_result);
            }
        }else {
            ToastUtil.showerror(getActivity(), rCode);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                mLocationErrText.setVisibility(View.GONE);
                mListener.onLocationChanged(amapLocation);
                cityname = amapLocation.getCity();
                Log.d("Mycity:", cityname );
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
                mLocationErrText.setVisibility(View.VISIBLE);
                mLocationErrText.setText(errText);
            }
        }
    }

    private void fillforecast() {
        reporttime2.setText(weatherforecast.getReportTime()+"发布");
        String forecast="";
        for (int i = 0; i < forecastlist.size(); i++) {
            LocalDayWeatherForecast localdayweatherforecast=forecastlist.get(i);
            String week = null;
            switch (Integer.valueOf(localdayweatherforecast.getWeek())) {
                case 1:
                    week = "周一";
                    break;
                case 2:
                    week = "周二";
                    break;
                case 3:
                    week = "周三";
                    break;
                case 4:
                    week = "周四";
                    break;
                case 5:
                    week = "周五";
                    break;
                case 6:
                    week = "周六";
                    break;
                case 7:
                    week = "周日";
                    break;
                default:
                    break;
            }
            String temp =String.format("%-3s/%3s",
                    localdayweatherforecast.getDayTemp()+"°",
                    localdayweatherforecast.getNightTemp()+"°");
            String date = localdayweatherforecast.getDate();
            forecast+=date+"  "+week+"                       "+temp+"\n\n";
        }
        forecasttv.setText(forecast);
    }
}
