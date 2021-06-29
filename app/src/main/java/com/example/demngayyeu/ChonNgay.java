package com.example.demngayyeu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ChonNgay extends AppCompatActivity {

    Button btnXacNhan;
    EditText editTextChonNgay, editTextTenBan, editTextTenNguoiAy;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_ngay);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        anhXa();

        editTextChonNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonNgay();
            }
        });
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(editTextChonNgay.getText().toString().equals(""))
                        throw new Exception("Vui lòng chọn ngày yêu của 2 bạn!!");
                    String ngay=editTextChonNgay.getText().toString();
                    chuyenActivity();
                    startService(v);
                }
                catch (Exception e){
                    Toast.makeText(ChonNgay.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void anhXa() {
        btnXacNhan = (Button) findViewById(R.id.buttonXacNhan);
        editTextChonNgay = (EditText) findViewById(R.id.editTextChonNgay);
        editTextTenBan = (EditText) findViewById(R.id.editTextTenBan);
        editTextTenNguoiAy =  (EditText) findViewById(R.id.editTextTenNguoiAy);
    }

    private  void chonNgay(){
        calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                editTextChonNgay.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }
    private void chuyenActivity(){
        String ngay = simpleDateFormat.format(calendar.getTime());
        Intent intent = new Intent();
        intent.putExtra("Ngay", ngay);
        intent.putExtra("TenBan", editTextTenBan.getText().toString());
        intent.putExtra("TenNguoiAy", editTextTenNguoiAy.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void startService(View v){

        Log.d("TEST", editTextChonNgay.getText().toString());
        Intent serviceIntent = new Intent(this, NgayYeuServices.class);
        serviceIntent.putExtra("inputExtra", editTextChonNgay.getText().toString());

        startService(serviceIntent);
    }

    public void stopService(View v){
        Intent serviceIntent = new Intent(this, NgayYeuServices.class);
        stopService(serviceIntent);
    }
}