package com.stog.android.statistic;

public class ErrorParamsPull extends ButtonParamsPull{

    private String errorType;
    private String errorDescription;

    public ErrorParamsPull(String id, String errorType, String errorDescription) {
        super(id);
        this.errorType = errorType;
        this.errorDescription = errorDescription;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorDescription() {
        return errorDescription;
    }


}
