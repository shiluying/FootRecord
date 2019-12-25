package com.shiluying.footrecord.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.shiluying.footrecord.R;
import com.shiluying.footrecord.ui.add.AddFragment;
import com.shiluying.footrecord.ui.list.DetailFragment;
import com.shiluying.footrecord.ui.list.ListFragment;
import com.shiluying.footrecord.ui.list.diary.DiaryContent;
import com.shiluying.footrecord.utils.*;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        ListFragment.OnListFragmentInteractionListener,
        DetailFragment.OnFragmentInteractionListener,
        AddFragment.OnFragmentInteractionListener,
        View.OnClickListener,
        WeatherSearch.OnWeatherSearchListener {

    private AppBarConfiguration mAppBarConfiguration;
    EditText editText;
    private BaseUtils baseUtils;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    //===========天气========
   
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;
    private LocalWeatherForecast weatherforecast;
    private List<LocalDayWeatherForecast> forecastlist = null;
    String forecast="";
    String strweatherforecast="";
    String reporttime1="";
    String weather="";
    String Temperature="";
    String wind="";
    String humidity="";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton add = findViewById(R.id.add);
        add.setOnClickListener(this);
        verifyStoragePermissions(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_list, R.id.nav_weather, R.id.nav_version,R.id.nav_detail,R.id.add)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        baseUtils=new BaseUtils();


    }
    public void callGallery(){
        int permission_WRITE = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_READ = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permission_WRITE != PackageManager.PERMISSION_GRANTED || permission_READ != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }
        Intent getAlbum = new Intent(Intent.ACTION_PICK);
        getAlbum.setType("image/*");
        startActivityForResult(getAlbum,200);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        ContentResolver resolver = getContentResolver();
        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri,proj,null,null,null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        // 根据索引值获取图片路径
        String path = cursor.getString(column_index);
       baseUtils.insertImg(this,(EditText)findViewById(R.id.add_content),path);
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onListFragmentInteraction(DiaryContent.DiaryItem item) {
        Bundle arguments = new Bundle();
        arguments.putString("id",item.id);
        Navigation.findNavController(this,R.id.nav_host_fragment)
                .navigate(R.id.nav_detail, arguments);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                Bundle arguments = new Bundle();
                Navigation.findNavController(this,R.id.nav_host_fragment)
                        .navigate(R.id.nav_add);
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permission,
                                           int[] grantResults) {
        //requestCode就是requestPermissions()的第三个参数
        //permission就是requestPermissions()的第二个参数
        //grantResults是结果，0调试通过，-1表示拒绝
        Log.i("PPPXXX",grantResults[0]+"");
    }
    //=============================天气获取==============================

    public void searchforcastsweather(String cityname) {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_FORECAST);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch=new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }
    public void searchliveweather(String cityname) {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_LIVE);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch=new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }
    /**
     * 实时天气查询回调
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult , int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                weatherlive = weatherLiveResult.getLiveResult();
                reporttime1=weatherlive.getReportTime()+"发布";
                weather=weatherlive.getWeather();
                Temperature=weatherlive.getTemperature()+"°";
                wind=weatherlive.getWindDirection()+"风     "+weatherlive.getWindPower()+"级";
                humidity="湿度         "+weatherlive.getHumidity()+"%";
                Bundle arguments = new Bundle();
                arguments.putString("reporttime1",reporttime1);
                arguments.putString("weather",weather);
                arguments.putString("Temperature",Temperature);
                arguments.putString("wind",wind);
                arguments.putString("humidity",humidity);
                arguments.putString("weatherforecast",strweatherforecast);
                arguments.putString("forecast",forecast);
                Navigation.findNavController(this,R.id.nav_host_fragment)
                        .navigate(R.id.nav_weather,arguments);
            }else {
                ToastUtil.show(MainActivity.this, R.string.no_result);
            }
        }else {
            ToastUtil.showerror(MainActivity.this, rCode);
        }
    }
    /**
     * 天气预报查询结果回调
     * */
    @Override
    public void onWeatherForecastSearched(
            LocalWeatherForecastResult weatherForecastResult, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (weatherForecastResult!=null && weatherForecastResult.getForecastResult()!=null
                    && weatherForecastResult.getForecastResult().getWeatherForecast()!=null
                    && weatherForecastResult.getForecastResult().getWeatherForecast().size()>0) {
                weatherforecast = weatherForecastResult.getForecastResult();
                forecastlist= weatherforecast.getWeatherForecast();
                fillforecast();

            }else {
                ToastUtil.show(MainActivity.this, R.string.no_result);
            }
        }else {
            ToastUtil.showerror(MainActivity.this, rCode);
        }
    }
    private void fillforecast() {
        forecast="";
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
            String temp = String.format("%-3s/%3s",
                    localdayweatherforecast.getDayTemp()+"°",
                    localdayweatherforecast.getNightTemp()+"°");
            String date = localdayweatherforecast.getDate();
            forecast+=date+"  "+week+"                       "+temp+"\n\n";
        }
        Bundle arguments = new Bundle();
        strweatherforecast=weatherforecast.getReportTime()+"发布";

        arguments.putString("reporttime1",reporttime1);
        arguments.putString("weather",weather);
        arguments.putString("Temperature",Temperature);
        arguments.putString("wind",wind);
        arguments.putString("humidity",humidity);
        arguments.putString("weatherforecast",strweatherforecast);
        arguments.putString("forecast",forecast);
        Navigation.findNavController(this,R.id.nav_host_fragment)
                .navigate(R.id.nav_weather,arguments);
    }
}
