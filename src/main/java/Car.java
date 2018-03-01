import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vova Yatsyk
 * Date: 3/1/18.
 */
@Data
public class Car implements Comparable<Integer> {

    private Integer orderNum;
    private List<Integer> orders = new ArrayList<>();
    private int time = 0;
    private int x = 0;
    private int y = 0;

    public Car(int i) {
        orderNum = i;
    }

    public void setOrder(int order) {
        orders.add(order);
    }


    @Override
    public int compareTo(Integer o) {
        return time - o;
    }

}
