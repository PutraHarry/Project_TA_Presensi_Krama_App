package com.harry.presensikrama.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.harry.presensikrama.R;
import com.harry.presensikrama.api.ApiRoute;
import com.harry.presensikrama.api.RetrofitSikramat;
import com.harry.presensikrama.model.CacahKramaMipil;
import com.harry.presensikrama.model.CacahKramaMipilDetailResponse;
import com.harry.presensikrama.model.KramaProfileGetResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailKramaActivity extends AppCompatActivity {

    private ImageView kramaImage;
    private LinearLayout kramaImageLoadingLayout;
    private TextView kramaName, kramaNic, kramaNik, kramaTempekan;

    private final String KRAMA_KEY = "KRAMA_KEY";
    private Gson gson = new Gson();

    private CacahKramaMipil cacahKramaMipil;

    private Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_krama);

        homeToolbar = findViewById(R.id.home_toolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cacahKramaMipil = gson.fromJson(getIntent().getStringExtra(KRAMA_KEY), CacahKramaMipil.class);

        kramaImage = findViewById(R.id.profile_image);
        kramaImageLoadingLayout = findViewById(R.id.profile_image_loading_container);
        kramaName = findViewById(R.id.detail_krama_nama_text);
        kramaNic = findViewById(R.id.detail_krama_nic_text);
        kramaNik = findViewById(R.id.detail_krama_nik_text);
        kramaTempekan = findViewById(R.id.detail_krama_tempekan_text);

        kramaName.setText(cacahKramaMipil.getPenduduk().getNama());
        kramaNic.setText(cacahKramaMipil.getNomorCacahKramaMipil());
        kramaTempekan.setText(cacahKramaMipil.getTempekan().getNamaTempekan());
        kramaNik.setText(cacahKramaMipil.getPenduduk().getNik());

        if (cacahKramaMipil.getPenduduk().getFoto() != null) {
            Picasso.get()
                    .load(getResources().getString(R.string.sikramat_endpoint) +
                            cacahKramaMipil.getPenduduk().getFoto())
                    .into(kramaImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            kramaImageLoadingLayout.setVisibility(View.GONE);
                            kramaImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(R.drawable.logo).into(kramaImage);
                            kramaImage.setVisibility(View.VISIBLE);
                            kramaImageLoadingLayout.setVisibility(View.GONE);
                        }
                    });
        }
        else {
            Picasso.get().load(R.drawable.logo).into(kramaImage);
            kramaImage.setVisibility(View.VISIBLE);
            kramaImageLoadingLayout.setVisibility(View.GONE);
        }

    }
}