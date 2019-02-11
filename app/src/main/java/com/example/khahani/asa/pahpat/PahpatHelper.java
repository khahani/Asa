package com.example.khahani.asa.pahpat;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohsen Beiranvand on 17/08/03.
 * Version 1.4 17/09/27.
 */

public class PahpatHelper {

    public static final int GOOD_PAYMENT = 1;
    public static final int GOOD_CONFIRM = 2;
    public static final int BALANCE = 3;
    public static final int BILL_PAY = 4;
    public static final int BILL_INQUIRY = 5;
    public static final int VOUCHER = 6;
    public static final int AUTHORISE = 7;
    public static final int RECEIPT = 8;
    public static final int GET_STATUS = 20;
    public static final int TXN_SUCCESS = 1;
    public static final int TXN_INPROCESS = 2;
    public static final int TXN_FAILED = 5;


    public interface Receiver {
        void onReceiveResult(int serviceId, int resultCode, Bundle resultData);
    }

    public static class PahpatException extends Exception{

        public PahpatException(String message)
        {
            super(message);
        }

    }

    public static class EMerchantInfoResult
    {
        String MID;
        String name;
        String tel;
        String address;
        String message;
        String postalCode;
        String terminalId;

        public String getMID() {
            return MID;
        }

        public void setMID(String MID) {
            this.MID = MID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getTerminalId() {
            return terminalId;
        }

        public void setTerminalId(String terminalId) {
            this.terminalId = terminalId;
        }
    }

    public static class EVoucherInfo{
        String productCode;
        String operatorCode;
        String amount;
        String currencyCode;

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getOperatorCode() {
            return operatorCode;
        }

        public void setOperatorCode(String operatorCode) {
            this.operatorCode = operatorCode;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }
    }

    @MainThread
    public static void billInquiry(int serviceId,Handler receiverHandler,Activity activity,
                            String billId,String billPaymentId,String resNum) throws PahpatException
    {
        if(!(activity instanceof Receiver))
            throw new PahpatException("Activity must implement Pahpat.Receiver interface.");

        new BillInquiry(serviceId,receiverHandler,activity,billId,billPaymentId,resNum).execute();
    }


    @MainThread
    public static void printReceipt(@NonNull Activity activity,@NonNull Handler receiverHandler,int TransType,Bundle data) throws PahpatException
    {
        if(!(activity instanceof Receiver))
            throw new PahpatException("Activity must implement Pahpat.Receiver interface.");

        new PrintReceipt(activity,receiverHandler,TransType,data).execute();
    }

    private static class PrintReceipt extends AsyncTask{

        private final int transType;
        private final Bundle data;
        private final Handler receiverHandler;
        private final Activity activity;

        public PrintReceipt(Activity activity, Handler receiverHandler, int transType, Bundle data) {

            this.activity = activity;
            this.receiverHandler = receiverHandler;
            this.transType = transType;
            this.data = data;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            PahpatResultReceiver mReceiver = new PahpatResultReceiver(receiverHandler);
            mReceiver.setReceiver((Receiver) activity);
            mReceiver.setServiceId(RECEIPT);
            Intent receiptIntent = new Intent("com.tosantechno.pahpat.services.ReceiptService");
            receiptIntent.setPackage("com.tosantechno.pahpat");
            receiptIntent.putExtra("ResultReceiver",receiverForSending(mReceiver));
            receiptIntent.putExtra("TransType" , transType);

            for (String key : data.keySet()) {
                try {
                    Log.d("Bundle " + key, data.get(key).toString());
                    receiptIntent.putExtra(key,data.get(key).toString());
                }catch (Exception e)
                {
                    if(key != null)
                        Log.e("bundle key",key + " is null");
                    e.printStackTrace();
                }
            }

            activity.startService(receiptIntent);
            return null;
        }
    }

    private static class BillInquiry extends AsyncTask {


        private final Handler receiverHandler;
        private final Activity activity;
        private final String billId;
        private final String billPaymentId;
        private final String resNum;
        private final int serviceId;

