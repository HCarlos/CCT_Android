package com.tabascoweb.cerocorrupciontabasco.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tabascoweb.cerocorrupciontabasco.Activitys.MiDenunciaActivity;
import com.tabascoweb.cerocorrupciontabasco.Classes.Opciones;
import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;
import com.tabascoweb.cerocorrupciontabasco.R;

import java.util.ArrayList;

public class MisDenunciasAdapter extends RecyclerView.Adapter<MisDenunciasAdapter.OpcionesViewHolder> {
    private static final String TAG = "RESPUESTA OP";
    private ArrayList<Opciones> Op;
    private Activity activity;
    private Context context;

    public MisDenunciasAdapter(Activity activity, Context context, ArrayList<Opciones> _Op) {
        this.activity = activity;
        this.context = context;
        this.Op = _Op;
    }

    @Override
    public OpcionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mis_denuncias, parent, false);
        return new OpcionesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final OpcionesViewHolder holder, int position) {
        final Opciones op = Op.get(position);

        holder.data = op.getKey();
        holder.label = op.getValue();
        holder.tvTitulo.setText("FOLIO: UIPE-"+String.format("%06d",op.getKey() )+"-TAB");
        holder.tvDetalle.setText(op.getVString1());

        holder.tvTitulo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIntentDenuncias(holder);
            }
        });

        holder.tvDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIntentDenuncias(holder);
            }
        });

        holder.btnGetDenuncia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIntentDenuncias(holder);
            }
        });

        holder.leggendLLCrv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIntentDenuncias(holder);
            }
        });


    }

    @Override
    public int getItemCount() {
        return Op.size();
    }

    public static class OpcionesViewHolder extends RecyclerView.ViewHolder{

        int data;
        String label;
        String VString1;
        TextView tvTitulo;
        TextView tvDetalle;
        ImageButton btnGetDenuncia;
        LinearLayout leggendLLCrv2;

        public OpcionesViewHolder(View itemView) {
            super(itemView);
            data = 0;
            label = "";
            VString1 = "";
            tvTitulo = (TextView) itemView.findViewById(R.id.tvTitulo);
            tvDetalle = (TextView) itemView.findViewById(R.id.tvDetalle);
            btnGetDenuncia = (ImageButton) itemView.findViewById(R.id.btnGetDenuncia);
            leggendLLCrv2 = (LinearLayout) itemView.findViewById(R.id.leggendLLCrv2);

        }

    }


    private void getIntentDenuncias(OpcionesViewHolder holder){
        Singleton.setIdDenuncia(holder.data);
        Intent intent = new Intent(activity, MiDenunciaActivity.class);
        activity.startActivity(intent);
    }
}
