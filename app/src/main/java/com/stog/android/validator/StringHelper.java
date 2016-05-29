package com.stog.android.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class StringHelper {
    public static String getDigitsOnlyString(String numString) {
        StringBuilder sb = new StringBuilder();
        for (char c : numString.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public static Date getDateForString(String dateString) {
        String digitsOnly = getDigitsOnlyString(dateString);
        SimpleDateFormat validDate = getDateFormatForLength(digitsOnly.length());
        if (validDate != null) {
            try {
                validDate.setLenient(false);
                Date date = validDate.parse(digitsOnly);
                return date;
            } catch (ParseException pe) {
                return null;
            }
        }
        return null;
    }

    public static SimpleDateFormat getDateFormatForLength(int len) {
        if (len == 4) {
            return new SimpleDateFormat("MMyy", Locale.getDefault());
        } else if (len == 6) {
            return new SimpleDateFormat("MMyyyy", Locale.getDefault());
        } else if (len == 8) {
            return new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        } else {
            return null;
        }
    }
}
