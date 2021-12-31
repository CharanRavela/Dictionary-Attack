/**
 * CS645101 - Project 1
 * Question 1A
 * 
 * Charan Ravela  - CR54
 * Amruta Jagtap - ADJ2
 * Tanish Bugnait - TB29
 */

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import java.math.BigInteger;
import java.security.MessageDigest;

public class SimpleCracker {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        try {
            String passwordsPath;
            final String passDictionary = "rockyou_1.txt";

            String qa1 = Chalk.RED_BOLD + "[Enter q to quit]" + Chalk.WHITE
                    + "Proceed with the default passwords file to crack?(y/n) ";
            String input = userInput(qa1, 1);
            if (input != null) {
                if (input.equalsIgnoreCase("y")) {
                    passwordsPath = "shadow-simple";

                } else {
                    String qa2 = Chalk.RED_BOLD + "[Enter q to quit]" + Chalk.WHITE
                            + "Please enter the passwords file path:\n";
                    passwordsPath = userInput(qa2, 2);
                }

                clearScreen();
                welcomeMessage();
                simpleCrarcker(passwordsPath, passDictionary);
            } else {
                System.out.println(Chalk.RED_BOLD_BRIGHT + "Something Went Wrong!!!Please re-run the program.");
            }
        } catch (Exception e) {
            System.out.println(Chalk.RED + "ERROR:" + e.getMessage());
            e.printStackTrace();
        }
        finally{
            long endTime = System.nanoTime();
            long timeElapsed = endTime - startTime;
            System.out.println(Chalk.RED_BRIGHT + "Execution time in milliseconds: " + timeElapsed / 1000000 + " ms");
        }
    };

    protected static void simpleCrarcker(String passwords, String dictionary) {
        try {
            int matches = 0;
            final MessageDigest md = MessageDigest.getInstance("MD5");

            ArrayList<String> passwordsList = readFile(passwords);
            ArrayList<String> dictionaryList = readFile(dictionary);
            int inValid = 0;
            ArrayList<String> remove = new ArrayList<>();
            for(String str: passwordsList){

                if(!isMD5Format(str)){
                    inValid++;
                    remove.add(str);
                }

            };

            passwordsList.removeAll(remove);

            if(inValid > 0){
                System.out.println(Chalk.RED_BOLD_BRIGHT + "Ignored " + Chalk.BLUE_BOLD_BRIGHT + inValid + Chalk.RED_BOLD_BRIGHT + " non-MD5 format passwords.");
            }

            int progress = 0;
            int passwordsListCount = passwordsList.size();
            System.out.println(Chalk.WHITE + "Cracking passwords list from " + Chalk.BLUE_BOLD_BRIGHT + getFileName(passwords) + Chalk.WHITE + ". Total Passwords: " + Chalk.BLUE_BOLD_BRIGHT + passwordsListCount);
            System.out.println(Chalk.WHITE + "Dictionary being used " + Chalk.BLUE_BOLD_BRIGHT + getFileName(dictionary) + Chalk.WHITE + " contains " + Chalk.BLUE_BOLD_BRIGHT + dictionaryList.size() +  Chalk.WHITE + " passwords.\n");
            System.out.println(Chalk.WHITE + "Matches Found.....\n");

            if(passwordsList != null && dictionaryList != null){
                for (String str: passwordsList) {
                    String details[] = str.split(":");
                    String userId = details[0];
                    String userSalt = details[1];
                    String userHash = details[2];
                    String match = null;
                    System.out.println(Chalk.YELLOW + "Progress: " + ((progress * 100)/passwordsListCount) + "%");

                    for (String password: dictionaryList) {

                        byte dPass[] = (userSalt + password).getBytes();
                        String dHash = toHex(md.digest(dPass));

                        if (dHash.equals(userHash)) {
                            matches++;
                            match = password;
                            break;
                        }
                    }
                    progress++;
                    clearLine();
                    String color = Chalk.CYAN_BOLD_BRIGHT;
                    if(match != null){
                        if (matches % 2 == 0)
                        color = Chalk.PURPLE_BOLD_BRIGHT;
                        System.out.println(color + userId + ":" + match);
                    }
                };
    
                System.out.println(Chalk.WHITE + "\nTotal matches found: " + Chalk.BLUE_BOLD_BRIGHT + matches + "\n");
            }
            else{
                System.out.println(Chalk.RED_BOLD_BRIGHT + "Something Went Wrong!!!Please re-run the program.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(Chalk.RED + "ERROR:" + e.getMessage());
        }
    };

    protected static String userInput(String question, int type) {
        Scanner s = new Scanner(System.in);
        try {
            String error = "NOT A VALID INPUT";

            clearScreen();
            welcomeMessage();
            System.out.print(Chalk.WHITE_BOLD + question + Chalk.YELLOW_BOLD);
            String userResponse = s.nextLine();
            if (type == 1 && validateInput(userResponse)) {
                if (userResponse.equalsIgnoreCase("q"))
                    System.exit(0);
                return userResponse;
            } else if (type == 2 && !userResponse.equals("q") && fileExists(userResponse)) {
                if (userResponse.equalsIgnoreCase("q"))
                    System.exit(0);
                return userResponse;
            } else {
                if (userResponse.equalsIgnoreCase("q"))
                System.exit(0);
                if(type == 2)error = "NO SUCH FILE PATH FOUND";
                throw new InvalidInputException(error);
            }
        } catch (Exception e) {
            System.out.println(Chalk.RED + "\nERROR: " + e.getMessage());
            exit();
            return userInput(question, type);
        }
    }

    protected static void exit() {
        System.out.println(Chalk.RED_BOLD + "\n[Enter q to quit]" + Chalk.WHITE
                + "Press enter/return key to try different input." + Chalk.YELLOW_BOLD);
        Scanner sp = new Scanner(System.in);
        if (sp.nextLine().equalsIgnoreCase("q")){
            System.exit(0);
        }
    };

    protected static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    protected static ArrayList<String> readFile(String file) {
        ArrayList<String> list = new ArrayList<>();
        try {
            File read = new File(file);
            Scanner myReader = new Scanner(read);
        
            while (myReader.hasNextLine()) {
                list.add(myReader.nextLine());
            }
            myReader.close();
            return list;
        } catch (Exception e) {
            System.out.println(Chalk.RED + "ERROR: " + e.getMessage());
            return list;
        }
    };

    protected static void clearScreen() {
        final String CLEAR = "\033[H\033[2J";
        System.out.print(CLEAR);
        System.out.flush();
    };

    protected static void clearLine() {
        final String CLEAR = "\033[A\033[2K";
        System.out.print(CLEAR);
        System.out.flush();
    };

    protected static void welcomeMessage() {
        System.out.printf("%80s", Chalk.YELLOW_BOLD + "Welcome to Simple Cracker\n");
        System.out.printf("%103s",
                Chalk.GREEN_BOLD + "Charan Ravela - CR54   Tanish Bugnait - TB29   Amruta Jagtap - ADJ2\n\n");
    };

    protected static Boolean validateInput(String userInput) throws InvalidInputException {
        Boolean isAccepted = false;
        String acceptedInputs[] = new String[] { "y", "n", "q" };
        for (String accepted : acceptedInputs) {
            if (userInput.equalsIgnoreCase(accepted)) {
                isAccepted = true;
            }
        }
        if (isAccepted) {
            return isAccepted;
        } else {
            throw new InvalidInputException("NOT AN VALID INPUT");
        }
    };
    
    protected static Boolean isMD5Format(String str){
        if(str.length() > 8 && str.substring(5, 6).contains(":") && str.substring(14, 15).contains(":")) return true;
        return false;
    }

    protected static Boolean fileExists(String path){
        File f = new File(path);
        return f.exists() && f.isFile();
    }

    protected static String getFileName(String path){
        File f = new File(path);
        return f.getName();
    }

    protected static class InvalidInputException extends Exception {
        public InvalidInputException(String str) {
            super(str);
        }
    };
    
};