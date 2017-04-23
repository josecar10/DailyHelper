package josecar.personal.dailyhelper.model;

/**
 * Created by JoseManuel on 28/05/2016.
 */
public class ArrivalInfo {

    public final String line;
    public final Integer time;
    public final Integer distance;

    public ArrivalInfo(String line, Integer time, Integer distance) {
        this.line = line;
        this.time = time;
        this.distance = distance;
    }

}
