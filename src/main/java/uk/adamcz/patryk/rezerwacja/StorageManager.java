package uk.adamcz.patryk.rezerwacja;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StorageManager {
    private static final String credentialsStoreFilename = "credentials.dat";

    private static final File credentialsStore = new File(credentialsStoreFilename);

    private static final String locationStoreFilename = "locations.dat";

    private static final File locationStore = new File(locationStoreFilename);

    public static boolean MatchCredentials(String preparedLogin, String passwordHash){
        if (!credentialsStore.exists())
            return false;
        try {
            Scanner scanner = new Scanner(credentialsStore);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if(line.equals("l: "+preparedLogin))
                    if (scanner.hasNextLine() && scanner.nextLine().equals("p: "+passwordHash)) {
                        return true;
                    }
            }
        } catch(Exception ignored) {}
        return false;
    }

    public static boolean TryAddCredentials(String preparedLogin, String passwordHash){
        PrintWriter writer;
        try{
            if ( credentialsStore.exists() && !credentialsStore.isDirectory() )
                writer = new PrintWriter(new FileOutputStream(credentialsStore, true));
            else
                writer = new PrintWriter(credentialsStoreFilename);

            try {
                Scanner scanner = new Scanner(new File(credentialsStoreFilename));

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();

                    if(line.equals("l: "+preparedLogin)) {
                        return false;
                    }
                }
            } catch(Exception ignored) { return false; }

            writer.append("l: "+preparedLogin+"\n");
            writer.append("p: "+passwordHash+"\n");
            writer.close();

            return true;

        } catch (Exception ignored) {}
        return false;
    }

    public static List<String> ListLocations(){
        if (!credentialsStore.exists())
            return null;
        List<String> result = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(locationStore);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                result.add( line );
            }
        } catch(Exception ignored) {}
        return result;
    }

    public static boolean TryAddLocation(String locationName){
        String preparedLocationName = StringInputUtils.Escape(locationName);

        PrintWriter writer;
        try{
            if ( locationStore.exists() && !locationStore.isDirectory() )
                writer = new PrintWriter(new FileOutputStream(locationStore, true));
            else
                writer = new PrintWriter(locationStoreFilename);

            try {
                Scanner scanner = new Scanner(new File(locationStoreFilename));

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();

                    if(line.equals(preparedLocationName)) {
                        return false;
                    }
                }
            } catch(Exception ignored) { return false; }

            writer.append(preparedLocationName+"\n");
            writer.close();

            return true;

        } catch (Exception ignored) {}
        return false;
    }
}
