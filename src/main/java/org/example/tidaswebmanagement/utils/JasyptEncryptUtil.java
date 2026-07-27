package org.example.tidaswebmanagement.utils;

import java.io.*;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.salt.RandomSaltGenerator;
import org.jasypt.iv.RandomIvGenerator;

/**
 * Jasypt encryption tool — matches jasypt-spring-boot-starter 3.0.5 defaults.
 * Reads passwords from encrypt-input.txt (one per line), writes ENC() values to stdout.
 */
public class JasyptEncryptUtil {
    public static void main(String[] args) throws Exception {
        String password = System.getenv("JASYPT_PASSWORD");
        if (password == null || password.isEmpty()) {
            password = "tidas2024secret";
        }

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        encryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        encryptor.setKeyObtentionIterations(1000);
        encryptor.setSaltGenerator(new RandomSaltGenerator());
        encryptor.setIvGenerator(new RandomIvGenerator());

        // Read from stdin or args
        if (args.length > 0) {
            for (String plain : args) {
                System.out.println(plain + " -> ENC(" + encryptor.encrypt(plain) + ")");
            }
        } else {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    System.out.println(line + " -> ENC(" + encryptor.encrypt(line) + ")");
                }
            }
        }
    }
}