        BillInquiry(int serviceId, Handler receiverHandler, Activity activity, String billId, String billPaymentId, String resNum) {
            this.serviceId = serviceId;
            this.receiverHandler = receiverHandler;
            this.activity = activity;
            this.billId = billId;
            this.billPaymentId = billPaymentId;
            this.resNum = resNum;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            PahpatResultReceiver mReceiver = new PahpatResultReceiver(receiverHandler);
            mReceiver.setReceiver((Receiver) activity);
            mReceiver.setServiceId(serviceId);
            Intent billInquiryIntent = new Intent("com.tosantechno.pahpat.services.PaymentService");
            billInquiryIntent.setPackage("com.tosantechno.pahpat");
            billInquiryIntent.putExtra("ResultReceiver",receiverForSending(mReceiver));
            billInquiryIntent.putExtra("TransType" , 4);
            billInquiryIntent.putExtra("BillId",billId);
            billInquiryIntent.putExtra("BillPayId",billPaymentId);
            billInquiryIntent.putExtra("ResNum",resNum);
            activity.startService(billInquiryIntent);
            return null;
        }
    }


    @MainThread
    public static void getStatus(int serviceId,Handler receiverHandler,Activity activity) throws PahpatException
    {
        if(!(activity instanceof Receiver))
            throw new PahpatException("Activity must implement Pahpat.Receiver interface.");

        new GetStatus(serviceId,receiverHandler,activity).execute();
    }

    private static class GetStatus extends AsyncTask {


        private final Handler receiverHandler;
        private final Activity activity;
        private final int serviceId;

        GetStatus(int serviceId, Handler receiverHandler, Activity activity) {
            this.serviceId = serviceId;
            this.receiverHandler = receiverHandler;
            this.activity = activity;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            PahpatResultReceiver mReceiver = new PahpatResultReceiver(receiverHandler);
            mReceiver.setReceiver((Receiver) activity);
            mReceiver.setServiceId(serviceId);
            Intent pahpatServiceIntent = new Intent("com.tosantechno.pahpat.services.PahpatService");
            pahpatServiceIntent.setPackage("com.tosantechno.pahpat");
            pahpatServiceIntent.putExtra("ResultReceiver",receiverForSending(mReceiver));
            activity.startService(pahpatServiceIntent);
            return null;
        }
    }

    public static class PahpatResultReceiver extends ResultReceiver {

        Receiver mReceiver = null;
        int serviceId;

        public PahpatResultReceiver(Handler handler) {
            super(handler);
        }

        public void setServiceId(int serviceId)
        {
            this.serviceId = serviceId;
        }
        public void setReceiver(Receiver receiver)
        {
            mReceiver = receiver;
        }

        @Override
        public void onReceiveResult(int resultCode, Bundle resultData) {
            if(mReceiver!=null)
                mReceiver.onReceiveResult(serviceId,resultCode,resultData);
        }
    }

    public static ResultReceiver receiverForSending(ResultReceiver actualReceiver) {
        Parcel parcel = Parcel.obtain();
        actualReceiver.writeToParcel(parcel,0);
        parcel.setDataPosition(0);
        ResultReceiver receiverForSending = ResultReceiver.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        return receiverForSending;
    }
    /***/
    public static EMerchantInfoResult getMerchantInfo(Context context) throws PahpatException, RemoteException {
        Uri contentURI = Uri.parse("content://com.tosantechno.pahpat.contentProvider.merchants/merchant");
        ContentProviderClient CR = context.getContentResolver().acquireContentProviderClient(contentURI);
        EMerchantInfoResult merchantInfoResult = new EMerchantInfoResult();
        Cursor cpCursor = null;
        if(CR != null) {
            try{
                cpCursor = CR.query(contentURI, null, null, null, null);
                if (cpCursor.moveToFirst()) {

                    for(String key : cpCursor.getColumnNames())
                    {
                        Log.d("Merchant" , key + " : " + cpCursor.getString(cpCursor.getColumnIndex(key)));
                    }

                    merchantInfoResult.setMID(cpCursor.getString(cpCursor.getColumnIndex("MID")));
                    merchantInfoResult.setName(cpCursor.getString(cpCursor.getColumnIndex("name")));
                    merchantInfoResult.setTel(cpCursor.getString(cpCursor.getColumnIndex("tel")));
                    merchantInfoResult.setAddress(cpCursor.getString(cpCursor.getColumnIndex("address")));
                    merchantInfoResult.setMessage(cpCursor.getString(cpCursor.getColumnIndex("message")));
                    merchantInfoResult.setPostalCode(cpCursor.getString(cpCursor.getColumnIndex("postalCode")));
                    merchantInfoResult.setTerminalId(cpCursor.getString(cpCursor.getColumnIndex("terminalId")));
                }
            }catch (Exception e)
            {
                throw new PahpatException(e.getMessage());
            }

            cpCursor.close();

            return merchantInfoResult;
        }
        else
            throw new PahpatException("content provider not exits");
    }

