package com.example.johanmorales.controlturnossai.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.johanmorales.controlturnossai.Models.RecentLog;
import com.example.johanmorales.controlturnossai.R;
import com.example.johanmorales.controlturnossai.utils.FormatDateUtil;
import com.example.johanmorales.controlturnossai.utils.TimeConverter;

import java.util.ArrayList;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.MyViewHolder> {

    //7.se crea un ArrayList de tipo de objeto que se le va a pasar a la lista y una variable tipo Context
    ArrayList<RecentLog> recentLogsArrayList;
    Context context;

    public LogsAdapter(ArrayList<RecentLog> recentLogsArrayList, Context context){

        this.recentLogsArrayList = recentLogsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        //11. se instancia el inflater con el layout que se va a ocupar por cada item mas el layout que ocupa el contexto
        //y se retorna esta vista instanciando la vista view holder

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_log_employee_item, viewGroup, false);

        MyViewHolder recentsViewHolder = new MyViewHolder(view);

        return recentsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        //10. se hace el bind de datos con la layout elemento por elemento
        final RecentLog recentObjt = recentLogsArrayList.get(i); //se obtiene el objeto del ArrayList en esa posicion

        myViewHolder.arrivingTimeTextView.setText(FormatDateUtil.getDateFormatted(recentObjt.getArrivingTime()));
        myViewHolder.agentTurnTextView.setText(FormatDateUtil.getDateFormatted(recentObjt.getAgentTurn()));
        myViewHolder.detailTextView.setText(recentObjt.getDetail().equals("") ? "-" : recentObjt.getDetail());

        myViewHolder.recentLogContentItem.setBackgroundResource(recentObjt.isAbleToEnter() ? R.color.recentLogSuccess : R.color.recentLogFail);

        myViewHolder.dateDiffTextView.setText(TimeConverter.getMinsConverted(recentObjt.getDateDiff()));

        this.setStylesUI(recentObjt.isAbleToEnter, myViewHolder);
    }

    private void setStylesUI(Boolean ableToEnter, LogsAdapter.MyViewHolder myViewHolder){

        int colorSuccess = ContextCompat.getColor(context, R.color.recentLogFontSuccess);
        int colorFail = ContextCompat.getColor(context, R.color.recentLogFontFail);

        myViewHolder.arrivingTimeTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        myViewHolder.agentTurnTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        myViewHolder.detailTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        myViewHolder.dateDiffTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
    }

    @Override
    public int getItemCount() {
        return recentLogsArrayList.size();
    }

    static public class MyViewHolder extends RecyclerView.ViewHolder{

        //3. se añaden los elementos del layout item
        public TextView agentTurnTextView;
        public TextView arrivingTimeTextView;
        public TextView detailTextView;
        public LinearLayout recentLogContentItem;
        public TextView dateDiffTextView;

        //2. se autocompleta el constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //4. se hace el bind de los elementos del layout item desde la view itemView
            agentTurnTextView = itemView.findViewById(R.id.agentTurnTextView);
            arrivingTimeTextView = itemView.findViewById(R.id.arrivingTimeTextView);
            detailTextView = itemView.findViewById(R.id.detailTextView);
            recentLogContentItem = itemView.findViewById(R.id.recentLogContentItem);
            dateDiffTextView = itemView.findViewById(R.id.dateDiffTextView);
        }
    }

}
