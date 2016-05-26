package josecar.personal.dailyhelper.server;

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
    }

    /*
    private InputStream getArrivalsInputStream(String stopNumber) throws IOException {
        MediaType mediaType = MediaType.parse("text/xml;charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, String.format(BODY_CONTENT, stopNumber));
        Request request = new Request.Builder()
                .url(URL_SOAP)
                .post(body)
                .addHeader("content-type", "text/xml;charset=utf-8")
                .addHeader("authorization", AUTHORIZATION_HEADER)
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
