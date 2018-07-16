package coolweather.com.coolweather.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.litepal.tablemanager.Connector;

import coolweather.com.coolweather.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
