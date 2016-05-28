package josecar.personal.dailyhelper.server;

import java.io.InputStream;

/**
 * Created by JoseManuel on 26/05/2016.
 */
public class ServerResponse {
    public String text;
    public String error;
    public InputStream rawResponse;

    public ServerResponse (String _text, String _error) {
        text = _text;
        error = _error;
        rawResponse = null;
    }

    public ServerResponse (String _text, String _error, InputStream _rawResponse) {
        text = _text;
        error = _error;
        rawResponse = _rawResponse;
    }
}
