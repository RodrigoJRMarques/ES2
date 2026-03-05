package com.es2.decorator;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String username = args.length > 0 ? args[0] : "admin";
        String password = args.length > 1 ? args[1] : "admin";
        boolean useCommonWordsValidator = args.length > 2 && Boolean.parseBoolean(args[2]);

        AuthInterface auth = useCommonWordsValidator
            ? new Logging(new CommonWordsValidator(new Auth()))
            : new Logging(new Auth());

        try {
            auth.auth(username, password);
            System.out.println("Authentication successful");
        } catch (AuthException e) {
            System.out.println("Authentication failed");
        } catch (IOException e) {
            System.out.println("I/O error during authentication");
        }
    }
}
