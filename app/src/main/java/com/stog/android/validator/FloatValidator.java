package com.stog.android.validator;

import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FloatValidator implements MaskedValidator {

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
        if(value == null) {
            return false;
        }
        Pattern p = Pattern.compile("^\\d{1,5}(\\.\\d{1,2})?$");
        Matcher m = p.matcher(value);
        return m.matches();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        SpannableStringBuilder result = new SpannableStringBuilder(source);

        // Посмотрим как будет выглядеть строка после обработки
        String updated = new SpannableStringBuilder(dest).replace(dstart, dend, result, start, end).toString();

        // Тут маска 999.99
        if(updated.indexOf(".") > 0) {
            if (updated.substring(0, updated.indexOf(".")).length() > 5) {
                return "";
            }
            if (updated.substring(updated.indexOf(".")).length() > 5) {
                return "";
            }
        } else if(updated.length() > 5 && !updated.endsWith(".")) {
            return "";
        }

        return result;
    }

}
