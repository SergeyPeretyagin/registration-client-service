package com.userservice.utilies;

public class ValidationUtil {
    public static boolean checkMobileNumber(String mobilePhone){
        return mobilePhone.matches("\\d{12}");
    }

}
