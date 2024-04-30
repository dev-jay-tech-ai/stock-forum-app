package dev.be.serviceuser.utils;

import org.springframework.stereotype.Component;

import java.util.function.Predicate;

@Component
public class EmailValidatorUtils implements Predicate<String> {
    @Override
    public boolean test(String s) {
//        TODO: Regex to validate email
        return true;
    }
}