    public static List<EVoucherInfo> getVouchers(Context context) throws PahpatException, RemoteException {
        Uri contentURI = Uri.parse("content://com.tosantechno.pahpat.contentProvider.vouchers/voucher");
        ContentProviderClient CR = context.getContentResolver().acquireContentProviderClient(contentURI);
        List<EVoucherInfo> voucherInfoList = new ArrayList<>();
        Cursor cpCursor = null;
        if(CR != null) {
            try{
                cpCursor = CR.query(contentURI, null, null, null, null);
                if (cpCursor.moveToFirst()) {


                    for(String key : cpCursor.getColumnNames())
                    {
                        Log.d("Voucher" , key + " : " + cpCursor.getString(cpCursor.getColumnIndex(key)));
                    }

                    do{
                        EVoucherInfo voucherInfo = new EVoucherInfo();
                        voucherInfo.setAmount(cpCursor.getString(cpCursor.getColumnIndex("amount")));
                        voucherInfo.setProductCode(cpCursor.getString(cpCursor.getColumnIndex("product_code")));
                        voucherInfo.setOperatorCode(cpCursor.getString(cpCursor.getColumnIndex("operator_code")));
                        voucherInfo.setCurrencyCode(cpCursor.getString(cpCursor.getColumnIndex("currency_code")));
                        voucherInfoList.add(voucherInfo);
                        // do what ever you want here
                    }while(cpCursor.moveToNext());
                }
            }catch (Exception e)
            {
                throw new PahpatException(e.getMessage());
            }

            cpCursor.close();

            return voucherInfoList;
        }
        else
            throw new PahpatException("content provider not exits");
    }

    public static void getOperators(Context context) throws PahpatException, RemoteException {
        Uri contentURI = Uri.parse("content://com.tosantechno.pahpat.contentProvider.operators/operator");
        ContentProviderClient CR = context.getContentResolver().acquireContentProviderClient(contentURI);
        Cursor cpCursor = null;
        if(CR != null) {
            try{
                cpCursor = CR.query(contentURI, null, null, null, null);
                if (cpCursor.moveToFirst()) {


                    for(String key : cpCursor.getColumnNames())
                    {
                        Log.d("Operator" , key + " : " + cpCursor.getString(cpCursor.getColumnIndex(key)));
                    }
                }
            }catch (Exception e)
            {
                throw new PahpatException(e.getMessage());
            }

            cpCursor.close();

            return;
        }
        else
            throw new PahpatException("content provider not exits");
    }

    public static void getCurrencies(Context context) throws PahpatException, RemoteException {
        Uri contentURI = Uri.parse("content://com.tosantechno.pahpat.contentProvider.currencies/currency");
        ContentProviderClient CR = context.getContentResolver().acquireContentProviderClient(contentURI);
        Cursor cpCursor = null;
        if(CR != null) {
            try{
                cpCursor = CR.query(contentURI, null, null, null, null);
                if (cpCursor.moveToFirst()) {
                    for(String key : cpCursor.getColumnNames())
                    {
                        Log.d("Currencies" , key + " : " + cpCursor.getString(cpCursor.getColumnIndex(key)));
                    }
                }
            }catch (Exception e)
            {
                throw new PahpatException(e.getMessage());
            }

            cpCursor.close();

            return;
        }
        else
            throw new PahpatException("content provider not exits");
    }

    /**
     * request voucher transaction from pahpat
     *
     * Attention ::
     * This method need use permission in android manifest like below :
     * <uses-permission android:name="com.tosantechno.pahpat.permission.VOUCHER_SERVICE" />
     * @param activity must implement Pahpat.Receiver
     * @param receiverHandler must define in main thread
     * @param amount amount of voucher
     * @param productCode you can get this code from pahpat content provider
     * @param resNum reserve number for internal use
     * @param currency three character from ISO-4217 like "IRR"
     * @param lang two character from ISO-639 like "FA"
     * @throws PahpatException if activity not implement Pahpat.Receiver exception will occur
     */
    @MainThread
    public static void voucherTxn(@NonNull Activity activity,@NonNull Handler receiverHandler,@NonNull String amount,@NonNull String productCode,
                                  @NonNull String resNum,@NonNull String currency,@NonNull String lang) throws PahpatException {

        if(!(activity instanceof Receiver))
            throw new PahpatException("Activity must implement Pahpat.Receiver interface.");

        Intent paymentIntent = new Intent("com.tosantechno.pahpat.VOUCHER_PAY");


        PahpatResultReceiver resultReceiver = new PahpatResultReceiver(receiverHandler);
        resultReceiver.setServiceId(PahpatHelper.VOUCHER);
        resultReceiver.setReceiver((Receiver) activity);

        paymentIntent.putExtra("TransType" , 6);
        paymentIntent.putExtra("Amount" , amount);
        paymentIntent.putExtra("ProductCode" , productCode);
        paymentIntent.putExtra("ResNum" , resNum);
        paymentIntent.putExtra("Currency" , currency);
        paymentIntent.putExtra("Lang" , lang);
        paymentIntent.putExtra("ResultReceiver" , PahpatHelper.receiverForSending(resultReceiver));

        activity.startActivityForResult( paymentIntent, PahpatHelper.VOUCHER );
    }

