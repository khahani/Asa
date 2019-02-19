package com.example.khahani.asa.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.khahani.asa.AsaApplication;
import com.example.khahani.asa.R;
import com.example.khahani.asa.utils.FontManager;
import com.example.khahani.asa.utils.PrinterUtils;
import com.pax.dal.IDAL;
import com.pax.dal.IPrinter;
import com.pax.dal.exceptions.PrinterDevException;

public class PrintActivity extends AppCompatActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_print);
        mBtnPrint = (Button) findViewById(R.id.btn_print);
        mBtnPreview = (Button) findViewById(R.id.btn_preview);
        mImgPreview = (ImageView) findViewById(R.id.img_preview);

        mBtnPreview.setOnClickListener(this);
        mBtnPrint.setOnClickListener(this);

        idal = AsaApplication.getInstance().getIdal();
        iPrinter = idal.getPrinter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_preview:
                mBitmap = generateBitmapByLayout();
                mImgPreview.setImageBitmap(mBitmap);
                break;
            case R.id.btn_print:
                mBitmap = generateBitmapByLayout();
                mImgPreview.setImageBitmap(mBitmap);
                if (null == mPrintThread) {
                    mPrintThread = new PrintThread();
                }
                mPrintThread.start();
                break;
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


        return PrinterUtils.convertViewToBitmap(view);
    }

}
