package com.example.johanmorales.controlturnossai.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.johanmorales.controlturnossai.Adapters.RecentsAdapter;
import com.example.johanmorales.controlturnossai.Models.RecentLog;
import com.example.johanmorales.controlturnossai.Models.Resultado;
import com.example.johanmorales.controlturnossai.Models.UtilsMainApp;
import com.example.johanmorales.controlturnossai.R;

import java.util.ArrayList;


public class IncomeRecentsFragment extends Fragment {

    private static final String TAG = IncomeRecentsFragment.class.getSimpleName();
    ArrayList<RecentLog> recents;
    UtilsMainApp urls;
    Resultado resultado;
    RecyclerView recyclerView;

    public IncomeRecentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para obtener los arguments
        Bundle bundle = getArguments();

        recents = bundle.getParcelableArrayList("recent_logs");
        urls = bundle.getParcelable("urls");
        resultado = bundle.getParcelable("resultado");

        //Toast.makeText(getContext(), "10 primeros logs. ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //se instancia el layout en donde va a aparecer
        View view = inflater.inflate(R.layout.fragment_income_recents, container, false);

        //se instancia el recyclerView en donde se va a mostrar la lista
        recyclerView = view.findViewById(R.id.recentListRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        RecentsAdapter recentsAdapter = new RecentsAdapter(recents,getContext(),urls,resultado);

        recyclerView.setAdapter(recentsAdapter);

        return view;
    }

}
