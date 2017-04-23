package josecar.personal.dailyhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import josecar.personal.dailyhelper.model.StopInfo;
import josecar.personal.dailyhelper.server.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d("MainActivity", "HelloWorld");
        MakeRequest();
    }

    void MakeRequest () {
        SevibusServerView.requestStopInfo(779, new StopInfo.StopInfoResponse() {
            @Override
            public void onStopInfoResponse(StopInfo stopInfoResponse) {
                onRequestFinished(stopInfoResponse);
            }
        });

        TussamServerView.requestStopInfo(779, new StopInfo.StopInfoResponse() {
            @Override
            public void onStopInfoResponse(StopInfo stopInfoResponse) {
                onRequestFinished(stopInfoResponse);
            }
        });
    }

    void onRequestFinished (StopInfo response) {
        if(response == null) {
            Log.e("MainActivity", "Null response");
            return;
        }

        Log.d("MainActivity", "Object parsed. Number: " + response.stopNumber.toString() + "   Name: " + response.stopName + "   ArrivalsCount: " + response.lineArrivals.size());
    }
}