package com.example.johanmorales.controlturnossai.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johanmorales.controlturnossai.Adapters.RecentsAdapter;
import com.example.johanmorales.controlturnossai.Events.CloseIncomeRecentsFragment;
import com.example.johanmorales.controlturnossai.Models.RecentLog;
import com.example.johanmorales.controlturnossai.Models.Resultado;
import com.example.johanmorales.controlturnossai.Models.UtilsMainApp;
import com.example.johanmorales.controlturnossai.R;
import com.example.johanmorales.controlturnossai.utils.ContainsString;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;


public class IncomeRecentsFragment extends Fragment implements SearchView.OnQueryTextListener{

    private static final String TAG = IncomeRecentsFragment.class.getSimpleName();
    ArrayList<RecentLog> recents;
    UtilsMainApp urls;
    Resultado resultado;
    RecyclerView recyclerView;

    SearchView socialNumberSearchView;
    TextView textResults;

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

        textResults = view.findViewById(R.id.textResults);
        textResults.setText(String.format("Mostrando %s logs recientes.", recents.size()));

        RecentsAdapter recentsAdapter = new RecentsAdapter(recents,getContext(),urls,resultado);

        recyclerView.setAdapter(recentsAdapter);

        socialNumberSearchView = view.findViewById(R.id.socialNumberSearchView);
        socialNumberSearchView.setOnQueryTextListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(CloseIncomeRecentsFragment closeIncomeRecentsFragment) {
        closeIncomeFragment();
    }

    public void closeIncomeFragment() {
        FragmentManager fm = ((AppCompatActivity) Objects.requireNonNull(getContext())).getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            Log.i(TAG, "popping backstack");
        } else {
            Log.i(TAG, "nothing on backstack, calling super");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        ArrayList<RecentLog> recentsFiltered = new ArrayList<>();

        if( (recents != null) && (!newText.equals("")) ) {

            Log.d(TAG, "Buscando: " + newText);

            for (RecentLog recent : recents) {

                if (ContainsString.containsIgnoreCase(recent.getArrivingAgent(),newText)) {
                    recentsFiltered.add(recent);
                } else {
                    continue;
                }
            }

            putRecentsArrayList(recentsFiltered);

        } else {
            putRecentsArrayList(recents);
        }

        return false;
    }

    private void putRecentsArrayList(ArrayList<RecentLog> list) {

        RecentsAdapter recentsAdapter = new RecentsAdapter(list,getContext(),urls,resultado);
        recyclerView.setAdapter(recentsAdapter);/**/
        textResults.setText(String.format("Mostrando %s logs recientes.", list.size()));
    }

}
