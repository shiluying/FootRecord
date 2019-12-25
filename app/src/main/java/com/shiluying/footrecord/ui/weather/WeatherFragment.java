package com.shiluying.footrecord.ui.weather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.shiluying.footrecord.R;
import com.shiluying.footrecord.activity.MainActivity;

import java.util.List;

public class WeatherFragment extends Fragment {
    private static final String argweatherforecast = "weatherforecast";
    private static final String argforecast = "forecast";
    private static final String argreporttime1 = "reporttime1";
    private static final String argweather = "weather";
    private static final String argTemperature = "Temperature";
    private static final String argwind = "wind";
    private static final String arghumidity = "humidity";
    // TODO: Rename and change types of parameters
//    private String mParam1;
    private WeatherViewModel weatherViewModel;
    private TextView forecasttv;
    private TextView reporttime1;
    private TextView reporttime2;
    private TextView weather;
    private TextView Temperature;
    private TextView wind;
    private TextView humidity;
    private String cityname="北京市";//天气搜索的城市，可以写名称或adcode；
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        weatherViewModel =
                ViewModelProviders.of(this).get(WeatherViewModel.class);
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        TextView city =(TextView)view.findViewById(R.id.city);
        city.setText(cityname);
        forecasttv=(TextView)view.findViewById(R.id.forecast);
        reporttime1 = (TextView)view.findViewById(R.id.reporttime1);
        reporttime2 = (TextView)view.findViewById(R.id.reporttime2);
        weather = (TextView)view.findViewById(R.id.weather);
        Temperature = (TextView)view.findViewById(R.id.temp);
        wind=(TextView)view.findViewById(R.id.wind);
        humidity = (TextView)view.findViewById(R.id.humidity);
        if (getArguments() != null) {
            String str = getArguments().getString(argweatherforecast);
            if(str!=null){
                reporttime2.setText(str);
            }
            str = getArguments().getString(argforecast);
            if(!"".equals(str)){
                forecasttv.setText(str);
            }
            str = getArguments().getString(argreporttime1);
            if(!"".equals(str)){
                reporttime1.setText(str);
            }
            str = getArguments().getString(argweather);
            if(!"".equals(str)){
                weather.setText(str);
            }
            str = getArguments().getString(argTemperature);
            if(!"".equals(str)){
                Temperature.setText(str);
            }
            str = getArguments().getString(argwind);
            if(!"".equals(str)){
                wind.setText(str);
            }
            str = getArguments().getString(arghumidity);
            if(!"".equals(str)){
                humidity.setText(str);
            }
        }else {
            MainActivity activity= (MainActivity) getActivity();
            activity.searchliveweather(cityname);
            activity.searchforcastsweather(cityname);
        }
        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}