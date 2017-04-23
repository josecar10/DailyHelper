package josecar.personal.dailyhelper.server;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import josecar.personal.dailyhelper.model.ArrivalInfo;
import josecar.personal.dailyhelper.model.LineArrivalInfo;
import josecar.personal.dailyhelper.model.StopInfo;

/**
 * Created by JoseManuel on 26/05/2016.
 */
public class SevibusServerView {

    public static final String SERVICE_URL = "http://api.sevibus.sloydev.com/llegada/";
    public static final String REQUEST_SUFFIX = "/?lineas";

    public static void requestStopInfo(final Integer stopNumber, final StopInfo.StopInfoResponse response) {
        new GenericAsyncRequest(new GenericAsyncRequest.AsyncResponse() {
            @Override
            public void onResponseReceived(ServerResponse serverResponse) {
               StopInfo stopInfo = null;

                if(serverResponse.error == null) {
                    try {
                        stopInfo = parseJson(stopNumber, serverResponse.rawResponse);
                    } catch (Exception e) {
                        Log.e("SevibusServerView", e.toString());
                        e.printStackTrace();
                        serverResponse.error = e.toString();
                    }
                } else {
                    Log.e("SevibusServerView", serverResponse.error);
                }

                response.onStopInfoResponse(stopInfo);
            }
        }).execute(SERVICE_URL + stopNumber.toString() + REQUEST_SUFFIX);
    }

    public static StopInfo parseJson(Integer stopNumber, InputStream in) throws IOException {
        ArrayList<LineArrivalInfo> lineArrivals = new ArrayList();

        JsonReader reader = new JsonReader(new InputStreamReader(in));
        reader.beginArray();
        while (reader.hasNext()) {
            String line = null;
            List<ArrivalInfo> arrivals = new ArrayList();

            /*reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("busLineName")) {
                    line = reader.nextString();
                } else if (name.equals("nextBus") || name.equals("secondBus")) {
                    Integer time = null, distance = null;

                    reader.beginObject();
                    while (reader.hasNext()) {
                        name = reader.nextName();
                        if (name.equals("timeInMinutes")) {
                            time = reader.nextInt();
                        } else if (name.equals("distanceInMeters")) {
                            distance = reader.nextInt();
                        } else {
                            reader.skipValue();
                        }
                    }
                    reader.endObject();
                    arrivals.add(new ArrivalInfo(line, time, distance));
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();*/
            lineArrivals.add(new LineArrivalInfo(line, arrivals));
        }
        reader.endArray();
        reader.close();
        return new StopInfo(stopNumber, null, lineArrivals);
    }

}
