package project1.demo;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<Vehicle> vehicleList;

    public Inventory (){
        vehicleList = new ArrayList<>();
    }
    public void addToList(Vehicle carName){
        vehicleList.add(carName);
    }
    public List<Vehicle> getVehicleList(){
        return vehicleList;
    }
    public void removeFromList(Vehicle carName) {
        vehicleList.remove(carName);
    }
    public String printInventory() {
        String inv = "";
        for (int i = 0; i < vehicleList.size(); i++) {
            inv = inv + " " + vehicleList.get(i).toString();
        }
        return inv;
    }
}
