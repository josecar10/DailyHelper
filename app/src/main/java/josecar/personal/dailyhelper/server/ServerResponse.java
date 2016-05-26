package josecar.personal.dailyhelper.server;

/**
 * Created by JoseManuel on 26/05/2016.
 */
public class ServerResponse {
    public String text;
    public String error;

    public ServerResponse (String _text, String _error) {
        text = _text;
        error = _error;
    }
}
