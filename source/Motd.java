import java.io.*;
import java.net.*;
import java.nio.charset.*;
import java.util.stream.*;



/**
 * This class represents a String 'MOTD' to be displayed frequently.
 * @author James Norman
 * @version 1.2
 *
 */
public class Motd {

    private static final String API_SITE = "http://cswebcat.swan.ac.uk/puzzle";
    private static String message;

    /**
     * Gets the current message of the day.
     * @return The current message of the day.
     */
    public static String getMessage() {
        return message;
    }

    /**
   * Sends a get request and is responsible for handling http status codes.
   * Retrieves the cipher to be sent to 'solve'.
     * @throws IOException to catch i/o exception possible in status checks
     * @throws InterruptedException to prevent the thread from hanging looking for error
     */

    public static void updateMessage() throws IOException, InterruptedException {



        URL url = new  URL(API_SITE);

        try {

            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("GET");

            //status represents http status codes

            int status = connect.getResponseCode();

            //debug variable
            // int info = connect.getReadTimeout();

            //Catching types of http status errors

            if (status >= 500 && status <= 599) {

                System.out.println("Server error, contact Admin (HTTP: " + status + ")");
                connect.disconnect();

            } else if (status >= 300 && status <= 399) {

                System.out.println("Redirect, please check connection (HTTP: " + status + ")");
                connect.disconnect();

            }


            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            //collector to recompile the buffered reader back into a string
            String body = in.lines().collect(Collectors.joining());

            //un-comment this for Debug (cipher retrieved)
            //System.out.println("@Timeout: " + info + "| [Initial cipher:] " + body);

            solve(body);


            //un-comment this for Debug(HTTP status)
            //System.out.println("@Timeout: " + info + "| [Status OK:] " + status);
            connect.disconnect();


        } catch (UnknownHostException e) {

            System.out.println("Client Error, please check connection");

        }

    }


    /**
     * Responsible to solve the cipher given.
     *
     * @param body contains the unsolved string still ciphered
     *
     * @throws IOException to catch error when passing the correct URL to post
     */

    private static String solve(String body) throws IOException {

        int i;

        //simple booleans for the length of the for loop to determine which way the cipher is working
        boolean isforwardCiph = true;
        boolean isbackwardCiph = false;

        //the current string of solved characters
        String solvedKey = "";

        //char array to handle each character using array index
        char[] keyArray = body.toCharArray();

        for (i = 0; i < keyArray.length; i++) {

            if (isforwardCiph && keyArray[i] == 'Z') {

                solvedKey += "A";
            } else if (isbackwardCiph && keyArray[i] == 'A') {

                solvedKey += "Z";
            } else if (isforwardCiph && (keyArray[i] != 'A' || keyArray[i] != 'Z')) {

                char temp = keyArray[i];
                temp += 1;
                solvedKey += temp;

            } else {

                char temp = keyArray[i];
                temp -= 1;
                solvedKey += temp;

            }

            //booleans reversed ready for next char
            isforwardCiph = !isforwardCiph;
            isbackwardCiph = !isbackwardCiph;

        }
        //un-comment for Debug (Posted key to url)
        //System.out.println("[The solved key:] " + solvedKey);

        sendPost(solvedKey);
        return solvedKey;
    }

    /**
     * Reads the text and displays it from the given solved website key.
     *
     * @param solvedKey contains the solved suffix of the URL
     *
     * @throws IOException handles errors with the new 'BufferedReader' created
     */

    private static void sendPost(String solvedKey) throws IOException  {

        String line;

        URL urlSent = new URL("http://cswebcat.swan.ac.uk/message?solution=" + solvedKey);


        //"UTF8" used for encoding to read the string with correct characters
        BufferedReader in = new BufferedReader(new InputStreamReader(urlSent.openStream(), StandardCharsets.UTF_8));

        while ((line = in.readLine()) != null) {

            line = new String(line.getBytes());
            message = (line);

        }

        in.close();
    }
}
