package com.example.demngayyeu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    private static final int DIENTHONGTIN_ACTIVITY_REQUEST_CODE = 1;

    private static final int REQUEST_CODE_SELECT_IMAGE1 = 2;
    private static final int REQUEST_CODE_SELECT_IMAGE2 = 3;
    private static final int REQUEST_CODE_STORAGE_PERMISSION1 = 2;
    private static final int REQUEST_CODE_STORAGE_PERMISSION2 = 3;

    Button btnChonLai;
    TextView txtNgayYeu, txtTenNguoiAy, txtTenBan;
    Calendar calendar;
    SharedPreferences sharedPreferences;
    ImageView imageView, imageViewNguoi1, imageViewNguoi2;
    String ngayDaChon = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        anhXa();
        //imageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.heart_custom));

        sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        ngayDaChon = sharedPreferences.getString("NgayDaChon", "");

        if (ngayDaChon.equals("") == false) {
            setNgayYeu(ngayDaChon);
            txtTenBan.setText(sharedPreferences.getString("TenBan",""));
            txtTenNguoiAy.setText(sharedPreferences.getString("TenNguoiAy",""));

            Intent serviceIntent = new Intent(this, NgayYeuServices.class);
            serviceIntent.putExtra("inputExtra", ngayDaChon);
            startService(serviceIntent);
        } else {
            Intent intent = new Intent(MainActivity.this, ChonNgay.class);
            startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
        }
        SharedPreferences myPrefrence = getPreferences(MODE_PRIVATE);
        String imageS = myPrefrence.getString("imagePreferance1", "");
        Bitmap imageNguoi1 = BitmapFactory.decodeResource(getResources(), R.drawable.imgnam);
        if (!imageS.equals("")) imageNguoi1 = decodeToBase64(imageS);
        imageViewNguoi1.setImageBitmap(imageNguoi1);

        imageS = myPrefrence.getString("imagePreferance2", "");
        Bitmap imageNguoi2 = BitmapFactory.decodeResource(getResources(), R.drawable.imagenu);
        if (!imageS.equals("")) imageNguoi2 = decodeToBase64(imageS);
        imageViewNguoi2.setImageBitmap(imageNguoi2);

        btnChonLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChonNgay.class);
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });

        imageViewNguoi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION1);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE1);
                    }
                }

            }
        });

        imageViewNguoi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSION2);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE2);
                    }
                }
            }
        });
    }

    private void anhXa() {
        btnChonLai = (Button) findViewById(R.id.buttonChonNgay);
        txtNgayYeu = (TextView) findViewById(R.id.textViewSoNgay);
        imageView = (ImageView) findViewById(R.id.imageViewTraiTim);
        imageViewNguoi1 = (ImageView) findViewById(R.id.imageViewNguoi1);
        imageViewNguoi2 = (ImageView) findViewById(R.id.imageViewNguoi2);
        txtTenNguoiAy = (TextView) findViewById(R.id.textViewTenAy);
        txtTenBan = (TextView) findViewById(R.id.textViewTenBan);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Get String data from Intent
                ngayDaChon = data.getStringExtra("Ngay");
                txtTenBan.setText(data.getStringExtra("TenBan"));
                txtTenNguoiAy.setText(data.getStringExtra("TenNguoiAy"));
                setNgayYeu(ngayDaChon);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NgayDaChon", ngayDaChon);
                editor.putString("TenBan", txtTenBan.getText().toString());
                editor.putString("TenNguoiAy", txtTenNguoiAy.getText().toString());
                editor.commit();
            }
        }
        if (requestCode == REQUEST_CODE_SELECT_IMAGE1 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 5000);
                        imageViewNguoi1.setImageBitmap(circularBitmap);

                        SharedPreferences myPrefrence = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefrence.edit();
                        editor.putString("imagePreferance1", encodeToBase64(circularBitmap));
                        editor.commit();
                    } catch (Exception exception) {
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        if (requestCode == REQUEST_CODE_SELECT_IMAGE2 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 5000);
                        imageViewNguoi2.setImageBitmap(circularBitmap);

                        SharedPreferences myPrefrence = getPreferences(MODE_PRIVATE);
                        SharedPreferences.Editor editor = myPrefrence.edit();
                        editor.putString("imagePreferance2", encodeToBase64(circularBitmap));
                        editor.commit();
                    } catch (Exception exception) {
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    protected void setNgayYeu(String ngayDaChon) {
        int ngay = Integer.parseInt(ngayDaChon.substring(0, 2));
        int thang = Integer.parseInt(ngayDaChon.substring(3, 5));
        int nam = Integer.parseInt(ngayDaChon.substring(6, 10));
        calendar = Calendar.getInstance();
        calendar.set(nam, thang - 1, ngay);
        long ngayYeu = (Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis()) / (1000 * 60 * 60 * 24);
        if (ngayYeu < 0) {
            Toast.makeText(MainActivity.this, "Hãy chọn ngày bé hơn ngày hiện tại", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ChonNgay.class);
            startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
        }
        txtNgayYeu.setText(String.valueOf(ngayYeu));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0) {
            if (requestCode == REQUEST_CODE_STORAGE_PERMISSION1) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE1);
                }
            }

            if (requestCode == REQUEST_CODE_STORAGE_PERMISSION2) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE2);
                }
            }
        } else {
            Toast.makeText(this, "Quyền bị từ chối...!!", Toast.LENGTH_SHORT).show();
        }

    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}