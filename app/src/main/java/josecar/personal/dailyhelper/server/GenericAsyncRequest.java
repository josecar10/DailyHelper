package josecar.personal.dailyhelper.server;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by JoseManuel on 26/05/2016.
 */
public class GenericAsyncRequest extends AsyncTask<String, Void, ServerResponse> {

    public interface AsyncResponse {
        void onResponseReceived (ServerResponse serverResponse);
    }

    private AsyncResponse asyncResponse;

    public GenericAsyncRequest(AsyncResponse response) {
        asyncResponse = response;
    }

    @Override
    protected ServerResponse doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String res = null, error = null;
            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                res = ServerUtils.ReadFromInputStream(new BufferedInputStream(urlConnection.getInputStream()));
            } else {
                error = urlConnection.getResponseMessage();
            }

            return new ServerResponse(res, error);
        } catch (Exception e) {
            Log.e("GenericAsyncRequest", e.toString());
            return new ServerResponse(null, e.toString());
        }
    }

    @Override
    protected void onPostExecute(ServerResponse serverResponse) {
        super.onPostExecute(serverResponse);
        asyncResponse.onResponseReceived(serverResponse);
    }
}