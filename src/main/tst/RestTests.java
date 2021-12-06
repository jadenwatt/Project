import org.junit.jupiter.api.Test;
import project1.demo.RestController;
import project1.demo.Vehicle;

import java.io.IOException;
import java.util.List;

public class RestTests {
    Vehicle ford = new Vehicle(1, "Ford Fusion", 2019, 25000);
    Vehicle tesla = new Vehicle(2, "Tesla Model Y", 2021, 50000);
    Vehicle honda = new Vehicle(3, "Honda Accord", 2017, 22000);
    Vehicle updated = new Vehicle(2, "Infinity QX 80", 2012, 45000);


    @Test
    public void addVehicleTest() throws IOException {
        RestController r = new RestController();
        r.addVehicle(ford);
        r.addVehicle(tesla);
        r.addVehicle(honda);
    }

    @Test
    public void getVehicleTest() throws IOException{
        RestController r = new RestController();
        Vehicle v = r.getVehicle(1);
        System.out.print(v.toString());
    }

    @Test
    public void updateVehicleTest() throws IOException{
        RestController r = new RestController();
        r.updateVehicle(updated);
    }

    @Test
    public void deleteTest() throws IOException {
        RestController r = new RestController();
        r.deleteVehicle(2);
    }

    @Test
    public void getLatestTest() throws IOException {
        RestController r = new RestController();
        List<Vehicle> v = r.getLatestVehicles();
        System.out.println(v);
    }
}
