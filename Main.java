import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    // check if the entry wont form a dupe (aka have the same id)
    private static boolean dupeCheck(String id, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineSplit = line.split(";");
                if (lineSplit[0].equals(id)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // capitalize the first letter of each word
    private static String capitalize(String city) {
        StringBuilder result = new StringBuilder();
        int count = 0;
        for (int i = 0; i < city.length(); i++) {
            if (count == 0) {
                char firstLetter = Character.toUpperCase(city.charAt(i));
                result.append(firstLetter);
            } else {
                char letter = city.charAt(i);
                result.append(letter);
            }
            count++;
            if (city.charAt(i) == ' ') {
                count = 0;
            }
        }
        return result.toString();
    }
    
    // check if the date is actually a valid date
    private static boolean dateVal(String date) {
        // DD/MM/YYYY
        if (date.length() != 10) {
            return false;
        }
        String[] dateSplit = date.split("/");
        if (dateSplit.length != 3) {
            return false;
        }
        int day = Integer.parseInt(dateSplit[0]);
        int month = Integer.parseInt(dateSplit[1]);
        int year = Integer.parseInt(dateSplit[2]);

        if (year < 1000 || year > 9999 || month < 1 || month > 12) {
            return false;
        }

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        if (leapYear(year)) {
            daysInMonth[1] = 29;
        }
        return day >= 1 && day <= daysInMonth[month - 1];
    }

    private static boolean leapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    
    // bubble sort algoritms datumu skatartošanai sarakstā
    private static void bubbleSort(ArrayList<String> datesList) {
        int listSize = datesList.size();
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < listSize; i++) {
                if (compareDates(datesList.get(i - 1), datesList.get(i)) > 0) {
                    String temp = datesList.get(i - 1);
                    datesList.set(i - 1, datesList.get(i));
                    datesList.set(i, temp);
                    swapped = true;
                }
            }
            listSize--;
        } while (swapped);
    }

    // compare two dates
    private static int compareDates(String date1, String date2) {
        // DD/MM/YYYY
        String[] splitDate1 = date1.split("/");
        String[] splitDate2 = date2.split("/");
        int year1 = Integer.parseInt(splitDate1[2]);
        int month1 = Integer.parseInt(splitDate1[1]);
        int day1 = Integer.parseInt(splitDate1[0]);
        int year2 = Integer.parseInt(splitDate2[2]);
        int month2 = Integer.parseInt(splitDate2[1]);
        int day2 = Integer.parseInt(splitDate2[0]);

        if (year1 != year2) {
            return Integer.compare(year1, year2);
        }

        if (month1 != month2) {
            return Integer.compare(month1, month2);
        }

        return Integer.compare(day1, day2);

    }
// the main comand methods begin here ==========================================================================================
    
