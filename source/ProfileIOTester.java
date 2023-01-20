import java.util.*;

public class ProfileIOTester {
    public static void main(String[] args) {
        Profile testProfile1 = new Profile("testing", 2, 1);
        Profile testProfile2 = new Profile("John", 3, 1);
        Profile testProfile3 = new Profile("Someone else", 4, 2);
        Profile testProfile4 = new Profile("Yet another user", 1, 2);

        ArrayList<Profile> profiles = new ArrayList<>();
        profiles.add(testProfile1);
        profiles.add(testProfile2);
        profiles.add(testProfile3);
        profiles.add(testProfile4);

        ProfileIOUtils.saveProfiles(profiles);

        ArrayList<Profile> savedProfiles = ProfileIOUtils.readProfiles("profiles.txt");
        for (Profile p : savedProfiles) {
            System.out.println(p);
        }
    }
}
