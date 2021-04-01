package authenticationsystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Scanner;

import static authenticationsystem.UserRole.*;

/**
 *
 * @author Colton Thompson <colton.thompson1@snhu.edu>
 * Date: 08/14/19
 *
 */
public class Auth {

    // The amount of login attempts remaining.
    static int loginAttempts = 3;

    // This boolean is used to determine if the user has been logged in.
    static boolean isLoggedIn = false;

    // This boolean controls if the application is running.
    static boolean isRunning = true;

    // The collection of saved user credentials that we use to compare login information with.
    static HashMap<String, User> userCredentials = new HashMap<>();

    // The currently logged in user object.
    static User currentUser;

    // Constants to remove magic numbers in the code
    public static final int MENU_CHOICE_LOG_OUT = 1;
    public static final int MENU_CHOICE_EXIT = 2;
    public static final int MENU_CHOICE_ADMIN_OPTION_ONE = 3;
    public static final int MENU_CHOICE_ADMIN_OPTION_TWO = 4;
    public static final int MENU_CHOICE_ZOO_OPTION_ONE = 5;
    public static final int MENU_CHOICE_ZOO_OPTION_TWO = 6;
    public static final int MENU_CHOICE_VET_OPTION_ONE = 7;
    public static final int MENU_CHOICE_VET_OPTION_TWO = 8;

    public static void main(String[] args) {
        // Load the credentials.txt file before we begin.
        loadSavedCredentials();

        Scanner scanner = new Scanner(System.in);

        // The choice variable is used for navigation through the application.
        int choice = -1;

        try {
            while (isRunning) {
                if (!isLoggedIn) {
                    // Print the options/commands available to the user so they know how to navigate the system.
                    System.out.println("Select an option below to continue.");
                    System.out.println("1: Log in.");
                    System.out.println("2: Exit.");
                    choice = scanner.nextInt();
                    System.out.println();

                    // I use a switch statement here to easily modify the commands for the user.
                    switch (choice) {
                        case 1: // Login

                            System.out.println("Username: ");
                            String username = scanner.next(); // scanner.next() is used to prevent the system from skipping passed the username field.

                            System.out.println("Password: ");
                            String password = scanner.next();
                            String hash = getHashForString(password);

                            if (validateCredentials(username, hash)) {
                                currentUser = userCredentials.get(username);
                                isLoggedIn = true;
                            } else {
                                if (loginAttempts > 0) {
                                    System.out.println("Error: Incorrect username or password!  Please try again.  You have " + loginAttempts + " remaining.");
                                    loginAttempts -= 1;
                                } else {
                                    System.out.println("You have exceeded your amount of login attempts!  Exiting program in 5 seconds.");
                                    isRunning = false;
                                    new Thread(() -> {
                                        try {
                                            Thread.sleep(5000l);
                                        } catch (InterruptedException e) {
                                        }
                                        System.exit(0);
                                    }).start();
                                }
                            }

                            break;
                        case 2: // Exit application
                            System.out.println("Exiting...");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("This option isn't available, try again!");
                    }
                } else {
                    displayMenuForUser(currentUser); // ADDED FOR CS-499 - MILESTONE TWO

                    choice = scanner.nextInt();
                    System.out.println();

                    handleMenuChoiceForUser(currentUser, choice); // ADDED FOR CS-499 - MILESTONE TWO
                }
            }

        } catch (NoSuchAlgorithmException nsa) {
        }
    }