    /**
     * Attention ::
     * This method need use permission in android manifest like below :
     * <uses-permission android:name="com.tosantechno.pahpat.permission.PAYMENT_SERVICE" />
     * @param activity
     * @param receiverHandler
     * @param approvalCode
     * @param decideType
     * @throws PahpatException
     */
    @MainThread
    public static void confirmTxn(@NonNull Activity activity,@NonNull Handler receiverHandler,@NonNull String approvalCode,@NonNull boolean decideType) throws PahpatException {
        if(!(activity instanceof Receiver))
            throw new PahpatException("Activity must implement Pahpat.Receiver interface.");

        PahpatResultReceiver resultReceiver = new PahpatResultReceiver(receiverHandler);
        resultReceiver.setServiceId(PahpatHelper.GOOD_CONFIRM);
        resultReceiver.setReceiver((Receiver) activity);

        Intent confirmIntent = new Intent("com.tosantechno.pahpat.DO_CONFIRM");
        confirmIntent.putExtra("ApprovalCode" , approvalCode);
        confirmIntent.putExtra("DecideType",decideType);
        confirmIntent.putExtra("ResultReceiver" , PahpatHelper.receiverForSending(resultReceiver));
        activity.startActivityForResult( confirmIntent, PahpatHelper.GOOD_CONFIRM );

    }

    @MainThread
    public static void billTxn(@NonNull Activity activity,@NonNull Handler receiverHandler,@NonNull String billId,@NonNull String billPayId,
                                  @NonNull String resNum,@NonNull String lang) throws PahpatException {

        if(!(activity instanceof Receiver))
            throw new PahpatException("Activity must implement Pahpat.Receiver interface.");

        Intent paymentIntent = new Intent("com.tosantechno.pahpat.BILL_INQUIRY_PAY");

        PahpatResultReceiver resultReceiver = new PahpatResultReceiver(receiverHandler);
        resultReceiver.setServiceId(PahpatHelper.BILL_PAY);
        resultReceiver.setReceiver((Receiver) activity);

        paymentIntent.putExtra("TransType" , 7);
        paymentIntent.putExtra("ResNum" , resNum);
        paymentIntent.putExtra("BillId" , billId);
        paymentIntent.putExtra("BillPayId" , billPayId);
        paymentIntent.putExtra("Lang" , lang);
        paymentIntent.putExtra("ResultReceiver" , PahpatHelper.receiverForSending(resultReceiver));

        activity.startActivityForResult( paymentIntent, PahpatHelper.BILL_PAY );

    }

    @MainThread
    public static void purchaseTxn(@NonNull Activity activity,@NonNull Handler receiverHandler,@NonNull String amount,@NonNull String merchantMobileNo,
                                  @NonNull String resNum,@NonNull String currency,@NonNull String lang) throws PahpatException {

        if(!(activity instanceof Receiver))
            throw new PahpatException("Activity must implement Pahpat.Receiver interface.");

        Intent paymentIntent = new Intent("com.tosantechno.pahpat.DO_PAYMENT");

        PahpatResultReceiver resultReceiver = new PahpatResultReceiver(receiverHandler);
        resultReceiver.setServiceId(PahpatHelper.GOOD_PAYMENT);
        resultReceiver.setReceiver((Receiver) activity);

        if(!amount.equals(""))
        {

            paymentIntent.putExtra("TransType" , 1);
            paymentIntent.putExtra("Amount" , amount);
            paymentIntent.putExtra("MerchantMobileNo" , merchantMobileNo);
            paymentIntent.putExtra("ResNum" , resNum);
            paymentIntent.putExtra("Currency" , currency);
            paymentIntent.putExtra("Lang" , lang);
            paymentIntent.putExtra("ResultReceiver" , PahpatHelper.receiverForSending(resultReceiver));

            activity.startActivityForResult( paymentIntent, PahpatHelper.GOOD_PAYMENT );
        }
    }



}
