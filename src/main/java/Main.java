import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * @author Vova Yatsyk
 * Date: 3/1/18.
 */
public class Main {

    InputData data;

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.go("a_example.out", "/home/iatsyk/work/hashcode/test/src/main/java/a_example.in");
        main.go("b_should_be_easy.out", "/home/iatsyk/work/hashcode/test/src/main/java/b_should_be_easy.in");
        main.go("c_no_hurry.out", "/home/iatsyk/work/hashcode/test/src/main/java/c_no_hurry.in");
        main.go("d_metropolis.out", "/home/iatsyk/work/hashcode/test/src/main/java/d_metropolis.in");
        main.go("e_high_bonus.out", "/home/iatsyk/work/hashcode/test/src/main/java/e_high_bonus.in");
    }

    void go(String out, String in) throws Exception {
        List<Ride> rides = new ArrayList<>();
        int rideNum = 0;
        try (Scanner scanner = new Scanner(Paths.get(in))) {
            data = parseData(scanner.nextLine());
            while (scanner.hasNextLine()) {
                rides.add(parseRide(scanner.nextLine(), rideNum));
                rideNum++;
            }
        }
        List<Car> cars = new ArrayList<>();
        for (int i = 1; i <= data.getVehicles(); i++) {
            cars.add(new Car(i));
        }
        Ride nextRide;
        do {
//                nextRide = findNextRideStepan(rides, car, nextRide);
            cars.sort(Comparator.comparingInt(Car::getTime));
            Car car = cars.iterator().next();
            nextRide = findNextRideSerhii(rides, car);
            if (nextRide != null) {
                recalculateCar(nextRide, car);
                rides.remove(nextRide);
            }
        } while (nextRide != null);
        System.out.println(rides.size());
        printResult(out, cars);
    }

    private Ride findNextRideSerhii(List<Ride> rides, Car car) {
        float ppt = -1;
        Ride result = null;

        for (Ride ride : rides) {
            if (car.getTime() + calcDistanceToRide(ride, car) > ride.getLatestStart()) {
                continue;
            }
            int bonus = ifBonusPossible(ride, car) ? data.getBonus() : 0;
            int waitTime = ride.getEarliestStart() - (car.getTime() + calcDistanceToRide(ride, car));
            waitTime = waitTime < 0 ? 0 : waitTime;
            float newPPT = (float) (ride.getDistance() + bonus * 2) / (float) (calcDistanceToRide(ride, car) + ride.getDistance() * 10 + waitTime);
            if (ppt < newPPT) {
                ppt = newPPT;
                result = ride;
            }
        }
        return result;
    }

    boolean ifBonusPossible(Ride ride, Car car) {
        return car.getTime() + calcDistanceToRide(ride, car) < ride.getEarliestStart();
    }

    private void recalculateCar(Ride ride, Car car) {
        int distanceToRide = calcDistanceToRide(ride, car);
        int waitTime = Math.max(ride.getEarliestStart() - (car.getTime() + distanceToRide), 0);
        car.setX(ride.getFinishX());
        car.setY(ride.getFinishY());
        car.setTime(car.getTime() + distanceToRide + waitTime + ride.getDistance());
        car.setOrder(ride.getRideNum());
    }

    int calcDistanceToRide(Ride ride, Car car) {
        return Math.abs(ride.getStartX() - car.getX()) + Math.abs(ride.getStartY() - car.getY());
    }

    private Ride parseRide(String ride, int rideNum) {
        Ride result = new Ride();
        String[] split = ride.split(" ");
        result.setStartX(Integer.valueOf(split[0]));
        result.setStartY(Integer.valueOf(split[1]));
        result.setFinishX(Integer.valueOf(split[2]));
        result.setFinishY(Integer.valueOf(split[3]));
        result.setEarliestStart(Integer.valueOf(split[4]));
        result.setLatestFinish(Integer.valueOf(split[5]));
        result.setDistance(Math.abs(result.getFinishX() - result.getStartX()) + Math.abs(result.getFinishY() - result.getStartY()));
        result.setLatestStart(result.getLatestFinish() - result.getDistance());
        result.setRideNum(rideNum);
        return result;
    }

    private void printResult(String out, List<Car> cars) throws IOException {
        Path path = Paths.get(out);
        String str = "";
        for (Car car : cars) {
            str = str + car.getOrders().size();
            List<Integer> orders = car.getOrders();
            for (Integer order : orders) {
                str = str + " " + order;
            }
            str = str + "\n";
        }
        Files.write(path, str.getBytes());
    }

    private InputData parseData(String data) {
        InputData result = new InputData();
        String[] split = data.split(" ");
        result.setRows(Integer.valueOf(split[0]));
        result.setColumns(Integer.valueOf(split[1]));
        result.setVehicles(Integer.valueOf(split[2]));
        result.setRides(Integer.valueOf(split[3]));
        result.setBonus(Integer.valueOf(split[4]));
        result.setSteps(Integer.valueOf(split[5]));
        return result;
    }


}
