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

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.go("a_example.out", "/home/iatsyk/work/hashcode/test/src/main/java/a_example.in");
        main.go("b_should_be_easy.out", "/home/iatsyk/work/hashcode/test/src/main/java/b_should_be_easy.in");
        main.go("c_no_hurry.out", "/home/iatsyk/work/hashcode/test/src/main/java/c_no_hurry.in");
        main.go("d_metropolis.out", "/home/iatsyk/work/hashcode/test/src/main/java/d_metropolis.in");
        main.go("e_high_bonus.out", "/home/iatsyk/work/hashcode/test/src/main/java/e_high_bonus.in");
    }

    private void go(String out, String in) throws Exception {
        List<Ride> rides = new ArrayList<>();
        Input input;
        int rideNum = 0;
        try (Scanner scanner = new Scanner(Paths.get(in))) {
            input = parseInput(scanner.nextLine());
            while (scanner.hasNextLine()) {
                rides.add(parseRide(scanner.nextLine(), rideNum));
                rideNum++;
            }
        }
        List<Car> cars = new ArrayList<>();
        for (int i = 1; i <= input.getVehicles(); i++) {
            cars.add(new Car());
        }

        Ride nextRide;
        do {
            cars.sort(Comparator.comparingInt(Car::getTime));
            Car car = cars.iterator().next();
            nextRide = findNextRide(rides, car, input.getBonus());
            if (nextRide != null) {
                recalculateCar(nextRide, car);
                rides.remove(nextRide);
            }
        } while (nextRide != null);

        System.out.println(rides.size());
        printResult(out, cars);
    }

    private Ride findNextRide(List<Ride> rides, Car car, int bonus) {
        float ppt = -1;
        Ride result = null;

        for (Ride ride : rides) {
            if (car.getTime() + calcDistanceToRide(ride, car) > ride.getLatestStart()) {
                continue;
            }
            int bonusAmount = ifBonusPossible(ride, car) ? bonus : 0;
            int waitTime = ride.getEarliestStart() - (car.getTime() + calcDistanceToRide(ride, car));
            waitTime = waitTime < 0 ? 0 : waitTime;
            float newPPT = (float) (ride.getDistance() + bonusAmount) / (float) (calcDistanceToRide(ride, car) + ride.getDistance() + waitTime);
            if (ppt < newPPT) {
                ppt = newPPT;
                result = ride;
            }
        }
        return result;
    }

    private boolean ifBonusPossible(Ride ride, Car car) {
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

    private int calcDistanceToRide(Ride ride, Car car) {
        return Math.abs(ride.getStartX() - car.getX()) + Math.abs(ride.getStartY() - car.getY());
    }

    private Ride parseRide(String rideStr, int rideNum) {
        Ride ride = new Ride();
        String[] split = rideStr.split(" ");
        ride.setStartX(Integer.valueOf(split[0]));
        ride.setStartY(Integer.valueOf(split[1]));
        ride.setFinishX(Integer.valueOf(split[2]));
        ride.setFinishY(Integer.valueOf(split[3]));
        ride.setEarliestStart(Integer.valueOf(split[4]));
        ride.setLatestFinish(Integer.valueOf(split[5]));
        ride.setDistance(Math.abs(ride.getFinishX() - ride.getStartX()) + Math.abs(ride.getFinishY() - ride.getStartY()));
        ride.setLatestStart(ride.getLatestFinish() - ride.getDistance());
        ride.setRideNum(rideNum);
        return ride;
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

    private Input parseInput(String inoutStr) {
        Input input = new Input();
        String[] split = inoutStr.split(" ");
        input.setRows(Integer.valueOf(split[0]));
        input.setColumns(Integer.valueOf(split[1]));
        input.setVehicles(Integer.valueOf(split[2]));
        input.setRides(Integer.valueOf(split[3]));
        input.setBonus(Integer.valueOf(split[4]));
        input.setSteps(Integer.valueOf(split[5]));
        return input;
    }


}
