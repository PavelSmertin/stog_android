package com.stog.android.validator;


import android.text.Editable;
import android.text.Spanned;

public class FixedLengthValidator implements MaskedValidator {

    public int requiredLength;
    private String value;

    public FixedLengthValidator(int length) {
        requiredLength = length;
    }

    @Override
    public void afterTextChanged(Editable s) {
        value = s.toString();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean hasFullLength() {
        return this.isValid();
    }

    @Override
    public boolean isValid() {
        return value == null || (value.length() == requiredLength || value.length() == 0);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
        if (end > 0 && dest.length() + dend - dstart + end > requiredLength) {
            return "";
        } else {
            return null;
        }
    }

}
