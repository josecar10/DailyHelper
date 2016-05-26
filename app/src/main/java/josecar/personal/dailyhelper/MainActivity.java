package josecar.personal.dailyhelper;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "HelloWorld");
        MakeRequest();
    }


    String BODY_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<v:Envelope xmlns:v=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:c=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:d=\"http://www.w3.org/2001/XMLSchema\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "\t<v:Header />\n" +
            "\t<v:Body>\n" +
            "\t\t<n0:getTiemposNodo xmlns:n0=\"http://services.infotusws.tussam.com/\" id=\"o0\" c:root=\"1\">\n" +
            "\t\t\t<codigo i:type=\"d:int\">%d</codigo>\n" +
            "\t\t</n0:getTiemposNodo>\n" +
            "\t</v:Body>\n" +
            "</v:Envelope>";

    String URL_SOAP = "http://www.infobustussam.com:9005/InfoTusWS/services/InfoTus?WSDL";

    String SEVIBUS_URL = "http://api.sevibus.sloydev.com/llegada/779/?lineas";

    void MakeRequest () {
       /* try {
            URL url = new URL(SEVIBUS_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            Log.d("", in.toString());
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }*/

        Log.d("MainActivity", "trying");

        new RetrieveFeedTask().execute("");
    }

    /*private InputStream getArrivalsInputStream(String stopNumber) throws IOException {
        MediaType mediaType = MediaType.parse("text/xml;charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, String.format(BODY_CONTENT, stopNumber));
        Request request = new Request.Builder()
                .url(URL_SOAP)
                .post(body)
                .addHeader("content-type", "text/xml;charset=utf-8")
                .addHeader("authorization", "Basic aW5mb3R1cy11c2VybW9iaWxlOjJpbmZvdHVzMHVzZXIxbW9iaWxlMg==")
                .addHeader("deviceid", generateRandomDeviceId())
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().byteStream();
    }*/

    private String generateRandomDeviceId() {
        return UUID.randomUUID().toString();
    }
}


class RetrieveFeedTask extends AsyncTask<String, Void, String> {

    private Exception exception;

    protected String doInBackground(String... urls) {
        Log.d("MainActivity", "backround");
        try {
            URL url = new URLº);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Log.d("MainActivity", "inside");

            BufferedInputStream total = new BufferedInputStream(urlConnection.getInputStream());


            byte[] contents = new byte[1024];

            int bytesRead=0;
            String strFileContents = "";
            while( (bytesRead = total.read(contents)) != -1){
                strFileContents += new String(contents, 0, bytesRead);
            }

            Log.d("MainActivity", strFileContents);
            return strFileContents;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

}