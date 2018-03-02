import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vova Yatsyk
 * Date: 3/1/18.
 */
@Data
public class Car {

    private List<Integer> orders = new ArrayList<>();
    private int x = 0;
    private int y = 0;
    private int time = 0;

    public void setOrder(int order) {
        orders.add(order);
    }

}
