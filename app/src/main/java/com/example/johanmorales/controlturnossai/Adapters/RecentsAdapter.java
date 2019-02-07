package com.example.johanmorales.controlturnossai.Adapters;

//antes de crear el adapter hay que crear el layout para cada item que se va a mostrar,
// en este caso fragment_income_recents_item

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.johanmorales.controlturnossai.Fragments.IncomeRecentsFragment;
import com.example.johanmorales.controlturnossai.Fragments.LogsEmployeeFragment;
import com.example.johanmorales.controlturnossai.MainActivity;
import com.example.johanmorales.controlturnossai.Models.RecentLog;
import com.example.johanmorales.controlturnossai.Models.Resultado;
import com.example.johanmorales.controlturnossai.Models.UtilsMainApp;
import com.example.johanmorales.controlturnossai.R;
import com.example.johanmorales.controlturnossai.utils.FormatDateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//5. se extiende la clase de --> extends RecyclerView.Adapter<Nombredelaclase.MyViewHolder>

public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.MyViewHolder> {

    private static final String TAG = RecentsAdapter.class.getSimpleName();
    //7.se crea un ArrayList de tipo de objeto que se le va a pasar a la lista y una variable tipo Context
    ArrayList<RecentLog> recentLogsArrayList;
    UtilsMainApp urls;
    Resultado resultado;
    Context context;

    //9. se crea el constructor del adapter
    public RecentsAdapter(ArrayList<RecentLog> recentLogsArrayList, Context context, UtilsMainApp urls, Resultado resultado){

        this.recentLogsArrayList = recentLogsArrayList;
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

        View view = LayoutInflater.from(context).inflate(R.layout.fragment_income_recents_item, viewGroup, false);

        MyViewHolder recentsViewHolder = new MyViewHolder(view);

        return recentsViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {

        //10. se hace el bind de datos con la layout elemento por elemento
        final RecentLog recentObjt = recentLogsArrayList.get(i); //se obtiene el objeto del ArrayList en esa posicion

        Log.d(TAG,String.valueOf(i));
        Log.d(TAG,recentObjt.getName());
        Log.d(TAG,String.valueOf(recentObjt.isAbleToEnter()));

        myViewHolder.nameTextView.setText(recentObjt.getName()); //se setea el textView segun el valor
        myViewHolder.arrivingAgentTextView.setText(recentObjt.getArrivingAgent());
        myViewHolder.arrivingTimeTextView.setText(FormatDateUtil.getDateFormatted(recentObjt.getArrivingTime()));
        myViewHolder.agentTurnTextView.setText(FormatDateUtil.getDateFormatted(recentObjt.getAgentTurn()));
        myViewHolder.detailTextView.setText(recentObjt.getDetail().equals("") ? "-" : recentObjt.getDetail());

        myViewHolder.recentLogContentItem.setBackgroundResource(recentObjt.isAbleToEnter() ? R.color.recentLogSuccess : R.color.recentLogFail);

        this.setStylesUI(recentObjt.isAbleToEnter, myViewHolder);

        myViewHolder.btnGetLogEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG,"pulsando boton para: "+recentObjt.getName());
                getEmployeeDetail(recentObjt, myViewHolder);
            }
        });
    }

    private void getEmployeeDetail(final RecentLog recentObjt, final MyViewHolder myViewHolder){

        myViewHolder.btnGetLogEmployee.setText("Consultando...");

        //Toast.makeText(context, "Consultando Logs de "+recentObjt.getName(), Toast.LENGTH_SHORT).show();

        final ArrayList<RecentLog> arrRecentLogs = new ArrayList<>();

        final RequestQueue queue = Volley.newRequestQueue(context);

        String urlApi = urls.getHost().concat("api/arrivingAgentLogs?socialNumber=").concat(recentObjt.getArrivingAgent()).concat("&token="+resultado.getToken());

        Log.d(TAG, "url: " + urlApi);
        Log.d(TAG, "token: " + resultado.getToken());

        JSONObject req = new JSONObject();

        try {
            req.put("socialNumber", recentObjt.getArrivingAgent());
            req.put("dateStart", "2019-02-04");
            req.put("dateEnd", "2019-02-04");
            req.put("token", resultado.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "req: " + req.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlApi, req, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {

                        //executeResponseJsonApp(response);

                        myViewHolder.btnGetLogEmployee.setText("VER LOGS");

                        JSONObject res = response;

                        Log.d(TAG, "Status de la respuesta DETAIL EMPLOYEE: " + res.toString());

                        try {

                            JSONArray resArray = res.getJSONArray("result");

                            if(resArray.length() > 0) {

                                for (Integer i = 0; i < resArray.length(); i++) {

                                    JSONObject logJson;

                                    RecentLog recentLog = new RecentLog();

                                    logJson = (JSONObject) resArray.get(i);

                                    recentLog.setName(recentObjt.getName());
                                    recentLog.setArrivingControlLogId(logJson.getInt("arrivingControlLogId"));
                                    recentLog.setArrivingTime(logJson.getString("arrivingTime"));
                                    recentLog.setArrivingAgent(logJson.getString("arrivingAgent"));
                                    recentLog.setAgentTurn(logJson.getString("agentTurn"));
                                    recentLog.setAbleToEnter(logJson.getBoolean("isAbleToEnter"));
                                    recentLog.setDetail(logJson.getString("detail"));

                                    //Log.d(TAG, recentLog.getName());

                                    arrRecentLogs.add(recentLog);
                                }


                                Bundle bundle = new Bundle();

                                bundle.putParcelableArrayList("logs", arrRecentLogs);
                                bundle.putParcelable("urls", urls);
                                bundle.putParcelable("resultado", resultado);

                                //fragment manager, gestiona los elementos fragment
                                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager(); //Support porque extiende de AppCompatibility
                                //ahora se crea un fragment transaction para poner el fragment sobre la actividad que se quiere
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //se le da la instracciona al fragment masnager que se quiere iuniciar una trasaccion
                                //va contenedor en este caso el layout que contiene el contenido principal, luego el fragment, y un tag en este caso cualquier variable tipo string que identifique el fragment
                                LogsEmployeeFragment logsEmployeeFragment = new LogsEmployeeFragment();
                                //se setea los argumentos para el fragment en este caso un bundle con el array list de recientes
                                logsEmployeeFragment.setArguments(bundle);
                                //se añade la transaccion
                                fragmentTransaction.add(R.id.contentMainRecentLogs, logsEmployeeFragment, "logsEmployeeFragment");

                                //para poder regresar
                                fragmentTransaction.addToBackStack(null);

                                //se realiza la transacción
                                fragmentTransaction.commit();

                            }else{
                                Toast.makeText(context, "Este empleado no tiene logs recientes.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }/**/


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        Log.d(TAG, error.toString());

                        myViewHolder.btnGetLogEmployee.setText("VER LOGS");

                        //despliegue del error en un toast
                        Toast.makeText(context,error.toString(),Toast.LENGTH_SHORT).show();

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    @Override
    public int getItemCount() {
        //8. se regresa el size del ArrayList
        return recentLogsArrayList.size();
    }

    //1. primero se crea la clase viewHolder que extiende de RecyclerView.ViewHolder
    static public class MyViewHolder extends RecyclerView.ViewHolder{

        //3. se añaden los elementos del layout item
        public TextView nameTextView;
        public TextView arrivingAgentTextView;
        public TextView agentTurnTextView;
        public TextView arrivingTimeTextView;
        public TextView detailTextView;
        public LinearLayout recentLogContentItem;
        private Button btnGetLogEmployee;

        //2. se autocompleta el constructor
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //4. se hace el bind de los elementos del layout item desde la view itemView
            nameTextView = itemView.findViewById(R.id.nameTextView);
            arrivingAgentTextView = itemView.findViewById(R.id.arrivingAgentTextView);
            agentTurnTextView = itemView.findViewById(R.id.agentTurnTextView);
            arrivingTimeTextView = itemView.findViewById(R.id.arrivingTimeTextView);
            detailTextView = itemView.findViewById(R.id.detailTextView);
            recentLogContentItem = itemView.findViewById(R.id.recentLogContentItem);
            btnGetLogEmployee = itemView.findViewById(R.id.btnGetLogEmployee);
        }
    }

    public void setStylesUI(Boolean ableToEnter, MyViewHolder myViewHolder){

        int colorSuccess = ContextCompat.getColor(context, R.color.recentLogFontSuccess);
        int colorFail = ContextCompat.getColor(context, R.color.recentLogFontFail);

        myViewHolder.nameTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        myViewHolder.arrivingAgentTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        myViewHolder.arrivingTimeTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        myViewHolder.agentTurnTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        myViewHolder.detailTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
    }
}
