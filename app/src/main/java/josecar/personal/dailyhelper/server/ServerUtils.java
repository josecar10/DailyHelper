package josecar.personal.dailyhelper.server;

import android.util.Log;
import java.io.InputStream;

/**
 * Created by JoseManuel on 26/05/2016.
 */
public class ServerUtils {

    public static String ReadFromInputStream (InputStream inputStream) {
        String res = "";

        try {
            byte[] contents = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(contents)) != -1) {
                res += new String(contents, 0, bytesRead);
            }
        } catch (Exception e) {
            Log.e("ServerUtils", e.toString());
            return null;
        }

        return res;
    }

}