package coolweather.com.coolweather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.zip.Inflater;

import coolweather.com.coolweather.R;
import coolweather.com.coolweather.gson.Forecast;
import coolweather.com.coolweather.gson.Weather;
import coolweather.com.coolweather.utils.HandleResponseUtil;
import coolweather.com.coolweather.utils.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by HP on 2018/7/5.
 */

public class WeatherActivity extends AppCompatActivity {
    private TextView title_text;
    private TextView update_time_text;
    private TextView degree_text;
    private TextView weather_text;
    private TextView date_text;
    private TextView info_text;
    private TextView max_text;
    private TextView min_text;
    private TextView aqi_text;
    private TextView pm25_text;
    private TextView comfort_text;
    private TextView carwash_text;
    private TextView sport_text;
    private LinearLayout forecast_layout;
    private SwipeRefreshLayout swipe_refresh;
    private String weatherId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        title_text = (TextView) findViewById(R.id.title_text);
        update_time_text = (TextView) findViewById(R.id.update_time_text);
        degree_text = (TextView) findViewById(R.id.degree_text);
        weather_text = (TextView) findViewById(R.id.weather_text);
        date_text = (TextView) findViewById(R.id.date_text);
        info_text = (TextView) findViewById(R.id.info_text);
        max_text = (TextView) findViewById(R.id.max_text);
        min_text = (TextView) findViewById(R.id.min_text);
        aqi_text = (TextView) findViewById(R.id.aqi_text);
        pm25_text = (TextView) findViewById(R.id.pm25_text);
        comfort_text = (TextView) findViewById(R.id.comfort_text);
        carwash_text = (TextView) findViewById(R.id.carwash_text);
        sport_text = (TextView) findViewById(R.id.sport_text);
        forecast_layout = (LinearLayout) findViewById(R.id.forecast_layout);
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weather_str = preferences.getString("weather", null);
        if (weather_str != null) {
            Weather weather = HandleResponseUtil.handleWeatherResponse(weather_str);
            weatherId = weather.basic.weatherId;
            showWeather(weather);
        } else {
            weatherId = getIntent().getStringExtra("weatherId");
            requestWeather(weatherId);
        }
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });
    }

    private void requestWeather(final String weatherId) {
        String address = "http://guolin.tech/api/weather?cityid=" + weatherId
                + "&key=d47e49d3288a4572b2a798d75f234d05";
        HttpUtil.sendHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        swipe_refresh.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String response_str = response.body().string();
                Log.v("response:", response_str);
                final Weather weather = HandleResponseUtil.handleWeatherResponse(response_str);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather", response_str);
                            editor.apply();
                            showWeather(weather);
                        }
                        else
                        {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipe_refresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    private void showWeather(Weather weather) {
        title_text.setText(weather.basic.cityName);
        update_time_text.setText(weather.basic.update.updateTime);
        degree_text.setText(weather.now.tmp);
        weather_text.setText(weather.now.more.txt);
        forecast_layout.removeAllViews();
        for (Forecast forecast : weather.forecastList) {
            View view = LayoutInflater.from(WeatherActivity.this).inflate(R.layout.forecast_item, forecast_layout, false);
            date_text = (TextView) view.findViewById(R.id.date_text);
            info_text = (TextView) view.findViewById(R.id.info_text);
            max_text = (TextView) view.findViewById(R.id.max_text);
            min_text = (TextView) view.findViewById(R.id.min_text);
            date_text.setText(forecast.date);
            info_text.setText(forecast.more.info);
            max_text.setText(forecast.tmp.max);
            min_text.setText(forecast.tmp.min);
            forecast_layout.addView(view);
        }
        aqi_text.setText(weather.aqi.city.aqi);
        pm25_text.setText(weather.aqi.city.pm25);
        comfort_text.setText(weather.suggestion.comfort.info);
        carwash_text.setText(weather.suggestion.carWash.info);
        sport_text.setText(weather.suggestion.sport.info);
    }
}
