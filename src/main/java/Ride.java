import lombok.Data;

/**
 * @author Vova Yatsyk
 * Date: 3/1/18.
 */
@Data
public class Ride {

    private int startX;
    private int startY;
    private int finishX;
    private int finishY;
    private int earliestStart;
    private int latestFinish;

    private int rideNum;
    private int distance;
    private int latestStart;

}
