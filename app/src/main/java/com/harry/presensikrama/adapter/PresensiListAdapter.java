package com.harry.presensikrama.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.harry.presensikrama.R;
import com.harry.presensikrama.activity.DetailPresensiActivity;
import com.harry.presensikrama.model.Presensi;

import java.util.ArrayList;

public class PresensiListAdapter extends RecyclerView.Adapter<PresensiListAdapter.ViewHolder> {
    private Context context;
    private final ArrayList<Presensi> presensiArrayList;
    private int position;
    private final String PRESENSI_KEY = "PRESENSI_KEY";
    private final Gson gson = new Gson();

    public PresensiListAdapter(Context context, ArrayList<Presensi> presensiArrayList) {
        this.context = context;
        this.presensiArrayList = presensiArrayList;
    }

    @NonNull
    @Override
    public PresensiListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.presensi_card_layout, parent, false);
        return new PresensiListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PresensiListAdapter.ViewHolder holder, int position) {
        holder.presensiName.setText(presensiArrayList.get(holder.getAdapterPosition()).getNamaPresensi());
        holder.presensiDesc.setText(presensiArrayList.get(holder.getAdapterPosition()).getKeterangan());
        holder.presensiDateOpen.setText(presensiArrayList.get(holder.getAdapterPosition()).getTglOpen());
        holder.presensiDateClose.setText(presensiArrayList.get(holder.getAdapterPosition()).getTglClose());

        if (presensiArrayList.get(holder.getAdapterPosition()).getIsOpen() == 0) {
            holder.presensiStatusChip.setText("Presensi Tertutup");
            holder.presensiStatusChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.secondaryLightColorSemiTransparent)));
        } else {
            holder.presensiStatusChip.setText("Presensi Terbuka");
            holder.presensiStatusChip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.greenSemiTransparent)));
        }

        holder.presensiDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Presensi presensi = presensiArrayList.get(holder.getAdapterPosition());
                Intent presensiDetail = new Intent(context, DetailPresensiActivity.class);
                presensiDetail.putExtra(PRESENSI_KEY, gson.toJson(presensi));
                context.startActivity(presensiDetail);
            }
        });
    }


    @Override
    public int getItemCount() {
        return presensiArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatTextView presensiName, presensiDesc, presensiDateOpen, presensiDateClose;
        private final Button presensiDetailButton;
        private final Chip presensiStatusChip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            presensiName = itemView.findViewById(R.id.presensi_card_title_text);
            presensiDesc = itemView.findViewById(R.id.presensi_card_desc_text);
            presensiDateOpen = itemView.findViewById(R.id.presensi_card_open_date_text);
            presensiDateClose = itemView.findViewById(R.id.presensi_card_close_date_text);
            presensiStatusChip = itemView.findViewById(R.id.status_presensi_chip);
            presensiDetailButton = itemView.findViewById(R.id.presensi_card_detail_button);
        }
    }
}