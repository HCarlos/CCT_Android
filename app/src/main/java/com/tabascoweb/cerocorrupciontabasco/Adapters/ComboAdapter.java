package com.tabascoweb.cerocorrupciontabasco.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tabascoweb.cerocorrupciontabasco.Interfaces.AsyncTaskListener;
import com.tabascoweb.cerocorrupciontabasco.Classes.Opciones;

import java.util.ArrayList;

public class ComboAdapter extends ArrayAdapter<Opciones> {

    private Context context;
    private ArrayList<Opciones> opciones;
    private AsyncTaskListener listener;

    public ComboAdapter(Context context, int textViewResourceId,
                        ArrayList<Opciones> opciones) {
        super(context, textViewResourceId, opciones);
        this.context = context;
        this.opciones = opciones;
    }

    @Override
    public int getCount(){
        return opciones.size();
    }

    @Override
    public Opciones getItem(int position){
        return opciones.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        final Opciones opt = opciones.get(position);
        label.setTextColor(Color.BLACK);
        label.setText(opt.getValue());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setGravity(Gravity.CENTER_VERTICAL);

        final Opciones opt = opciones.get(position);
        label.setText(opt.getValue());

        return label;
    }



    
    

}
