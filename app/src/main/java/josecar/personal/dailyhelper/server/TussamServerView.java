package josecar.personal.dailyhelper.server;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import josecar.personal.dailyhelper.model.ArrivalInfo;
import josecar.personal.dailyhelper.model.LineArrivalInfo;
import josecar.personal.dailyhelper.model.StopInfo;
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

    public static void requestStopInfo(final Integer stopNumber, final StopInfo.StopInfoResponse response) {
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
                StopInfo stopInfo = null;
                if(serverResponse.error == null) {
                    try {
                        stopInfo = parseXml(serverResponse.rawResponse);
                    } catch (Exception e) {
                        Log.e("TussamServerView", e.toString());
                        serverResponse.error = e.toString();
                    }
                } else {
                    Log.e("TussamServerView", serverResponse.error);
                }

                response.onStopInfoResponse(stopInfo);
            }
        }, "POST", requestProperties, String.format(BODY_CONTENT, stopNumber)).execute(URL_SOAP);
    }

    public static StopInfo parseXml(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "soap:Envelope");
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "soap:Body");
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, "ns2:getTiemposNodoResponse");
            return readStopInfo(parser);
        } finally {
            in.close();
        }
    }

    private static StopInfo readStopInfo(XmlPullParser parser) throws XmlPullParserException, IOException {
        Integer stopNumber = null;
        String stopName = null;
        ArrayList<LineArrivalInfo> lineArrivals = new ArrayList();

        parser.require(XmlPullParser.START_TAG, null, "tiempoNodo");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("codigo")) {
                stopNumber = readNumber("codigo", parser);
            } else if (name.equals("descripcion")) {
                stopName = readText("descripcion", parser);
            } else if (name.equals("lineasCoincidentes")) {
                parser.nextTag();
                parser.require(XmlPullParser.START_TAG, null, "lineasCoincidentes");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    if (parser.getName().equals("tiempoLinea")) {
                        lineArrivals.add(readTiempoLinea(parser));
                    } else {
                        skip(parser);
                    }
                }
            }
        }
        return new StopInfo(stopNumber, stopName, lineArrivals);
    }

    private static LineArrivalInfo readTiempoLinea(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "tiempoLinea");
        String line = null;
        ArrayList<ArrivalInfo> arrivals = new ArrayList();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("label")) {
                line = readText("label", parser);
            } else if (name.equals("estimacion1")) {
                arrivals.add(readEstimacion(line, parser));
            } else if (name.equals("estimacion2")) {
                arrivals.add(readEstimacion(line, parser));
            } else {
                skip(parser);
            }
        }
        return new LineArrivalInfo(line,arrivals);
    }

    private static ArrivalInfo readEstimacion(String line, XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "tiempoLinea");
        Integer time = null;
        Integer distance = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("minutos")) {
                time = readNumber("minutos", parser);
            } else if (name.equals("metros")) {
                distance = readNumber("metros", parser);
            } else {
                skip(parser);
            }
        }
        return new ArrivalInfo(line, time, distance);
    }

    // Extract the text contained in the body of the tag
    private static String readText(String tagName, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tagName);
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, tagName);
        return result;
    }

    // Extracts the number contained in the body of the tag
    private static Integer readNumber(String tagName, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tagName);
        Integer result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = Integer.parseInt(parser.getText());
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, tagName);
        return result;
    }

    // Extract the specified attribute of the tag
    private static String readAtribute(String tagName, String attributeName, XmlPullParser parser) throws IOException, XmlPullParserException {
        String res = "";
        parser.require(XmlPullParser.START_TAG, null, tagName);
        if (parser.getName().equals(tagName)) {
            res = parser.getAttributeValue(null, attributeName);
            parser.nextTag();
        }
        parser.require(XmlPullParser.END_TAG, null, tagName);
        return res;
    }

    // Skips the tag
    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    public static String generateRandomDeviceId() {
        return UUID.randomUUID().toString();
    }
}
