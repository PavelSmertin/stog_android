package com.stog.android.api.response;

import com.google.gson.annotations.SerializedName;

public class CheckLoginResponse {

    public static final String PHONE_IN_BLACKLIST = "phone_in_blacklist";
    public static final String PHONE_EXISTS = "phone_exists";

    @SerializedName("phone_exists")
    private boolean phoneExists;

    private String phone;

    @SerializedName("email_exists")
    private boolean emailExists;

    private String email;

    @SerializedName("reason_phone")
    private String reasonPhone;

    @SerializedName("mobile_device_exist")
    private boolean mobileDeviceExists;

    @SerializedName("mobile_pincode_exist")
    private boolean mobilePincodeExists;

    public boolean isPhoneExists() {
        return phoneExists;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isEmailExists() {
        return emailExists;
    }

    public String getEmail() {
        return email;
    }

    public String getReasonPhone() {
        return reasonPhone;
    }

    public boolean isMobileDeviceExists() {
        return mobileDeviceExists;
    }

    public boolean isMobilePincodeExists() {
        return mobilePincodeExists;
    }
}
