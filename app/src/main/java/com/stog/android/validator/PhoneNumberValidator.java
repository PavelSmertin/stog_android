package com.stog.android.validator;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

public class PhoneNumberValidator implements MaskedValidator {

    final static int NUMBER_SIZE = 10;

    private String numberString;

    final static int[] numberMask = { 2, 6, 10, 13 };


    public PhoneNumberValidator() {
    }

    @Override
    public void afterTextChanged(Editable source) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public boolean hasFullLength() {
        if (TextUtils.isEmpty(numberString)) {
            return false;
        }

        return (numberString.length() == NUMBER_SIZE);
    }

    @Override
    public boolean isValid() {
        if (!this.hasFullLength()) {
            return false;
        }
        return true;
    }

    @Override
    public String getValue() {
        return numberString;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,  int dend) {

        end = (end > NUMBER_SIZE+1) ? NUMBER_SIZE+1 : end;
        source = source.subSequence(start, end);

        String updated = new SpannableStringBuilder(dest).replace(dstart, dend, source, start, end).toString();
        String updatedDigits = StringHelper.getDigitsOnlyString(updated);

        if (updatedDigits.length() > NUMBER_SIZE+1) {
            return "";
        }

        SpannableStringBuilder result = new SpannableStringBuilder(source);

        if (dstart == 0  && result.length() > 0 && (result.charAt(0) == '7')) {
            result.insert(0, "+");
            end++;
        }

        if (dstart == 0  && result.length() > 0 && (result.charAt(0) != '7' && result.charAt(0) != '+')) {
            result.insert(0, "+7");
            end++;
        }


        if(updatedDigits.length() > 0){
            numberString = updatedDigits.substring(1, updatedDigits.length());
        }

        int replen = dend - dstart;

        for (int spacer : numberMask) {
            if (dstart - replen <= spacer && dstart + end - replen >= spacer) {
                int loc = spacer - dstart;
                if (loc == end || (0 <= loc && loc < end && result.charAt(loc) != ' ')) {
                    result.insert(loc, " ");
                    end++;
                }
            }
        }

        return result;
    }
}
