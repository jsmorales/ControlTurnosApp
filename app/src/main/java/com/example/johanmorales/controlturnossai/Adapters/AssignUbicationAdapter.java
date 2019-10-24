package com.example.johanmorales.controlturnossai.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.johanmorales.controlturnossai.Events.CloseAssignUbicationFragment;
import com.example.johanmorales.controlturnossai.Events.UbicationSelectedEvent;
import com.example.johanmorales.controlturnossai.Handler.MyDBHandler;
import com.example.johanmorales.controlturnossai.Models.Resultado;
import com.example.johanmorales.controlturnossai.Models.Ubication;
import com.example.johanmorales.controlturnossai.Models.UtilsMainApp;
import com.example.johanmorales.controlturnossai.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

//5. se extiende la clase de --> extends RecyclerView.Adapter<Nombredelaclase.MyViewHolder>
public class AssignUbicationAdapter extends RecyclerView.Adapter<AssignUbicationAdapter.MyViewHolder> {

    private static final String TAG = AssignUbicationAdapter.class.getSimpleName();
    private static final int MY_SOCKET_TIMEOUT_MS = 20000;

    //7.se crea un ArrayList de tipo de objeto que se le va a pasar a la lista y una variable tipo Context
    ArrayList<Ubication> ubicationArrayList;
    UtilsMainApp urls;
    Resultado resultado;
    Context context;

    //9. se crea el constructor del adapter
    public AssignUbicationAdapter(ArrayList<Ubication> ubicationArrayList, Context context, UtilsMainApp urls, Resultado resultado){
        this.ubicationArrayList = ubicationArrayList;
        this.urls = urls;
        this.resultado = resultado;
        this.context = context;
    }

    //6. se implementan los metodos con alt + enter
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //11. se instancia el inflater con el layout que se va a ocupar por cada item mas el layout que ocupa el contexto
        //y se retorna esta vista instanciando la vista view holder

        Log.d(TAG, ubicationArrayList.toString());

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_assign_ubication_item, viewGroup, false);

        MyViewHolder assignViewHolder = new MyViewHolder(view);

        return assignViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        //10. se hace el bind de datos con la layout elemento por elemento
        final Ubication ubicationObjt = ubicationArrayList.get(i); //se obtiene el objeto del ArrayList en esa posicion

        myViewHolder.ubicationTextView.setText(String.format("%s", ubicationObjt.getPosition()));
        myViewHolder.selectUbicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Click seleccionar desde adapter" + ubicationObjt.getId());
                EventBus.getDefault().post(new UbicationSelectedEvent(ubicationObjt));
                EventBus.getDefault().post(new CloseAssignUbicationFragment());
            }
        });
    }

    @Override
    public int getItemCount() {
        //8. se regresa el size del ArrayList
        return ubicationArrayList.size();
    }

    //1. primero se crea la clase viewHolder que extiende de RecyclerView.ViewHolder
    static public class MyViewHolder extends RecyclerView.ViewHolder{

        //3. se añaden los elementos del layout item
        public TextView ubicationTextView;
        public Button selectUbicationButton;

        //2. se autocompleta el constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //4. se hace el bind de los elementos del layout item desde la view itemView
            ubicationTextView = itemView.findViewById(R.id.ubicationTextView);
            selectUbicationButton = itemView.findViewById(R.id.selectUbicationButton);

        }
    }
}
