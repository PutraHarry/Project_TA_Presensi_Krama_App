package com.harry.presensikrama.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.harry.presensikrama.R;
import com.harry.presensikrama.adapter.PresensiListAdapter;
import com.harry.presensikrama.api.ApiRoute;
import com.harry.presensikrama.api.RetrofitClient;
import com.harry.presensikrama.model.CacahKramaMipil;
import com.harry.presensikrama.model.KramaProfileGetResponse;
import com.harry.presensikrama.model.Presensi;
import com.harry.presensikrama.model.PresensiDetailResponse;
import com.harry.presensikrama.model.PresensiGetResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {


    private MaterialCardView kegiatanBanjarButton, presensiBanjarButton, kramaBanjarButton;

    private ImageView kramaImage;
    private LinearLayout kramaImageLoadingLayout;
    private TextView kramaName, kramaNic;
    private Button kramaDetail, kramaLogout;

    private TextView noPresensi;
    private RecyclerView presensiList;
    private PresensiListAdapter presensiListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Presensi> presensiArrayList = new ArrayList<>();

    private SharedPreferences loginPref;

    private SwipeRefreshLayout homeContainer;

    private final String KRAMA_KEY = "KRAMA_KEY";

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginPref = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        homeContainer = findViewById(R.id.home_container);

        kramaImage = findViewById(R.id.profile_image);
        kramaImageLoadingLayout = findViewById(R.id.profile_image_loading_container);
        kramaName = findViewById(R.id.home_krama_nama_text);
        kramaNic = findViewById(R.id.home_krama_nic_text);
        kramaDetail = findViewById(R.id.home_krama_detail_profile_button);
        kramaLogout = findViewById(R.id.home_logout_button);

        kramaLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginPref.getInt("status", 0) != 0) {
                    SharedPreferences.Editor loginPrefEditor = loginPref.edit();
                    loginPrefEditor.putInt("status", 0);
                    loginPrefEditor.putString("token", "empty");
                    loginPrefEditor.apply();
                }
                Toast.makeText(getApplicationContext(), "Logout berhasil", Toast.LENGTH_SHORT).show();
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                finishAffinity();
            }
        });

        presensiList = findViewById(R.id.presensi_recycler);
        noPresensi = findViewById(R.id.home_no_presensi_open_text);

        presensiListAdapter = new PresensiListAdapter(this, presensiArrayList);
        linearLayoutManager = new LinearLayoutManager(this);
        presensiList.setLayoutManager(linearLayoutManager);
        presensiList.setAdapter(presensiListAdapter);


        presensiBanjarButton = findViewById(R.id.home_presensi_daftar_button);
        presensiBanjarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent presensiIntent = new Intent(getApplicationContext(), ListPresensiActivity.class);
                startActivity(presensiIntent);
            }
        });

        kramaDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kramaIntent = new Intent(getApplicationContext(), DetailKramaActivity.class);
                startActivity(kramaIntent);
            }
        });

        homeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getKrama(loginPref.getString("token", "empty"));
            }
        });

        getKrama(loginPref.getString("token", "empty"));
    }

    public void getKrama(String token) {
        homeContainer.setRefreshing(true);
        ApiRoute apiRoute = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<KramaProfileGetResponse> kramaProfileGetResponseCall = apiRoute.getProfileKrama(token);
        kramaProfileGetResponseCall.enqueue(new Callback<KramaProfileGetResponse>() {
            @Override
            public void onResponse(Call<KramaProfileGetResponse> call, Response<KramaProfileGetResponse> response) {
                homeContainer.setRefreshing(false);
                if (response.code() == 200 && response.body().getStatusCode() == 200
                        && response.body().getMessage().equals("data penduduk sukses")) {

                    kramaName.setText(response.body().getPenduduk().getNama());
                    kramaNic.setText(response.body().getCacahKramaMipil().getNomorCacahKramaMipil());
                    getPresensi(response.body().getCacahKramaMipil().getNomorCacahKramaMipil());
                    presensiBanjarButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent presensiBanjarIntent = new Intent(getApplicationContext(), ListPresensiActivity.class);
                            presensiBanjarIntent.putExtra(KRAMA_KEY, response.body().getCacahKramaMipil().getNomorCacahKramaMipil());
                            startActivity(presensiBanjarIntent);
                        }
                    });

                    kramaDetail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent kramaDetailIntent = new Intent(getApplicationContext(), DetailKramaActivity.class);
                            CacahKramaMipil cacahKramaMipil = response.body().getCacahKramaMipil();
                            cacahKramaMipil.setPenduduk(response.body().getPenduduk());
                            kramaDetailIntent.putExtra(KRAMA_KEY, gson.toJson(cacahKramaMipil));
                            startActivity(kramaDetailIntent);
                        }
                    });

                    if (response.body().getPenduduk().getFoto() != null) {
                        Picasso.get()
                                .load(getResources().getString(R.string.sikramat_endpoint) +
                                        response.body().getPenduduk().getFoto())
                                .into(kramaImage, new com.squareup.picasso.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        kramaImageLoadingLayout.setVisibility(View.GONE);
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get().load(R.drawable.logo).into(kramaImage);
                                    }
                                });
                    }
                    else {
                        Picasso.get().load(R.drawable.logo).into(kramaImage);
                    }

                } else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "Gagal memuat data Krama. Silahkan coba lagi nanti.", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KramaProfileGetResponse> call, Throwable t) {
                homeContainer.setRefreshing(false);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "Gagal memuat data Krama. Silahkan coba lagi nanti.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void getPresensi(String nic) {
        homeContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PresensiGetResponse> presensiDetailResponseCall = getData.getPresensiOpen(nic);
        presensiDetailResponseCall.enqueue(new Callback<PresensiGetResponse>() {
            @Override
            public void onResponse(Call<PresensiGetResponse> call, Response<PresensiGetResponse> response) {
                homeContainer.setRefreshing(false);
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    if (response.body().getPresensiList().size() != 0) {
                        noPresensi.setVisibility(View.GONE);
                        presensiList.setVisibility(View.VISIBLE);
                        presensiArrayList.clear();
                        presensiArrayList.addAll(response.body().getPresensiList());
                        presensiListAdapter.notifyDataSetChanged();
                    } else {
                        noPresensi.setVisibility(View.VISIBLE);
                        presensiList.setVisibility(View.GONE);
                    }
                }
                else {
                    noPresensi.setVisibility(View.VISIBLE);
                    presensiList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PresensiGetResponse> call, Throwable t) {
                homeContainer.setRefreshing(false);
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "gagal memuat data presensi open", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

}