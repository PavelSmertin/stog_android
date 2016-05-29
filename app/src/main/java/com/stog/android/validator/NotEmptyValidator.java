package com.stog.android.validator;

import android.text.Editable;
import android.text.Spanned;
import android.text.TextUtils;


public class NotEmptyValidator implements MaskedValidator {

    private String value;

    @Override
    public void afterTextChanged(Editable source) {
        this.value = source.toString();
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
        return this.hasFullLength();
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