// ADD comand ==================================================================================================================
    public static void add(String input) {
        String filename = "db.csv"; // this might need rework, I dont get it
        String[] splitInput = input.split(";");

        if (splitInput.length != 6) {
            System.out.println("wrong field count");
            return;
        }

        String id = splitInput[0];
        String city = capitalize(splitInput[1]);
        String date = splitInput[2];
        String days = splitInput[3];
        String price = splitInput[4];
        String vehicle = splitInput[5].toUpperCase();

        if (!id.matches("\\d{3}") || dupeCheck(id, filename)) { 
            System.out.println("wrong id");
            return;
        }

        if (!dateVal(date)) {
            System.out.println("wrong date");
            return;
        }
        
        try {
            Integer.parseInt(days);
        } catch (NumberFormatException e) {
            System.out.println("wrong day count");
            return;
        }
        int newDays = Integer.parseInt(days);

        try {
            Double.parseDouble(price);
        } catch (NumberFormatException e) {
            System.out.println("wrong price");
            return;
        }
        double newPrice = Double.parseDouble(price);
        String finPrice = String.format("%.2f", newPrice);

        if (!(vehicle.equals("TRAIN") || vehicle.equals("PLANE") || vehicle.equals("BUS") || vehicle.equals("BOAT"))) {
            System.out.println("wrong vehicle");
            return;
        }
        
        // everything is checked
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            String entry = id + ";" + city + ";" + date + ";" + newDays + ";" + finPrice + ";" + vehicle + "\n";
            writer.write(entry);
            System.out.println("added");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

// DEL comand =================================================================================================================
    public static void del(String input) {
        String filename = "db.csv"; // this might need rework, I dont get it
        // validate the id input
        if (!input.matches("\\d{3}")) { 
            System.out.println("wrong id");
            return;
        }
        boolean idExists = dupeCheck(input, filename);
        if (!idExists) {
            System.out.println("wrong id");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filename)); BufferedWriter writer = new BufferedWriter(new FileWriter("temp.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineSplit = line.split(";");
                if (!lineSplit[0].equals(input)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new File(filename).delete();
            new File("temp.csv").renameTo(new File(filename));
            System.out.println("deleted");
        } catch (SecurityException e) {
            e.printStackTrace();
        }  
    }

// EDIT command ================================================================================================================
    public static void edit(String input) {
        String filename = "db.csv";
        String[] splitInput = input.split(";");

        if (splitInput.length != 6) {
            System.out.println("wrong field count");
            return;
        }

        String id = splitInput[0];
        String city = capitalize(splitInput[1]);
        String date = splitInput[2];
        String days = splitInput[3];
        String price = splitInput[4];
        String vehicle = splitInput[5].toUpperCase();

        String newInput = (id + ";" + city + ";" + date + ";" + days + ";" + price + ";" + vehicle);
        String[] newSplitInput = newInput.split(";");

        // validate data
        if (!id.matches("\\d{3}") && dupeCheck(id, filename)) { 
            System.out.println("wrong id");
            return;
        }

        if (!date.equals("") && !dateVal(date)) {
            System.out.println("wrong date");
            return;
        }
        
        if (!days.equals("")) {
            try {
                Integer.parseInt(days);
            } catch (NumberFormatException e) {
                System.out.println("wrong day count");
                return;
            }
        }

        if (!price.equals("")) {
            try {
                Double.parseDouble(price);
            } catch (NumberFormatException e) {
                System.out.println("wrong price");
                return;
            }
        }

        if (!vehicle.equals("") && !(vehicle.equals("TRAIN") || vehicle.equals("PLANE") || vehicle.equals("BUS") || vehicle.equals("BOAT"))) {
            System.out.println("wrong vehicle");
            return;
        }
        //data validation done

        boolean idFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename)); BufferedWriter writer = new BufferedWriter(new FileWriter("temp.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineSplit = line.split(";");
                if (!lineSplit[0].equals(id)) {
                    writer.write(line);
                    writer.newLine();
                } else {
                    StringBuilder modifiedLine = new StringBuilder();
                    for (int i = 0; i < newSplitInput.length; i++){
                        if (!newSplitInput[i].equals("")) {
                            modifiedLine.append(newSplitInput[i]).append(";");
                        } else {
                            modifiedLine.append(lineSplit[i]).append(";");
                        }
                    }
                    modifiedLine.setLength(modifiedLine.length() - 1);
                    writer.write(modifiedLine.toString());
                    writer.newLine();
                    idFound = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (idFound) {
            System.out.println("changed");
        } else {
            System.out.println("wrong id");
            return;
        }

        try {
            new File(filename).delete();
            new File("temp.csv").renameTo(new File(filename));
        } catch (SecurityException e) {
            e.printStackTrace();
        }  
    }

// PRINT comand ===================================================================================================================
    public static void print() {
        String filename = "db.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            System.out.println("------------------------------------------------------------");
            System.out.println("ID  City                 Date         Days     Price Vehicle");
            System.out.println("------------------------------------------------------------");
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String id = parts[0];
                String city = parts[1];
                String date = parts[2];
                String days = parts[3];
                String price = parts[4];
                String vehicle = parts[5];
                System.out.printf("%-4s%-21s%-11s%6s%10s%-8s%n", id, city, date, days, price, " " + vehicle);
            }
            System.out.println("------------------------------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
            return; 
        }
    }

// SORT comand ====================================================================================================================
    public static void sort() {
        String filename = "db.csv";
        ArrayList<String> datesList = new ArrayList<>();
        ArrayList<String> fileLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineSplit = line.split(";");
                String date = lineSplit[2];
                datesList.add(date);
                fileLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return; 
        }

        bubbleSort(datesList);

        ArrayList<String> sortedLines = new ArrayList<>();
        for (String date : datesList) {
            for (String line : fileLines) {
                if (line.contains(date)) {
                    sortedLines.add(line);
                    break;
                }
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("temp.csv"))) {
            for (String date : sortedLines) {
                writer.write(date);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            new File(filename).delete();
            new File("temp.csv").renameTo(new File(filename));
        } catch (SecurityException e) {
            e.printStackTrace();
        } 

        System.out.println("sorted");
    }

// FIND comand =====================================================================================================================
    public static void find(String input) {
        String filename = "db.csv";
        int beg = 0;
        try {
            double inputPrice = Double.parseDouble(input);
            try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] lineSplit = line.split(";");
                    String filePrice = lineSplit[4];
                    double filePriceDouble = Double.parseDouble(filePrice);
                    if (filePriceDouble < inputPrice) {
                        if (beg == 0) {
                            System.out.println("------------------------------------------------------------");
                            System.out.println("ID  City                 Date         Days     Price Vehicle");
                            System.out.println("------------------------------------------------------------");
                            beg = 1;
                        }
                        String id = lineSplit[0];
                        String city = lineSplit[1];
                        String date = lineSplit[2];
                        String days = lineSplit[3];
                        String price = lineSplit[4];
                        String vehicle = lineSplit[5];
                        System.out.printf("%-4s%-21s%-11s%6s%10s%-8s%n", id, city, date, days, price, " " + vehicle);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return; 
            }
        } catch (NumberFormatException e) {
            System.out.println("wrong price");
            return;
        }
        System.out.println("------------------------------------------------------------");
    }

// AVG comand ======================================================================================================================
    public static void avg() {
        String filename = "db.csv";
        double sum = 0.0;
        double count = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineSplit = line.split(";");
                String price = lineSplit[4];
                double priceDouble = Double.parseDouble(price);
                sum += priceDouble;
                count += 1.0;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return; 
        }

        double average = sum/count;
        System.out.print("average=");
        System.out.printf("%.2f%n", average);
    }

// main ============================================================================================================================
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();
        while (!command.equals("exit")) {
            String[] splitCommand = command.split(" ", 2);
            if (splitCommand[0].equals("add")) {
                String addInput = splitCommand[1];
                add(addInput);
            } else if (splitCommand[0].equals("del")) {
                String delInput = splitCommand[1];
                del(delInput);
            } else if (splitCommand[0].equals("edit")) {
                String editInput = splitCommand[1];
                edit(editInput);
            } else if (splitCommand[0].equals("print")) {
                print();
            } else if (splitCommand[0].equals("sort")) {
                sort();
            } else if (splitCommand[0].equals("find")) {
                String findInput = splitCommand[1];
                find(findInput);
            } else if (splitCommand[0].equals("avg")) {
                avg();
            } else {
                System.out.println("wrong command");
            }
            command = sc.nextLine();
        }
        sc.close();
    }
}