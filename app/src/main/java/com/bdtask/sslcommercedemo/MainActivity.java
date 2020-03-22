package com.bdtask.sslcommercedemo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sslcommerz.library.payment.model.datafield.MandatoryFieldModel;
import com.sslcommerz.library.payment.model.dataset.TransactionInfo;
import com.sslcommerz.library.payment.model.util.CurrencyType;
import com.sslcommerz.library.payment.model.util.ErrorKeys;
import com.sslcommerz.library.payment.model.util.SdkCategory;
import com.sslcommerz.library.payment.model.util.SdkType;
import com.sslcommerz.library.payment.viewmodel.listener.OnPaymentResultListener;
import com.sslcommerz.library.payment.viewmodel.management.PayUsingSSLCommerz;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button paymentButton;
    EditText amountEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        paymentButton     = findViewById(R.id.pay);
        amountEditText    = findViewById(R.id.amount);

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amount = amountEditText.getText().toString();
                if (amount.isEmpty()){
                    //do something......
                }else {
                    paymentProcess(amount);
                }

            }
        });


    }

    private void paymentProcess(String amount) {

        MandatoryFieldModel mandatoryFieldModel = new MandatoryFieldModel("your store id", "store password", amount, "Your transaction id", CurrencyType.BDT, SdkType.TESTBOX, SdkCategory.BANK_LIST);
        PayUsingSSLCommerz.getInstance().setData(MainActivity.this, mandatoryFieldModel, new OnPaymentResultListener() {
            @Override
            public void transactionSuccess(TransactionInfo transactionInfo) {

                // If payment is success and risk label is 0 get payment details from here
                if (transactionInfo.getRiskLevel().equals("0")) {

                    Log.wtf("ak"+TAG, transactionInfo.getValId());

            /** After successful transaction send this val id
             * to this link to get details of transaction
              https://sandbox.sslcommerz.com/validator/api/validationserverAPI.php?val_id=yourvalid&store_id=yourstoreid&store_passwd=yourpassword
             **/
                }

            // Payment is success but payment is not complete yet. Card on hold now.

                else {
                    Log.e("ak"+TAG, "Transaction in risk. Risk Title : " + transactionInfo.getRiskTitle());
                }
            }

            @Override
            public void transactionFail(String s) {
                Log.e(TAG, s);
            }


            @Override
            public void error(int errorCode) {
                switch (errorCode) {

                    // Your provides information is not valid.
                    case ErrorKeys.USER_INPUT_ERROR:
                        Log.wtf(TAG, "User Input Error");
                        break;

                    // Internet is not connected.
                    case ErrorKeys.INTERNET_CONNECTION_ERROR:
                        Log.wtf(TAG, "Internet Connection Error");
                        break;

                    // Server is not giving valid data.
                    case ErrorKeys.DATA_PARSING_ERROR:
                        Log.wtf(TAG, "Data Parsing Error");
                        break;

                    // User press back button or canceled the transaction.
                    case ErrorKeys.CANCEL_TRANSACTION_ERROR:
                        Log.wtf(TAG, "User Cancel The Transaction");
                        break;

                    // Server is not responding.
                    case ErrorKeys.SERVER_ERROR:
                        Log.wtf(TAG, "Server Error");
                        break;

                    // For some reason network is not responding
                    case ErrorKeys.NETWORK_ERROR:
                        Log.wtf(TAG, "Network Error");
                        break;

                }
            }
        });


    }
}
