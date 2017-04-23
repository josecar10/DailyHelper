package josecar.personal.dailyhelper.server;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Created by JoseManuel on 26/05/2016.
 */
public class GenericAsyncRequest extends AsyncTask<String, Void, ServerResponse> {

    public interface AsyncResponse {
        void onResponseReceived (ServerResponse serverResponse);
    }

    private AsyncResponse asyncResponse;
    private String requestMethod;
    private Map<String, String> requestProperties;
    private String requestData;

    public GenericAsyncRequest(AsyncResponse response) {
        asyncResponse = response;
        requestMethod = "GET";
        requestProperties = null;
        requestData = null;
    }

    public GenericAsyncRequest(AsyncResponse response, String method, Map<String, String> properties, String data) {
        asyncResponse = response;
        requestMethod = method;
        requestProperties = properties;
        requestData = data;
    }

    @Override
    protected ServerResponse doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            if(requestProperties != null && requestProperties.size() > 0) {
                for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
                    urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if(requestMethod == "POST") {
                urlConnection.setRequestMethod("POST");
                if(requestData != null) {
                    byte[] postData = requestData.getBytes(StandardCharsets.UTF_8);
                    urlConnection.setRequestProperty("Content-Length", Integer.toString(postData.length));
                    urlConnection.setDoOutput(true);
                    try( DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream())) {
                        wr.write( postData );
                    }
                }
            }

            String res = null, error = null;
            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //res = ServerUtils.ReadFromInputStream(new BufferedInputStream(urlConnection.getInputStream()));
            } else {
                error = urlConnection.getResponseMessage();
            }

            return new ServerResponse(res, error, urlConnection.getInputStream());
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