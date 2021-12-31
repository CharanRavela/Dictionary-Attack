
/**
 * CS645101 - Project 1
 * Question 1B
 * 
 * Charan Ravela  - CR54
 * Amruta Jagtap - ADJ2
 * Tanish Bugnait - TB29
 */

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class Cracker {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        try {
            String passwordsPath;
            final String passDictionary = "common-passwords.txt";

            String qa1 = Chalk.RED_BOLD + "[Enter q to quit]" + Chalk.WHITE
                    + "Proceed with the default passwords file to crack?(y/n) ";
            String input = userInput(qa1, 1);
            if (input != null) {
                if (input.equalsIgnoreCase("y")) {
                    passwordsPath = "shadow";

                } else {
                    String qa2 = Chalk.RED_BOLD + "[Enter q to quit]" + Chalk.WHITE
                            + "Please enter the passwords file path:\n";
                    passwordsPath = userInput(qa2, 2);
                }

                clearScreen();
                welcomeMessage();
                Crarcker(passwordsPath, passDictionary);
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

    protected static void Crarcker(String passwords, String dictionary) {
        try {
            int matches = 0;

            ArrayList<String> passwordsList = readFile(passwords);
            ArrayList<String> dictionaryList = readFile(dictionary);
            int inValid = 0;

            ArrayList<String> remove = new ArrayList<>();
            for(String str: passwordsList){

                if(!isMD5cryptFormat(str)){
                    inValid++;
                    remove.add(str);
                }

            };

            passwordsList.removeAll(remove);

            if(inValid > 0){
                System.out.println(Chalk.RED_BOLD_BRIGHT + "Ignored " + Chalk.BLUE_BOLD_BRIGHT + inValid + Chalk.RED_BOLD_BRIGHT + " non-MD5Crypt format passwords.");
            }

            
            System.out.println(Chalk.WHITE + "Cracking passwords list from " + Chalk.BLUE_BOLD_BRIGHT + getFileName(passwords) + Chalk.WHITE + ". Total Passwords: " + Chalk.BLUE_BOLD_BRIGHT + passwordsList.size());
            System.out.println(Chalk.WHITE + "Dictionary being used " + Chalk.BLUE_BOLD_BRIGHT + getFileName(dictionary) + Chalk.WHITE + " contains " + Chalk.BLUE_BOLD_BRIGHT + dictionaryList.size() +  Chalk.WHITE + " passwords.\n");
            System.out.println(Chalk.WHITE + "Matches Found.....\n");

            if(passwordsList != null && dictionaryList != null){
                for (String str : passwordsList) {
                    String shadow[] = str.split(":");
                    String userId = shadow[0];
                    String userCredentials[] = shadow[1].split("\\$");
                    String userSalt = userCredentials[2];
                    String userHash = userCredentials[3];
    
                    for (String password: dictionaryList) {

                        String color = Chalk.CYAN_BOLD_BRIGHT;
                        if(password.length() < 16){
                            String dHash = MD5Shadow.crypt(password, userSalt);
    
                            if (dHash.equals(userHash)) {
                                matches++;
                                if (matches % 2 == 0)color = Chalk.PURPLE_BOLD_BRIGHT;
                                System.out.println(color + userId + ":" + password);
                                break;
                            }
                        }
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
            System.out.println(Chalk.RED + "ERROR: " + e.getMessage());
            exit();
            return userInput(question, type);
        }
    }

    protected static void exit() {
        System.out.println(Chalk.RED_BOLD + "\n[Enter q to quit]" + Chalk.WHITE
                + "Press enter/return key to try different input." + Chalk.YELLOW_BOLD);
        Scanner sp = new Scanner(System.in);
        if (sp.nextLine().equalsIgnoreCase("q")) 
        {
            System.exit(0);
        }
    };

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
            e.printStackTrace();
            return list;
        }
    };

    protected static Boolean isMD5cryptFormat(String str){
        if(str.length() > 8 && str.substring(5, 9).contains(":$1$")) return true;
        return false;
    }

    protected static void clearScreen() {
        final String CLEAR = "\033[H\033[2J";
        System.out.print(CLEAR);
        System.out.flush();
    };

    protected static void welcomeMessage() {
        System.out.printf("%80s", Chalk.YELLOW_BOLD + "Welcome to Cracker\n");
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