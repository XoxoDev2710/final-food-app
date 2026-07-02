package com.dev.util;

import java.util.regex.Pattern;

public final class ValidationUtil {


    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z]([._-](?![._-])|[A-Za-z0-9]){2,18}[A-Za-z0-9]$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^.{6,}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z .'-]{2,39}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9][0-9]{9}$");
    private static final Pattern UPI_ID_PATTERN = Pattern.compile("^[A-Za-z0-9._-]{2,30}@[A-Za-z]{2,20}$");
    private static final Pattern UPI_PIN_PATTERN = Pattern.compile("^\\d{4,6}$");
    private static final Pattern PRICE_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");

    private ValidationUtil()
    {
    }

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidUpiId(String upiId) {
        return upiId != null && UPI_ID_PATTERN.matcher(upiId.trim()).matches();
    }

    public static boolean isValidUpiPin(String pin) {
        return pin != null && UPI_PIN_PATTERN.matcher(pin.trim()).matches();
    }

    public static boolean isValidPrice(String price) {
        return price != null && PRICE_PATTERN.matcher(price.trim()).matches();
    }

    public static boolean isNonEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidAddress(String address) {
        return isNonEmpty(address) && address.trim().length() >= 10;
    }
}