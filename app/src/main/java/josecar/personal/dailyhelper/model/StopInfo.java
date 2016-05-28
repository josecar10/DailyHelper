package josecar.personal.dailyhelper.model;

import java.util.List;

/**
 * Created by JoseManuel on 28/05/2016.
 */
public class StopInfo {

    public final Integer stopNumber;
    public final String stopName;
    public final List<LineArrivalInfo> lineArrivals;

    public StopInfo(Integer stopNumber, String stopName, List<LineArrivalInfo> lineArrivals) {
        this.stopNumber = stopNumber;
        this.stopName = stopName;
        this.lineArrivals = lineArrivals;
    }

}
