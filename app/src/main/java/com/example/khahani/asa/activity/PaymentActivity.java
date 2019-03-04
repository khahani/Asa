package com.example.khahani.asa.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khahani.asa.AsaActivity;
import com.example.khahani.asa.AsaApplication;
import com.example.khahani.asa.R;
import com.example.khahani.asa.pahpat.PahpatHelper;
import com.example.khahani.asa.utils.Asa;
import com.example.khahani.asa.utils.FontManager;
import com.example.khahani.asa.utils.PrinterUtils;
import com.pax.dal.IDAL;
import com.pax.dal.IPrinter;
import com.pax.dal.exceptions.PrinterDevException;

import java.text.DecimalFormat;

import static com.example.khahani.asa.pahpat.PahpatHelper.GOOD_PAYMENT;


public class PaymentActivity extends AsaActivity
        implements PahpatHelper.Receiver {

    private float lastX;
    private Handler receiverHandler;
    private String amount;


    private IDAL idal;
    private IPrinter iPrinter;
    private Button mBtnPrint;
    private ImageView mImgPreview;
    private Button mBtnPreview;
    private Bitmap mBitmap;
    private PrintThread mPrintThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        idal = AsaApplication.getInstance().getIdal();
        iPrinter = idal.getPrinter();

        if (!getIntent().hasExtra("amount")) {
            finish();
        }

        amount = getIntent().getStringExtra("amount");
        amount = "1024";

        TextView textViewAmount = findViewById(R.id.textViewAmount);
        DecimalFormat frm = new DecimalFormat("#,###,###");
        textViewAmount.setText(getResources().getString(R.string.show_lastprice,
                frm.format(Integer.parseInt(amount))));

        Button buttonPayment = findViewById(R.id.buttonPayment);
        buttonPayment.setOnClickListener(v -> stepPayment(amount));

        Button buttonPrint = findViewById(R.id.buttonPrint);
        buttonPrint.setOnClickListener(v -> stepPrint());

        stepPayment(amount);

    }

    private void stepPrint() {

        mBitmap = generateBitmapByLayout();
        if (null == mPrintThread) {
            mPrintThread = new PrintThread();
        }
        mPrintThread.start();

    }

    private void stepPayment(String amount) {

        receiverHandler = new Handler();

        Signature[] sigs = new Signature[0];
        try {
            sigs = getApplicationContext().getPackageManager()
                    .getPackageInfo("com.tosantechno.pahpat",
                            PackageManager.GET_SIGNATURES).signatures;
            for (Signature sig : sigs) {
                Log.d("SIGN", sig.toCharsString());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Intent paymentIntent = new Intent("com.tosantechno.pahpat.DO_PAYMENT");

        if (amount == null)
            finish();

        if (!amount.equals("")) {
            try {
                PahpatHelper.purchaseTxn(PaymentActivity.this, receiverHandler, amount, "09353391873", "123", "IRR", "FA");
            } catch (PahpatHelper.PahpatException e) {
                e.printStackTrace();
            }
        } else {
            finish();
        }
    }

    @Override
    public void onReceiveResult(int serviceId, int resultCode, Bundle resultData) {
        Log.e("onReceiveResult", resultCode + " ");

        if (resultData != null)
            for (String key : resultData.keySet()) {
                try {
                    Log.d("Bundle " + key, resultData.get(key).toString());
                } catch (Exception e) {
                    if (key != null)
                        Log.e("bundle key", key + " is null");
                    e.printStackTrace();
                }
            }

        /**
         * Issuer: شهر (2)
         * Reason: Successful
         * ResNum: 123
         * Result: erSucceed
         * DiscountAmount: 0
         * MerchantMobileNo: 09353391873
         * TransDate: 20190219075911
         * TransType: 1
         * TransStatus: 00
         * RRN: 905007477915
         * Lang: FA
         * ResultReceiver: android.os.ResultReceiver@2c2620c
         * ApprovalCode: 051133850000150759110219
         * TraceNo: 477915
         * Currency: IRR
         * TerminalID: 05113385
         * MustFinish: false
         * MerchantID: 51007887
         * MaskedPan: 504706***7163
         * Amount: 1024
         */

        if (serviceId == PahpatHelper.GOOD_CONFIRM) {
            Log.e("onReceiveResult", "== GOOD_CONFIRM ==");

            for (String key : resultData.keySet()) {
                try {
                    Log.d("Bundle " + key, resultData.get(key).toString());
                } catch (Exception e) {
                    if (key != null)
                        Log.e("bundle key", key + " is null");
                    e.printStackTrace();
                }
            }
        }
//        if (serviceId == PahpatHelper.VOUCHER) {
//            Log.e("onReceiveResult", "== VOUCHER ==");
//
//            for (String key : resultData.keySet()) {
//                try {
//                    Log.d("Bundle " + key, resultData.get(key).toString());
//                } catch (Exception e) {
//                    if (key != null)
//                        Log.e("bundle key", key + " is null");
//                    e.printStackTrace();
//                }
//            }
//        }
//        if (serviceId == PahpatHelper.BILL_INQUIRY)
//            if (resultCode == 1) {
//                if (resultData.getString("Result").equals("erSucceed")) {
//                    Toast.makeText(getApplicationContext(),
//                            "Amount " + resultData.getString("Amount") + "\n" +
//                                    resultData.getString("ResNum") + "\n" +
//                                    resultData.getString("BillId") + "\n" +
//                                    resultData.getString("BillPayId") + "\n"
//                            ,
//                            Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), resultData.getString("Reason"), Toast.LENGTH_LONG).show();
//                }
//            } else {
//                Log.e("onReceiveResult", resultCode + " BILL_INQUIRY");
//            }
//        if (serviceId == PahpatHelper.GET_STATUS)
//            if (resultCode == 1) {
//                Log.e("onReceiveResult", "== GET_STATUS ==");
//
//                if (resultData != null)
//                    for (String key : resultData.keySet()) {
//                        try {
//                            Log.d("Bundle " + key, " " + resultData.get(key).toString());
//                        } catch (Exception e) {
//                            if (key != null)
//                                Log.e("bundle key", key + " is null");
//                            e.printStackTrace();
//                        }
//                    }
//                Toast.makeText(getApplicationContext(), resultData.getBoolean("Result") + " ", Toast.LENGTH_LONG).show();
//            } else {
//                Log.e("onReceiveResult ", resultCode + " GET_STATUS");
//            }
        if (serviceId == GOOD_PAYMENT) {
            if (resultCode == PahpatHelper.TXN_SUCCESS) {


                Toast.makeText(this, "Success : " + resultData.get("Reason"), Toast.LENGTH_SHORT).show();

                String approvalCode = resultData.getString("ApprovalCode", "");

                Log.d("GOOD_PAYMENT", "Success : " + approvalCode + " " + resultData.get("Reason"));

                try {
                    PahpatHelper.confirmTxn(PaymentActivity.this, receiverHandler, approvalCode, true);
                } catch (PahpatHelper.PahpatException e) {
                    e.printStackTrace();
                }

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
        Log.d("activityResult", "Happened");
        Log.d("activityResult", "requestCode: " + requestCode);
        Log.d("activityResult", "resultCode: " + resultCode);


        if (requestCode == PahpatHelper.AUTHORISE) {
            if (resultCode == RESULT_OK) {
                Log.d("AUTHORISE", "OK");
                Toast.makeText(this,"Success : "+intent.getExtras().get("Authorised") ,Toast.LENGTH_SHORT).show();

            } else {
                Log.d("AUTHORISE", "Failed");

            }
        }
    }

    class PrintThread extends Thread {
        @Override
        public void run() {
            try {
                iPrinter.init();
                iPrinter.setGray(100);
                iPrinter.printBitmap(mBitmap);
                iPrinter.start();
            } catch (PrinterDevException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap generateBitmapByLayout() {
        View view = this.getLayoutInflater().inflate(R.layout.pars_receipt, null);
//        TextView tv = view.findViewById(R.id.merchant_name_label);
//        tv.setTypeface(FontManager.getInstance().getPersianFont());

//        .setText(getIntent().getStringExtra("hotel_id"));

//        .setText(getIntent().getStringExtra("hotelPhone"));

        TextView name_text = view.findViewById(R.id.name_text);
        name_text .setTypeface(FontManager.getInstance().getPersianFont());
        name_text.setText(getIntent().getStringExtra("reserveName"));

        TextView family_text= view.findViewById(R.id.family_text);
        family_text.setTypeface(FontManager.getInstance().getPersianFont());
        family_text.setText(getIntent().getStringExtra("reserveFamily"));

        TextView codemelli_text= view.findViewById(R.id.codemelli_text);
        codemelli_text.setTypeface(FontManager.getInstance().getPersianFont());
        codemelli_text.setText(getIntent().getStringExtra("reserveCodeMelli"));

        TextView mobile_text= view.findViewById(R.id.mobile_text);
        mobile_text.setTypeface(FontManager.getInstance().getPersianFont());
        mobile_text.setText(getIntent().getStringExtra("reservePhone"));

        TextView trace_no_text= view.findViewById(R.id.trace_no_text);
        trace_no_text.setTypeface(FontManager.getInstance().getPersianFont());
        trace_no_text.setText(getIntent().getStringExtra("id_reserve_asa"));

        TextView hotel_text= view.findViewById(R.id.hotel_text);
        hotel_text.setTypeface(FontManager.getInstance().getPersianFont());
        hotel_text.setText(getIntent().getStringExtra("hotel_persian_name"));

        TextView from_date_text= view.findViewById(R.id.from_date_text);
        from_date_text.setTypeface(FontManager.getInstance().getPersianFont());
        from_date_text.setText(getIntent().getStringExtra("fromDate"));

        TextView to_date_text= view.findViewById(R.id.to_date_text);
        to_date_text.setTypeface(FontManager.getInstance().getPersianFont());
        to_date_text.setText(Asa.getToDate(getIntent().getStringExtra("fromDate"),getIntent().getStringExtra("nightNumbers")));


        LinearLayout roomDetailContainer = view.findViewById(R.id.room_detail_container);

        for (int i = 0; i < getIntent().getIntExtra("roomCount", 0); i++) {

            View roomDetailView=  getLayoutInflater().inflate(R.layout.pars_receipt_room_detail, null);

//            .setText(getIntent().getStringExtra("foodType_" + i));

            TextView room_type_text = roomDetailView.findViewById(R.id.room_type_text);
            room_type_text .setTypeface(FontManager.getInstance().getPersianFont());
            room_type_text.setText(getIntent().getStringExtra("room_kind_name_" + i));

            TextView room_number_text = roomDetailView.findViewById(R.id.room_number_text);
            room_number_text .setTypeface(FontManager.getInstance().getPersianFont());
            room_number_text.setText(Integer.toString(getIntent().getIntExtra("selectedRoomsCount_" + i, -1)));

            TextView adult_text = roomDetailView.findViewById(R.id.adult_text);
            adult_text .setTypeface(FontManager.getInstance().getPersianFont());
            adult_text.setText(Integer.toString(getIntent().getIntExtra("selectedAdultsCount_" + i, -1)));

            TextView child_text = roomDetailView.findViewById(R.id.child_text);
            child_text .setTypeface(FontManager.getInstance().getPersianFont());
            child_text.setText(Integer.toString(getIntent().getIntExtra("selectedChildsCount_" + i, -1)));

            roomDetailContainer.addView(roomDetailView);
        }


        TextView hotel_address_text= view.findViewById(R.id.hotel_address_text);
        hotel_address_text.setTypeface(FontManager.getInstance().getPersianFont());
        hotel_address_text.setText(getIntent().getStringExtra("hotelAddress"));

        TextView hotel_phone_text= view.findViewById(R.id.hotel_phone_text);
        hotel_phone_text.setTypeface(FontManager.getInstance().getPersianFont());
        hotel_phone_text.setText("051------- آسا");


        ImageView hotel_isps_image = view.findViewById(R.id.hotel_isps_image);

        return PrinterUtils.convertViewToBitmap(view);
    }


}
