package com.tabascoweb.cerocorrupciontabasco.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tabascoweb.cerocorrupciontabasco.Activitys.WebViewActivity;
import com.tabascoweb.cerocorrupciontabasco.Classes.AppConfig;
import com.tabascoweb.cerocorrupciontabasco.Classes.Opciones;
import com.tabascoweb.cerocorrupciontabasco.Classes.Singleton;
import com.tabascoweb.cerocorrupciontabasco.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MisArchivosAdapter extends RecyclerView.Adapter<MisArchivosAdapter.OpcionesViewHolder> {
    private static final String TAG = "RESPUESTA OP";
    private ArrayList<Opciones> Op;
    private Activity activity;
    private Context context;


    public MisArchivosAdapter(Activity activity, Context context, ArrayList<Opciones> _Op) {
        this.activity = activity;
        this.context = context;
        this.Op = _Op;
    }


    @Override
    public MisArchivosAdapter.OpcionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_mis_archivos, parent, false);
        return new MisArchivosAdapter.OpcionesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final OpcionesViewHolder holder, int position) {
        if (position >= 0){

        final Opciones op = Op.get(position);

            holder.data = op.getKey();
            holder.label = op.getValue();
            holder.VString1 = op.getVString1();
            holder.VInteger1 = op.getVInteger1();
            Singleton.setArchivo(holder.label);
            holder.Url = AppConfig.getURLImagenArchivo();
            Singleton.setPathFile(holder.Url);

            String[] parts = holder.label.split("\\.");
            holder.Extension = parts[1];
            switch (holder.Extension){
                case "jpg":
                    Picasso
                            .get()
                            .load(Singleton.getPathFile())
                            .placeholder(R.drawable.ic_image_tizne_azul_96dp)
                            .into(holder.imageArchivo);
                    break;
                case "mp4":
                    holder.imageArchivo.setImageResource(R.drawable.ic_video_library_tizne_202dp);
                    break;
                case "mp3":
                    holder.imageArchivo.setImageResource(R.drawable.ic_queue_audio_white_202dp);
                    break;
                default:
                    holder.imageArchivo.setImageResource(R.drawable.ic_file_white_202dp);
                    break;

            }

            holder.fileName.setText(holder.label);

            holder.imageArchivo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Singleton.setIdArchivo(holder.data);
                    Intent intent = new Intent(activity, WebViewActivity.class);
                    intent.putExtra("Url", holder.Url );
                    intent.putExtra("Extension", holder.Extension );
                    activity.startActivity(intent);
                }
            });

        }

    }


    @Override
    public int getItemCount() {
        return Op.size();
    }

    public static class OpcionesViewHolder extends RecyclerView.ViewHolder {

        int data;
        String label;
        String VString1;
        Integer VInteger1;
        ImageView imageArchivo;
        TextView fileName;
        String Extension;
        String Url;

        public OpcionesViewHolder(View itemView) {
            super(itemView);
            data = 0;
            label = "";
            VString1 = "";
            VInteger1 = 0;
            Extension = "";
            Url = "";
            imageArchivo = (ImageView) itemView.findViewById(R.id.imageArchivo);
            fileName = (TextView) itemView.findViewById(R.id.fileName);
        }

    }

}