    /**
     * Displays a menu of options based on the user's role.
     * @param user
     *      The user parameter is passed from the authentication method.
     */
    public static void displayMenuForUser(User user) { // ADDED FOR CS-499 MILESTONE TWO
        if (user == null) return;
        System.out.println();
        System.out.println("Logged in as " + user.getUsername());
        System.out.println("Role: " + user.getRole().toString()); // ADDED FOR CS-499 - MILESTONE TWO
        System.out.println();
        System.out.println("Select an option below to continue.");
        System.out.println("1. Log out.");
        System.out.println("2. Exit application (Log out & exit).");
        switch (user.getRole()) {
            case admin:
                System.out.println(MENU_CHOICE_ADMIN_OPTION_ONE + ". Admin Option 1");
                System.out.println(MENU_CHOICE_ADMIN_OPTION_TWO + ". Admin Option 2");
                break;
            case zookeeper:
                System.out.println(MENU_CHOICE_ZOO_OPTION_ONE + ". ZooKeeper Option 1");
                System.out.println(MENU_CHOICE_ZOO_OPTION_TWO + ". ZooKeeper Option 2");
                break;
            case veterinarian:
                System.out.println(MENU_CHOICE_VET_OPTION_ONE + ". Veterinarian Option 1");
                System.out.println(MENU_CHOICE_VET_OPTION_TWO + ". Veterinarian Option 2");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + user.getRole());
        }
    }

    /**
     * Handles the logic for all menu choices
     * @param user
     *  The user that is currently logged in
     * @param choice
     *  The user's menu choice
     */
    public static void handleMenuChoiceForUser(User user, int choice) { // ADDED FOR CS-499 MILESTONE TWO
        if (user == null) return;

        if (choice == MENU_CHOICE_LOG_OUT) {
            currentUser = null;
            isLoggedIn = false;
            return;
        } else if (choice == MENU_CHOICE_EXIT) {
            currentUser = null;
            isLoggedIn = false;
            isRunning = false;
            System.exit(0);
        } else if (choice == MENU_CHOICE_ADMIN_OPTION_ONE) {
            // TODO: Handle logic for this option
            if (!user.getRole().equals(admin)) {
                System.out.println("You do not have permission to use this option, please try again!");
                return;
            }
        } else if (choice == MENU_CHOICE_ADMIN_OPTION_TWO) {
            // TODO: Handle logic for this option
            if (!user.getRole().equals(admin)) {
                System.out.println("You do not have permission to use this option, please try again!");
                return;
            }
        } else if (choice == MENU_CHOICE_ZOO_OPTION_ONE) {
            // TODO: Handle logic for this option
            if (!user.getRole().equals(zookeeper) && !user.getRole().equals(admin)) { // Admin role would override
                System.out.println("You do not have permission to use this option, please try again!");
                return;
            }
        } else if (choice == MENU_CHOICE_ZOO_OPTION_TWO) {
            // TODO: Handle logic for this option
            if (!user.getRole().equals(zookeeper) && !user.getRole().equals(admin)) { // Admin role would override
                System.out.println("You do not have permission to use this option, please try again!");
                return;
            }
        } else if (choice == MENU_CHOICE_VET_OPTION_ONE) {
            // TODO: Handle logic for this option
            if (!user.getRole().equals(veterinarian) && !user.getRole().equals(admin)) { // Admin role would override
                System.out.println("You do not have permission to use this option, please try again!");
                return;
            }
        } else if (choice == MENU_CHOICE_VET_OPTION_TWO) {
            // TODO: Handle logic for this option
            if (!user.getRole().equals(veterinarian) && !user.getRole().equals(admin)) { // Admin role would override
                System.out.println("You do not have permission to use this option, please try again!");
                return;
            }
        }


    }

    /**
     * Compares the credentials to the current users input to validate the
     * login.
     *
     * @param username The username of the current user.
     * @param inputHash The hash that was input during the login process.
     * @return True if the login should be successful, false for not.
     */
    public static boolean validateCredentials(String username, String inputHash) {
        User user = userCredentials.get(username);
        if (user == null) {
            System.out.println("User doesn't exist!");
            return false;
        }
        String userHash = user.getHash();
        // Compare the hash of the saved credentials to the input of the current user to validate the login.
        return inputHash.equals(userHash);
    }

    /**
     * Loads the saved credentials from the credentials.txt file provided for
     * this assignment.
     */
    public static void loadSavedCredentials() {
        File file = new File("credentials.txt");
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));

            String line = null;
            // While the file has data to read, continue reading.
            while ((line = reader.readLine()) != null) {
                String[] array = line.split("\\t"); // Splits the data based on tabs.
                String username = array[0];
                String hash = array[1];
                String password = array[2].replace("\"", "");
                String role = array[3];
                User user = new User(username, hash, password, valueOf(role));
                userCredentials.put(username, user);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This takes a string and creates a hash based on the input.
     *
     * @param input This is the input string that we use to generate the hash.
     * @return The hashed string.
     * @throws NoSuchAlgorithmException
     */
    public static String getHashForString(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

} // End of Auth class
