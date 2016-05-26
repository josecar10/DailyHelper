package josecar.personal.dailyhelper.server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import josecar.personal.dailyhelper.server.GenericAsyncRequest;

/**
 * Created by JoseManuel on 26/05/2016.
 */
public class TussamServerView {

    public static final String BODY_CONTENT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<v:Envelope xmlns:v=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:c=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:d=\"http://www.w3.org/2001/XMLSchema\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "\t<v:Header />\n" +
            "\t<v:Body>\n" +
            "\t\t<n0:getTiemposNodo xmlns:n0=\"http://services.infotusws.tussam.com/\" id=\"o0\" c:root=\"1\">\n" +
            "\t\t\t<codigo i:type=\"d:int\">%d</codigo>\n" +
            "\t\t</n0:getTiemposNodo>\n" +
            "\t</v:Body>\n" +
            "</v:Envelope>";
    public static final String URL_SOAP = "http://www.infobustussam.com:9005/InfoTusWS/services/InfoTus?WSDL";
    public static final String AUTHORIZATION_HEADER = "Basic aW5mb3R1cy11c2VybW9iaWxlOjJpbmZvdHVzMHVzZXIxbW9iaWxlMg==";

    public static void RequestStopInfo(Integer stopNumber, final GenericAsyncRequest.AsyncResponse response) {
        //TODO: SOAP petition

        HashMap<String, String> requestProperties = new HashMap<>();
        requestProperties.put("content-type", "text/xml");
        requestProperties.put("charset", "utf-8");
        requestProperties.put("authorization", AUTHORIZATION_HEADER);
        requestProperties.put("deviceid", generateRandomDeviceId());
        requestProperties.put("cache-control", "no-cache");

        new GenericAsyncRequest(new GenericAsyncRequest.AsyncResponse() {
            @Override
            public void onResponseReceived(ServerResponse serverResponse) {
                response.onResponseReceived(serverResponse);
            }
        }, "POST", requestProperties, String.format(BODY_CONTENT, stopNumber)).execute(URL_SOAP);
    }

    public static String generateRandomDeviceId() {
        return UUID.randomUUID().toString();
    }
}
