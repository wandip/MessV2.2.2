package com.messedup.saurabh.mess2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


import com.instamojo.android.models.Order;

import static com.messedup.saurabh.mess2.MainActivity.UserDataObj;


public class PaymentGatewayActivity {

    private static final HashMap<String, String> env_options = new HashMap<>();

    static {
        env_options.put("Test", "https://test.instamojo.com/");
        env_options.put("Production", "https://api.instamojo.com/");
    }


    String timestamp=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());



    String name = UserDataObj.getName();
    final String email = UserDataObj.getEmail();
    String phone = UserDataObj.getContact();
    String amount = "3050";
    String description = "1 Month Payment";
    String transactionID=UserDataObj.getuid()+timestamp;


    public void createTransactionDetails() {

        Order order = new Order("accessToken", transactionID, name, email, phone, amount, description);




    }

    //Order order = new Order(accessToken, transactionID, name, email, phone, amount, purpose);


}
