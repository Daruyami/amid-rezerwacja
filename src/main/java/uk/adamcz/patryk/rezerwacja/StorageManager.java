package uk.adamcz.patryk.rezerwacja;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StorageManager {
    private static final String credentialsStoreFilename = "credentials.dat";

    private static final File credentialsStore = new File(credentialsStoreFilename);

    private static final String locationStoreFilename = "locations.dat";

    private static final File locationStore = new File(locationStoreFilename);

    public static Identity MatchCredentials(String preparedLogin, String passwordHash){
        if (!credentialsStore.exists())
            return new Identity();
        try {
            Scanner scanner = new Scanner(credentialsStore);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if( line.equals("l: "+preparedLogin) ) {
                    String username;
                    String role;

                    if ( scanner.hasNextLine() && scanner.nextLine().equals("p: "+passwordHash) ) {

                        if ( scanner.hasNextLine() ) {
                            username = scanner.nextLine();

                            if ( username.startsWith("u: ") ){
                                username = username.substring(3);

                                if ( scanner.hasNextLine() ) {
                                    role = scanner.nextLine();

                                    if ( role.startsWith("r: ") )
                                        role = role.substring(3).trim();

                                    else throw new Exception("Invalid database format!");
                                } else throw new Exception("Invalid database format!");
                            } else throw new Exception("Invalid database format!");
                        } else throw new Exception("Invalid database format!");

                        return new Identity(true, role, username);
                    }
                }
            }
        } catch(Exception ignored) {}
        return new Identity();
    }

    public static Identity TryAddCredentials(String preparedLogin, String passwordHash){
        try{
            PrintWriter writer;
            boolean initFile = false;

            if ( credentialsStore.exists() && !credentialsStore.isDirectory() )
                writer = new PrintWriter(new FileOutputStream(credentialsStore, true));
            else {
                writer = new PrintWriter(credentialsStoreFilename);
                initFile = true;
            }

            Scanner scanner = new Scanner(new File(credentialsStoreFilename));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if(line.equals("l: "+preparedLogin)) {
                    return new Identity();
                }
            }

            writer.append("l: "+preparedLogin+"\n");
            writer.append("p: "+passwordHash+"\n");
            if (!initFile) {
                writer.append("u: Guest\n");
                writer.append("r: "+UserRole.Guest.toString()+"\n");

                writer.close();

                return new Identity(true, "Guest", UserRole.Guest.toString());

            }
            else {
                writer.append("u: Administrator\n");
                writer.append("r: "+UserRole.Administrator.toString()+"\n");

                writer.close();

                return new Identity(true, "Administrator", UserRole.Administrator.toString());

            }
        } catch (Exception ignored) { return new Identity(); }
    }

    public static boolean TryUpdateUser(String login, String username, UserRole role){
        String preparedLogin = StringInputUtils.Escape(login);
        String preparedUsername = StringInputUtils.Escape(username);
        try{
            if ( !credentialsStore.exists() || credentialsStore.isDirectory() )
                return false;

            File temp = new File(".credentials.tmp");
            PrintWriter writer = new PrintWriter(new FileWriter(temp));

            Scanner scanner = new Scanner(new File(credentialsStoreFilename));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if( line.equals("l: "+preparedLogin) ) {
                    writer.println(line);

                    if ( scanner.hasNextLine() ) {
                        line = scanner.nextLine();

                        if ( line.startsWith("p: ") ) {
                            writer.println(line);

                            if ( scanner.hasNextLine() ) {
                                line = scanner.nextLine();

                                if ( line.startsWith("u: ") ) {
                                    writer.println("u: " + preparedUsername);

                                    if ( scanner.hasNextLine() ) {
                                        line = scanner.nextLine();

                                        if ( line.startsWith("r: ") )
                                            writer.println("r: " + role.toString());

                                        else throw new Exception("Invalid database format!");
                                    } else throw new Exception("Invalid database format!");
                                }
                                else throw new Exception("Invalid database format!");
                            } else throw new Exception("Invalid database format!");
                        }
                        else throw new Exception("Invalid database format!");
                    } else throw new Exception("Invalid database format!");
                }
                else {
                    writer.println(line);
                }
            }

            writer.flush();
            writer.close();

            if (!temp.renameTo(credentialsStore))
                System.out.println("Failed to save the changes!");

            return true;
        } catch (Exception ignored){ return false; }
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

            Scanner scanner = new Scanner(new File(locationStoreFilename));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if(line.equals(preparedLocationName)) {
                    return false;
                }
            }

            writer.append(preparedLocationName+"\n");
            writer.close();

            return true;
        } catch (Exception ignored) {return false; }
    }

    public static boolean TryRemoveLocation(String locationName){
        String preparedLocationName = StringInputUtils.Escape(locationName);
        try{
            File temp = new File(".locations.tmp");
            PrintWriter out = new PrintWriter(new FileWriter(temp));
            Files.lines(locationStore.toPath())
                    .filter(line -> !line.contains(preparedLocationName))
                    .forEach(out::println);
            out.flush();
            out.close();
            if (!temp.renameTo(locationStore))
                System.out.println("Failed to save the changes!");
            return true;
        } catch (Exception ignored){ return false; }
    }
}
