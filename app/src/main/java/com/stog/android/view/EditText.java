package com.stog.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;

import com.stog.android.R;
import com.stog.android.validator.DateValidator;
import com.stog.android.validator.MaskedValidator;
import com.stog.android.validator.NotEmptyValidator;
import com.stog.android.validator.PhoneNumberValidator;


public class EditText extends AppCompatEditText {
    private String maskedValidatorType = "type_null";
    private String maskedValidatorMessage;
    private MaskedValidator maskedValidator;


    public EditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AppEditText);

        maskedValidatorType = a.getString(R.styleable.AppEditText_validator);

        if(maskedValidatorType == null) {
            maskedValidatorType = "type_null";
        } else {
            createMaskedValidator();
        }

        a.recycle();
    }

    private void createMaskedValidator(){

        switch (maskedValidatorType) {
            case "date":
                this.maskedValidator = new DateValidator();
                this.maskedValidatorMessage = getResources().getString(R.string.validator_date_wrong);
                addTextChangedListener(maskedValidator);
                setFilters(new InputFilter[]{new DigitsKeyListener(), maskedValidator});
                break;
            case "not_empty":
                this.maskedValidator = new NotEmptyValidator();
                this.maskedValidatorMessage = getResources().getString(R.string.validator_not_empty_wrong);
                addTextChangedListener(maskedValidator);
                break;
            case "phone_number":
                this.maskedValidator = new PhoneNumberValidator();
                this.maskedValidatorMessage = getResources().getString(R.string.validator_phone_wrong);
                addTextChangedListener(maskedValidator);
                setFilters(new InputFilter[]{new DigitsKeyListener(), maskedValidator});
                break;

            default: break;
        }

    }

    public boolean isValid() {
        if (maskedValidator == null || maskedValidator.isValid()) {
            setErrorStyle(false);
            return true;
        } else {
            setErrorStyle(true);
            return false;
        }
    }

    private void setErrorStyle(boolean isError) {
        if(getParent() instanceof TextInputLayout) {
            TextInputLayout parent = (TextInputLayout) getParent();
            if(isError) {
                parent.setError(maskedValidatorMessage);
            } else {
                parent.setErrorEnabled(false);
            }
        }
    }

    public String getValue() {
        if(maskedValidator != null) {
            return maskedValidator.getValue();
        } else if(getText() != null){
            return getText().toString();
        } else {
            return null;
        }
    }
}
