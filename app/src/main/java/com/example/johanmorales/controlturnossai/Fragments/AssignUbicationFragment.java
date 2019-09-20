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
import android.widget.Button;
import android.widget.SearchView;

import com.example.johanmorales.controlturnossai.Adapters.AssignUbicationAdapter;
import com.example.johanmorales.controlturnossai.Events.CloseAssignUbicationFragment;
import com.example.johanmorales.controlturnossai.Models.Resultado;
import com.example.johanmorales.controlturnossai.Models.Ubication;
import com.example.johanmorales.controlturnossai.Models.UtilsMainApp;
import com.example.johanmorales.controlturnossai.R;
import com.example.johanmorales.controlturnossai.utils.ContainsString;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignUbicationFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String TAG = AssignUbicationFragment.class.getSimpleName();
    private static final int MY_SOCKET_TIMEOUT_MS = 20000;

    ArrayList<Ubication> ubicationArrayList;
    UtilsMainApp urls;
    Resultado resultado;
    RecyclerView recyclerView; //itineraryListRecyclerView

    Button buttonCancelAssign;
    SearchView ubicationSearchView;

    public AssignUbicationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para obtener los arguments
        Bundle bundle = getArguments();

        if (bundle != null) {
            ubicationArrayList = bundle.getParcelableArrayList("arrayUbications");
            urls = bundle.getParcelable("urls");
            resultado = bundle.getParcelable("resultado");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "Create view Assign Position");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assign_ubication, container, false);

        recyclerView = view.findViewById(R.id.ubicationListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        AssignUbicationAdapter assignUbicationAdapter = new AssignUbicationAdapter(ubicationArrayList,getContext(),urls,resultado);
        recyclerView.setAdapter(assignUbicationAdapter);/**/

        buttonCancelAssign = view.findViewById(R.id.buttonCancelAssign);
        buttonCancelAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "click cancel!");
                closeChangeStatusFragment();
            }
        });

        ubicationSearchView = view.findViewById(R.id.ubicationSearchView);
        ubicationSearchView.setQueryHint("Buscar Ubicación");
        ubicationSearchView.setOnQueryTextListener(this);

        return view;
    }

    public void closeChangeStatusFragment() {
        FragmentManager fm = ((AppCompatActivity) Objects.requireNonNull(getContext())).getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
            Log.i(TAG, "popping backstack");
        } else {
            Log.i(TAG, "nothing on backstack, calling super");
        }
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
    public void onMessageEvent(CloseAssignUbicationFragment closeAssignUbicationFragment) {
        closeChangeStatusFragment();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<Ubication> ubicationFiltered = new ArrayList<>();

        if( (ubicationArrayList != null) && (!newText.equals("")) ) {

            Log.d(TAG, "Buscando: " + newText);

            for (Ubication ubication : ubicationArrayList) {

                //Log.d(TAG, "Nombre: " + service.getPaxName());
                //Log.d(TAG, "Contiene: " + service.getPaxName().contains(text));

                if (ContainsString.containsIgnoreCase(ubication.getPosition(),newText)) {
                    ubicationFiltered.add(ubication);
                } else {
                    continue;
                }
            }

            putUbicationArrayList(ubicationFiltered);

        } else {
            putUbicationArrayList(ubicationFiltered);
        }

        return false;
    }

    private void putUbicationArrayList(ArrayList<Ubication> list) {

        AssignUbicationAdapter assignUbicationAdapter = new AssignUbicationAdapter(list,getContext(),urls,resultado);
        recyclerView.setAdapter(assignUbicationAdapter);/**/

    }
}
