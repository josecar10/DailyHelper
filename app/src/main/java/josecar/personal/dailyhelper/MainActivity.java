package josecar.personal.dailyhelper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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
        SevibusServerView.RequestStopInfo(779, new GenericAsyncRequest.AsyncResponse() {
            @Override
            public void onResponseReceived(ServerResponse serverResponse) {
                onRequestFinished(serverResponse);
            }
        });

        TussamServerView.RequestStopInfo(779, new GenericAsyncRequest.AsyncResponse() {
            @Override
            public void onResponseReceived(ServerResponse serverResponse) {
                onRequestFinished(serverResponse);
            }
        });
    }

    void onRequestFinished (ServerResponse response) {
        if(response.error != null) {
            Log.e("MainActivity", response.error);
            return;
        }

        Log.d("MainActivity", response.text);
    }
}