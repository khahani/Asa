package com.example.khahani.asa.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.os.Parcel;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.khahani.asa.R;
import com.example.khahani.asa.pahpat.PahpatHelper;

import static com.example.khahani.asa.pahpat.PahpatHelper.GOOD_PAYMENT;


public class PaymentActivity extends AppCompatActivity
        implements PahpatHelper.Receiver {

    private float lastX;
    private Handler receiverHandler;

    Button paymentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        paymentButton = findViewById(R.id.buttonPayment);

        receiverHandler = new Handler();

        Signature[] sigs = new Signature[0];
        try {
            sigs = getApplicationContext().getPackageManager()
                    .getPackageInfo("com.tosantechno.pahpat",
                            PackageManager.GET_SIGNATURES).signatures;
            for (Signature sig : sigs)
            {
                Log.d("SIGN" , sig.toCharsString());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        paymentButton.setOnClickListener(v -> {
            try {
                Bundle data = new Bundle();

                data.putString("RRN","726122996185");
                data.putString("TransDate","20170912");
                data.putString("TraceNo","996185");
                data.putString("Issuer","شهر");
                data.putString("MaskedPan","603765***4056");
                data.putString("Amount","1000");
                data.putString("Lang","FA");
                data.putString("Currency","IRR");

                data.putString("VoucherSerial","123123123123");

                data.putString("BillId","887977897789");
                data.putString("BillPayId","7601200");


                PahpatHelper.printReceipt(
                        PaymentActivity.this ,
                        receiverHandler ,
                        1 , //1: purchase , 6: voucher , 7: bill
                        data
                );
            } catch (PahpatHelper.PahpatException e) {
                e.printStackTrace();
            }

            /**
             * Attention ::
             * For call this intent you need use permission in android manifest like below :
             * <uses-permission android:name="com.tosantechno.pahpat.permission.PAYMENT_SERVICE" />
             */
//            Intent paymentIntent = new Intent("com.tosantechno.pahpat.DO_PAYMENT");
//
//            String amount = "1000";
//
//
//            if(!amount.equals(""))
//            {
//                try {
//                    PahpatHelper.purchaseTxn(PaymentActivity.this,receiverHandler,amount,"09353391873","123","IRR","FA");
//                } catch (PahpatHelper.PahpatException e) {
//                    e.printStackTrace();
//                }
//            }
        });
    }

    @Override
    public void onReceiveResult(int serviceId, int resultCode, Bundle resultData) {
        Log.e("onReceiveResult",resultCode+ " " );

        if(resultData!=null)
            for (String key : resultData.keySet()) {
                try {
                    Log.d("Bundle " + key, resultData.get(key).toString());
                }catch (Exception e)
                {
                    if(key != null)
                        Log.e("bundle key",key + " is null");
                    e.printStackTrace();
                }
            }

        if(serviceId == PahpatHelper.GOOD_CONFIRM)
        {
            Log.e("onReceiveResult", "== GOOD_CONFIRM ==" );

            for (String key : resultData.keySet()) {
                try {
                    Log.d("Bundle " + key, resultData.get(key).toString());
                }catch (Exception e)
                {
                    if(key != null)
                        Log.e("bundle key",key + " is null");
                    e.printStackTrace();
                }
            }
        }
        if(serviceId == PahpatHelper.VOUCHER)
        {
            Log.e("onReceiveResult", "== VOUCHER ==" );

            for (String key : resultData.keySet()) {
                try {
                    Log.d("Bundle " + key, resultData.get(key).toString());
                }catch (Exception e)
                {
                    if(key != null)
                        Log.e("bundle key",key + " is null");
                    e.printStackTrace();
                }
            }
        }
        if(serviceId == PahpatHelper.BILL_INQUIRY)
            if(resultCode == 1)
            {
                if(resultData.getString("Result").equals("erSucceed"))
                {
                    Toast.makeText(getApplicationContext(),
                            "Amount "+resultData.getString("Amount") +"\n"+
                                    resultData.getString("ResNum")+"\n"+
                                    resultData.getString("BillId")+"\n"+
                                    resultData.getString("BillPayId")+"\n"
                            ,
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),resultData.getString("Reason"),Toast.LENGTH_LONG).show();
                }
            }else{
                Log.e("onReceiveResult",resultCode+ " BILL_INQUIRY" );
            }
        if(serviceId == PahpatHelper.GET_STATUS)
            if(resultCode == 1)
            {
                Log.e("onReceiveResult", "== GET_STATUS ==" );

                if(resultData!=null)
                    for (String key : resultData.keySet()) {
                        try {
                            Log.d("Bundle " + key," "+ resultData.get(key).toString());
                        }catch (Exception e)
                        {
                            if(key != null)
                                Log.e("bundle key",key + " is null");
                            e.printStackTrace();
                        }
                    }
                Toast.makeText(getApplicationContext(),resultData.getBoolean("Result")+" ",Toast.LENGTH_LONG).show();
            }else{
                Log.e("onReceiveResult ",resultCode+ " GET_STATUS" );
            }
        if (serviceId == GOOD_PAYMENT) {
            if (resultCode == PahpatHelper.TXN_SUCCESS) {


                Toast.makeText(this, "Success : " + resultData.get("Reason"), Toast.LENGTH_SHORT).show();

                String approvalCode = resultData.getString("ApprovalCode", "");

                Log.d("GOOD_PAYMENT", "Success : " + approvalCode + " " + resultData.get("Reason"));

                /*try {
                    PahpatHelper.confirmTxn(MainActivity.this, receiverHandler, approvalCode, true);
                } catch (PahpatHelper.PahpatException e) {
                    e.printStackTrace();
                }*/

            } else {
                if (resultData != null) {
                    Log.d("GOOD_PAYMENT", "Not Success : " + resultData.get("Result"));
                    Toast.makeText(this, "Not Success : " + resultData.get("Reason"), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // Check which request we're responding to
        Log.d("activityResult","Happened");


        if(requestCode == PahpatHelper.AUTHORISE) {
            if (resultCode == RESULT_OK) {
                Log.d("AUTHORISE" , "OK");
//                Toast.makeText(this,"Success : "+intent.getExtras().get("Authorised") ,Toast.LENGTH_SHORT).show();

            }else{
                Log.d("AUTHORISE" , "Failed");

            }
        }
    }

}
