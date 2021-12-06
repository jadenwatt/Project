package project1.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.FileUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;
import java.util.*;


@org.springframework.web.bind.annotation.RestController
public class RestController {

    File file = new File("./inventory.txt");
    File holder = new File("./temp.txt");

    // PART 1
    @RequestMapping(value = "/addVehicle", method = RequestMethod.POST)
    public Vehicle addVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        // ObjectMapper provides functionality for reading and writing JSON
        ObjectMapper mapper = new ObjectMapper();

        // Create a FileWriter to write to inventory.txt with append mode true
        FileWriter output = new FileWriter("./inventory.txt", true);

        // serialize greeting object to JSON and write it to file
        mapper.writeValue(output, newVehicle);

        // Append a new line character to the file
        // The output FileWriter is automatically closed by the mapper
        FileUtils.writeStringToFile(new File("./inventory.txt"), System.lineSeparator(), CharEncoding.UTF_8, true);

        return newVehicle;
    }

    @RequestMapping(value = "/getVehicle/{id}", method = RequestMethod.GET)
    public Vehicle getVehicle(@PathVariable("id") int id) throws IOException {
        // creates scanner object to iterate through file
        Scanner scanner = new Scanner(file);
        // vehicle object to return
        Vehicle vehicle = new Vehicle();
        // object mapper to convert string back to its original object
        ObjectMapper mapper = new ObjectMapper();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // convert id to a string and see if it is in the line
            if (line.substring(0, 10).contains(Integer.toString(id))) {
                // turn this string back into an object then return it
                vehicle = mapper.readValue(line, Vehicle.class);
                return vehicle;
            }
        }
        return vehicle;
    }

    @RequestMapping(value = "/updateVehicle", method = RequestMethod.PUT)
    public Vehicle updateVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        // if the same add new vehicle to the file and remove the old one
        ObjectMapper mapper = new ObjectMapper();
        Scanner scanner = new Scanner(file);
        Scanner scan = new Scanner(file);
        Scanner forHold = new Scanner(holder);
        Vehicle v = new Vehicle();
        String original = "";
        FileWriter result = new FileWriter(holder, true);
        StringBuffer buff = new StringBuffer();
        // this will be so that we can get this newVehicle string in JSON to replace into inventory file
        mapper.writeValue(result, newVehicle);
        FileUtils.writeStringToFile(holder, System.lineSeparator(), CharEncoding.UTF_8, true);
        String newString = "";
        // gets newVehicle's string from file
        while (forHold.hasNextLine()) {
            newString = forHold.nextLine(); // gets new vehicles JSON
        }

        // adds each line of inventory file to a StringBuffer so we can get the file as a string
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            buff.append(line);
            buff.append('\n');
            original = buff.toString();
        }

        // go through inventory file again and find line that has the same id and replace it
        while (scan.hasNextLine()) {
            String l = scan.nextLine();
            // if vehicle at this line in the file contains the same id as the vehicle passed in assign old string to be replaced
            // used substring to test beginning of string where id resides so it does not pick up the year value towards the end of the string
            if (l.substring(0, 10).contains(Integer.toString(newVehicle.getId()))) {
                // if it contains replace that string with newVehicles string
                original = original.replaceAll(l.substring(1, l.length() - 1), newString.substring(1, newString.length() - 1));
            }
        }
        // use output stream to update the inventory file with the new vehicle
        FileOutputStream fileOut = new FileOutputStream(file);
        fileOut.write(original.getBytes());
        fileOut.close();
        return newVehicle;
    }


    @RequestMapping(value = "/deletedVehicle/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteVehicle(@PathVariable("id") int id) throws IOException {
        Scanner scanner = new Scanner(new File("./inventory.txt"));
        String fileContents = "";
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // if this line doesn't have the id then add to a fileContents string, if it does then we dont add it
            if (!line.substring(0, 10).contains(Integer.toString(id))) {
                fileContents = fileContents + line + "\n";
            }
        }
        // print out this new string to the file essentially deleting that vehicle
        FileOutputStream fileOut = new FileOutputStream(file);
        fileOut.write(fileContents.getBytes());
        fileOut.close();
        return new ResponseEntity<>(fileContents, HttpStatus.OK);
    }

    // if list is less than size 10 loop through whole list, if it is greater loop through 10
    @RequestMapping(value = "/getLatestVehicles", method = RequestMethod.GET)
    public List<Vehicle> getLatestVehicles() throws IOException {
        // use read value to get the vehicle and add it to a list using arraylist
        // then loop through list removing the end until size is <=10
        ArrayList<Vehicle> list = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        ObjectMapper mapper = new ObjectMapper();
        Vehicle car;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            car = mapper.readValue(line, Vehicle.class);
            list.add(car);
        }
        // remove all values in list until list has 10 values, if it starts with less than 10 then it just prints
        // goes from end of list because the most recent vehicle prints at the bottom of the file
        int index = list.size() - 1;
        while (list.size() > 10) {
            list.remove(index);
            index--;
        }
        return list;
    }
}
