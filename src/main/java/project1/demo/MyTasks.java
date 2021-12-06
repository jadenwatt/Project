package project1.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project1.demo.RestController;
import project1.demo.Vehicle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

// extended rest controller class not sure if this is to be done but I needed it

// uncomment this for sheduling to work and comment out the @restcontroller
@Component
public class MyTasks {
    int id = 0;
    ObjectMapper mapper = new ObjectMapper();
    File file = new File("./inventory.txt");

    // not sure what 5000 stands for, got it from lecture
    @Scheduled(fixedDelay = 5000)
    public void addVehicle() throws IOException {
        FileWriter fw = new FileWriter("./inventory.txt", true);
        List<String> models = new ArrayList<>();
        models.add("Ford Fusion");
        models.add("BMW m4");
        models.add("Honda Accord");
        models.add("Kia Stinger");

        id++;
        Random r = new Random();
        // creates random int for the size of the list
        int randString = r.nextInt(models.size());
        // assigns a string to a random index in list
        String makeModel = models.get(randString);
        // creates random year from 1986-2016
        int year = r.nextInt(2016) + 1986;
        double retail = r.nextInt(45000) + 15000;
        Vehicle vehicle = new Vehicle(id, makeModel, year, retail);
        // called add vehicle from rest controller on this vehicle
        mapper.writeValue(fw, vehicle);
        FileUtils.writeStringToFile(file, System.lineSeparator(), CharEncoding.UTF_8, true);
    }

    @Scheduled(fixedDelay = 1000)
    public void deleteVehicle() throws IOException {
        String fileContents = "";
        Random r = new Random();
        int id = r.nextInt(100);
        ObjectMapper mapper = new ObjectMapper();
        Scanner scanner = new Scanner(file);
        // iterate through vehicle list to find vehicle with matching id then delete
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (!line.substring(0, 10).contains(Integer.toString(id))) {
                fileContents = fileContents + line + "\n";
            }
        }
        FileOutputStream fileOut = new FileOutputStream(file);
        fileOut.write(fileContents.getBytes());
        fileOut.close();
    }

    @Scheduled(fixedDelay = 2000)
    public void updateVehicle() throws IOException {
        Random r = new Random();
        int id = r.nextInt(100);
        String makeModel = "Honda Accord";
        int year = 2017;
        double retail = 25000;
        // call update vehicle with new vehicle
        Vehicle updated = new Vehicle(id, makeModel, year, retail);
        FileWriter fw = new FileWriter("./temp.txt", true);
        Scanner hold = new Scanner(new File("./temp.txt"));
        mapper.writeValue(fw, updated);
        FileUtils.writeStringToFile(new File("./temp.txt"), System.lineSeparator(), CharEncoding.UTF_8, true);
        String newString = "";
        Scanner scan = new Scanner(file);
        String line = "";
        Scanner scanned = new Scanner(file);

        while (hold.hasNextLine()) {
            newString = hold.nextLine();
        }

        while (scan.hasNextLine()) {
            line += scan.nextLine() + "\n";
        }

        while (scanned.hasNextLine()) {
            String l = scanned.nextLine();
            if (l.substring(0, 10).contains(Integer.toString(updated.getId()))) {
                // if it contains add newVehicle to the new file
                line = line.replaceAll(l.substring(1, l.length() - 1), newString.substring(1, newString.length() - 1));
            }
        }

        FileOutputStream fileOut = new FileOutputStream(file);
        fileOut.write(line.getBytes());
        fileOut.close();

    }

    @Scheduled(cron = "0 0 * * * *")
    public void latestVehiclesReport() throws IOException {
        ArrayList<Vehicle> list = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        Vehicle car;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            car = mapper.readValue(line, Vehicle.class);
            list.add(car);
        }
        // if there are 10 or less vehicles in inventory just assign list to inventory
        int index = list.size() - 1;
        while (list.size() > 10) {
            list.remove(index);
            index--;
        }
    }

}

