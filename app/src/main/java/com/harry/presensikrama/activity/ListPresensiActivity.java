package com.harry.presensikrama.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.harry.presensikrama.R;
import com.harry.presensikrama.adapter.PresensiListAdapter;
import com.harry.presensikrama.api.ApiRoute;
import com.harry.presensikrama.api.RetrofitClient;
import com.harry.presensikrama.model.Presensi;
import com.harry.presensikrama.model.PresensiGetResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPresensiActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton tambahPresensiFab;
    RecyclerView presensiList;
    ArrayList<Presensi> presensiArrayList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    PresensiListAdapter presensiListAdapter;

    SwipeRefreshLayout presensiContainer;

    private final String KRAMA_KEY = "KRAMA_KEY";

    private Toolbar homeToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_presensi);

        homeToolbar = findViewById(R.id.home_toolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        presensiList = findViewById(R.id.presensi_recycler);
        presensiContainer = findViewById(R.id.presensi_container);

        linearLayoutManager = new LinearLayoutManager(this);
        presensiListAdapter = new PresensiListAdapter(this, presensiArrayList);
        presensiList.setLayoutManager(linearLayoutManager);
        presensiList.setAdapter(presensiListAdapter);

        presensiContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(getIntent().getStringExtra(KRAMA_KEY));
            }
        });

        getData(getIntent().getStringExtra(KRAMA_KEY));
    }

    public void getData(String noCacahKramaMipil) {
        presensiContainer.setRefreshing(true);
        ApiRoute getData = RetrofitClient.buildRetrofit().create(ApiRoute.class);
        Call<PresensiGetResponse> presensiGetResponseCall = getData.getPresensiTerdaftar(noCacahKramaMipil);
        presensiGetResponseCall.enqueue(new Callback<PresensiGetResponse>() {
            @Override
            public void onResponse(Call<PresensiGetResponse> call, Response<PresensiGetResponse> response) {
                if (response.code() == 200 && response.body().getStatus().equals(true)) {
                    presensiArrayList.clear();
                    presensiArrayList.addAll(response.body().getPresensiList());
                    presensiListAdapter.notifyDataSetChanged();
                }
                else {
                    Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                            "kegiatan gagal", Snackbar.LENGTH_SHORT).show();
                }
                presensiContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PresensiGetResponse> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                        "presensi gagal", Snackbar.LENGTH_SHORT).show();
                presensiContainer.setRefreshing(false);
            }
        });
    }
}