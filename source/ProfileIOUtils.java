import java.io.*;
import java.util.*;

/**
 * Utilities for reading and writing profile files.
 *
 * @author Victor Cai
 * @version 2.0
 */
public class ProfileIOUtils {
    /**
     * Reads a list of profiles from a file.
     * @param filename Name fo the file to read from
     * @return A list of profiles.
     */
    public static ArrayList<Profile> readProfiles(String filename) {
        Scanner in = null;
        try {
            File profileFile = new File(filename);
            String absolutePath = profileFile.getAbsolutePath();
            File absProfileFile = new File(absolutePath);
            in = new Scanner(absProfileFile);
        } catch (FileNotFoundException e) {
            System.out.println("ProfileIOUtils.readProfiles: Cannot open file for reading: " + filename);
            System.exit(-1);
        }

        ArrayList<Profile> profiles = new ArrayList<>();
        while (in.hasNextLine()) {
            String line = in.nextLine();
            Profile profile = readProfile(line);
            profiles.add(profile);
        }

        in.close();
        return profiles;
    }

    /**
     * Saves a list of profiles to a file called "profiles.txt".
     *
     * @param profiles The {@link ArrayList} containing the profiles.
     */
    public static void saveProfiles(ArrayList<Profile> profiles) {
        final String PROFILES_FILE_NAME = "profiles.txt";
        File outFile = new File(PROFILES_FILE_NAME);

        try {
            outFile.createNewFile();
        } catch (IOException e) {
            System.out.println("ProfileIOUtils.saveProfiles: Failed to create file: " + PROFILES_FILE_NAME);
            System.exit(-2);
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(outFile);
        } catch (FileNotFoundException e) {
            System.out.println("ProfileIOUtils.saveProfiles: Cannot find file: " + PROFILES_FILE_NAME);
            System.exit(-2);
        }

        for (Profile profile : profiles) {
            out.println(profile);
        }

        out.close();
    }

    /**
     * Finds a profile with the given name in a file.
     *
     * @param filename name of the file
     * @param profileName name of the profile to search for
     *
     * @return The requested profile
     */
    public static Profile findProfileInFile(String filename, String profileName) {
        ArrayList<Profile> list = readProfiles(filename);
        Profile profile = findProfileInList(list, profileName);

        return profile;
    }

    /**
     * Finds a profile with the given name in a list.
     *
     * @param list The list to look through
     * @param name The name of the profile to search for
     *
     * @return the profile requested
     *
     * @throws IllegalArgumentException if the profile doesn't exist.
     */
    public static Profile findProfileInList(ArrayList<Profile> list, String name) {
        Profile profile = list.stream()
                .filter(p -> p.getName().equals(name))
                .findAny()
                .orElse(null);
        if (profile == null) {
            String errorMsg = String.format("Profile named \"%s\" does not exist", name);
            throw new IllegalArgumentException(errorMsg);
        }

        return profile;
    }

    /**
     * Creates a single profile from a comma-separated line of text
     *
     * @param line The line specifying the profile
     *
     * @return The profile constructed
     */
    private static Profile readProfile(String line) {
        Scanner lineReader = new Scanner(line);
        lineReader.useDelimiter(",");

        String name = lineReader.next();
        int highestLevel = lineReader.nextInt();
        int playerVariant = lineReader.nextInt();

        Profile profile = new Profile(name, highestLevel, playerVariant);
        lineReader.close();
        return profile;
    }

}
