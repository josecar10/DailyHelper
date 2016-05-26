package josecar.personal.dailyhelper.server;

import josecar.personal.dailyhelper.server.GenericAsyncRequest;
import josecar.personal.dailyhelper.server.ServerResponse;

/**
 * Created by JoseManuel on 26/05/2016.
 */
public class SevibusServerView {

    public static final String SERVICE_URL = "http://api.sevibus.sloydev.com/llegada/";
    public static final String REQUEST_SUFFIX = "/?lineas";

    public static void RequestStopInfo(Integer stopNumber, final GenericAsyncRequest.AsyncResponse response) {
        new GenericAsyncRequest(new GenericAsyncRequest.AsyncResponse() {
            @Override
            public void onResponseReceived(ServerResponse serverResponse) {
                response.onResponseReceived(serverResponse);
            }
        }).execute(SERVICE_URL + stopNumber.toString() + REQUEST_SUFFIX);
    }

}
