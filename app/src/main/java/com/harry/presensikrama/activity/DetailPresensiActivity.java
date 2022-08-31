package com.harry.presensikrama.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.harry.presensikrama.R;
import com.harry.presensikrama.model.Presensi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class DetailPresensiActivity extends AppCompatActivity {

    private TextView presensiTitleText, presensiDescText, presensiDateOpenText,
            presensiDateCloseText, presensiKegiatanName, presensiKegiatanDesc, presensiFilledText;

    private Chip presensiStatusChip;

    private Presensi presensi;
    private Gson gson = new Gson();

    private final String PRESENSI_KEY = "PRESENSI_KEY";

    private Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_presensi);

        homeToolbar = findViewById(R.id.home_toolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        presensi = gson.fromJson(getIntent().getStringExtra(PRESENSI_KEY), Presensi.class);

        presensiTitleText = findViewById(R.id.presensi_detail_title_text);
        presensiDescText = findViewById(R.id.presensi_detail_desc_text);
        presensiDateOpenText = findViewById(R.id.presensi_detail_open_date_text);
        presensiDateCloseText = findViewById(R.id.presensi_detail_close_date_text);
        presensiKegiatanName = findViewById(R.id.presensi_detail_kegiatan_name_text);
        presensiKegiatanDesc = findViewById(R.id.presensi_detail_kegiatan_desc_text);
        presensiStatusChip = findViewById(R.id.status_presensi_chip);
        presensiFilledText = findViewById(R.id.presensi_detail_filled_date_text);

        presensiTitleText.setText(presensi.getNamaPresensi());
        presensiDescText.setText(presensi.getKeterangan());
        presensiDateOpenText.setText(presensi.getTglOpen());
        presensiDateCloseText.setText(presensi.getTglClose());
        presensiKegiatanName.setText(presensi.getKegiatan().getNamaKegiatan());
        presensiKegiatanDesc.setText(presensi.getKegiatan().getKeterangan());

        if (presensi.getIsOpen() == 0) {
            presensiStatusChip.setText("Presensi Tertutup");
            presensiStatusChip.setChipBackgroundColor(ColorStateList.valueOf(
                    ContextCompat.getColor(getApplicationContext(), R.color.secondaryLightColorSemiTransparent)));
        } else {
            presensiStatusChip.setText("Presensi Terbuka");
            presensiStatusChip.setChipBackgroundColor(ColorStateList.valueOf(
                    ContextCompat.getColor(getApplicationContext(), R.color.greenSemiTransparent)));
        }

        if (presensi.getPresensiDetail().getIsHadir() == 0) {
            presensiFilledText.setText("Anda belum mengisi presensi ini");
        } else {
            presensiFilledText.setText(changeDateTimeFormatForCreatedAt(presensi.getPresensiDetail().getUpdatedAt()));
        }
    }

    public static String changeDateTimeFormatForCreatedAt (String time) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(time);

            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMMM-yyyy, HH:mm"); //this format changeable
            dateFormatter.setTimeZone(TimeZone.getDefault());
            time = dateFormatter.format(value);
        }
        catch (Exception e)
        {
            time = "00-00-0000 00:00";
        }
        Log.d("tanggal", time);
        return time;
    }
}