package com.stog.android.validator;

import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LatinInputFilter implements MaskedValidator {

    private String value;
    private EditTextCheckValidListener editTextCheckValidListener;

    public LatinInputFilter() {
    }

    public LatinInputFilter(EditTextCheckValidListener editTextCheckValidListener) {
        this.editTextCheckValidListener = editTextCheckValidListener;
    }

    @Override
    public void afterTextChanged(Editable source) {
        this.value = source.toString().trim();
        if(editTextCheckValidListener != null) {
            editTextCheckValidListener.OnCheckField(isValid());
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public boolean hasFullLength() {
        return !TextUtils.isEmpty(value);
    }

    @Override
    public boolean isValid() {
        if(value == null) {
            return false;
        }
        Pattern pattern =   Pattern.compile("^[a-zA-Z0-9\\s]+$");
        Matcher m = pattern.matcher(value);
        return m.matches();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        return source;
    }
}
