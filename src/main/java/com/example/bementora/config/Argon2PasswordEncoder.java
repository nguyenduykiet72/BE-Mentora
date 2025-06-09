package com.example.bementora.config;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class Argon2PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        char[] passwordChars = extractChars(rawPassword);
        Argon2 argon2 = null;
        try {
            argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 16, 32);
            return argon2.hash(4, 15 * 1024, 4, passwordChars);
        } finally {
            wipeArray(passwordChars);
            if (argon2 != null) {
                try {
                    argon2.getClass().getMethod("close").invoke(argon2);
                } catch (Exception e) {
                    // Ignore - method doesn't exist or can't be called
                }
            }
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        char[] passwordChars = extractChars(rawPassword);
        Argon2 argon2 = null;
        try {
            argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id, 16, 32);
            return argon2.verify(encodedPassword, passwordChars);
        } finally {
            wipeArray(passwordChars);
            if (argon2 != null) {
                try {
                    argon2.getClass().getMethod("close").invoke(argon2);
                } catch (Exception e) {
                    // Ignore - method doesn't exist or can't be called
                }
            }
        }
    }

    private char[] extractChars(CharSequence cs) {
        if (cs instanceof String) {
            return ((String) cs).toCharArray();
        } else if (cs instanceof StringBuilder || cs instanceof StringBuffer) {
            final int length = cs.length();
            final char[] chars = new char[length];
            for (int i = 0; i < length; i++) {
                chars[i] = cs.charAt(i);
            }
            return chars;
        } else {
            return cs.toString().toCharArray();
        }
    }

    private void wipeArray(char[] array) {
        if (array != null) {
            Arrays.fill(array, '\0');
        }
    }
}
