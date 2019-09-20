package com.example.johanmorales.controlturnossai.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johanmorales.controlturnossai.Adapters.LogsAdapter;
import com.example.johanmorales.controlturnossai.Models.RecentLog;
import com.example.johanmorales.controlturnossai.Models.Resultado;
import com.example.johanmorales.controlturnossai.Models.UtilsMainApp;
import com.example.johanmorales.controlturnossai.R;

import java.util.ArrayList;

public class LogsEmployeeFragment extends Fragment {

    ArrayList<RecentLog> recents;
    UtilsMainApp urls;
    Resultado resultado;
    RecyclerView recyclerView;

    private TextView nameAgentTurnTextView;
    private TextView socialNumberTextView;

    public LogsEmployeeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para obtener los arguments
        Bundle bundle = getArguments();

        recents = bundle.getParcelableArrayList("logs");
        urls = bundle.getParcelable("urls");
        resultado = bundle.getParcelable("resultado");

        Log.d("Logs fragment: ", recents.get(0).getArrivingAgent());
        Log.d("Logs fragment: ", recents.get(0).getName());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //se instancia el layout en donde va a aparecer
        View view = inflater.inflate(R.layout.fragment_logs_employee, container, false);

        //se instancia el recyclerView en donde se va a mostrar la lista
        recyclerView = view.findViewById(R.id.logListRecyclerView);
        nameAgentTurnTextView = view.findViewById(R.id.nameAgentTurnTextView);
        socialNumberTextView = view.findViewById(R.id.socialNumberTextView);

        nameAgentTurnTextView.setText(recents.get(0).getName());
        socialNumberTextView.setText(recents.get(0).getArrivingAgent());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        LogsAdapter logsAdapter = new LogsAdapter(recents,getContext());

        recyclerView.setAdapter(logsAdapter);

        return view;
    }

}
