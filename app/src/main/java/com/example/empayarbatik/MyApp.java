package com.example.empayarbatik;

import android.app.Application;

import com.stripe.android.PaymentConfiguration;
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51Jh5QqBQTso4SBk75OEuYIBdDYn8M8DGdFofKNHLDuehK0hkH8lfeKsTS08czc0W1eNwptD9NxtTJjmJiPAvnLuZ00QlFD0t6N"
        );
    }
}