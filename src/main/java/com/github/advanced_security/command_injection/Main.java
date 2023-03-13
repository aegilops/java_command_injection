package com.github.advanced_security.command_injection;

/* Principles of command injection.
 * 
 * DO NOT use this as a test case for demonstrating static analysis tools - the commands are hardcoded strings, which should not be flagged as a problem.
 * 
 * This is purely to demonstrate how Java handles running processes and passing arguments to Runtime.exec().
 * 
 * (C) Copyright GitHub, 2033
 * 
 */

import java.util.stream.Stream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("Command injection demo");

        try {
            // 1. Bad: user input is used directly
            String[] args_1 = {"/bin/sh", "-c", "echo case 1"};
            System.out.println("1");
            run(args_1);

            // 2. Bad: user input is used as input to "sh"
            String[] args_2 = {"-c", "echo case 2"};
            System.out.println("2");
            run(
                Stream.concat(
                    Arrays.stream(new String[]{"/bin/sh"}),
                    Arrays.stream(args_2)
                ).toArray(String[]::new)
            );

            // 3. OK: user input is used as input to "cat", which won't execute it
            String[] args_3 = {"foo | rm foo"};
            System.out.println("3");
            run(
                Stream.concat(
                    Arrays.stream(new String[]{"/bin/cat"}),
                    Arrays.stream(args_3)
                ).toArray(String[]::new)
            );

            // 4. OK, but broken: takes envp array as second arg
            String[] envp_4 = {"whatever=this_is_not_an_argument"};
            System.out.println("4");
            run(
                "/bin/chmod 777",
                envp_4
            );

            // 5. NO: doesn't compile, there is no overload for exec(String, String)
            // String user_input_target_file_name = "foo | rm foo"
            // System.out.println("5");
            // run(
            //     "/bin/chmod 777",
            //     user_input_target_file_name
            // );

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    // first arg is command array
    private static void run(String[] args) throws IOException {
        System.out.println("Running: " + args[0] + " \"" + String.join("\" \"", Arrays.copyOfRange(args, 1, args.length)) + '"');
        Process proc = Runtime.getRuntime().exec(args);
        printOutput(proc);
    }

    // first arg is command, second arg is envp
    private static void run(String cmd, String[] envp) throws IOException {
        System.out.println("Running: " + cmd + " with envp of \"" + String.join("\" \"", envp) + '"');
        Process proc = Runtime.getRuntime().exec(
            cmd,    // command string - deprecated in Java 18
            envp    // envp, not args
        );
        printOutput(proc);
    }

    private static void printOutput(Process proc) throws IOException {
        BufferedReader stdInput = new BufferedReader(new 
        InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new 
                InputStreamReader(proc.getErrorStream()));

        // Read the output from the command
        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

        // Read any errors from the attempted command
        while ((s = stdError.readLine()) != null) {
            System.err.println(s);
        }
    }
}
