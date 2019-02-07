package com.example.johanmorales.controlturnossai;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.johanmorales.controlturnossai.Fragments.ConsultaFragment;
import com.example.johanmorales.controlturnossai.Fragments.IncomeRecentsFragment;
import com.example.johanmorales.controlturnossai.Models.Employee;
import com.example.johanmorales.controlturnossai.Models.RecentLog;
import com.example.johanmorales.controlturnossai.Models.Respuesta;
import com.example.johanmorales.controlturnossai.Models.Resultado;
import com.example.johanmorales.controlturnossai.Models.UtilsMainApp;
import com.example.johanmorales.controlturnossai.utils.ConnectivityReceiver;
import com.example.johanmorales.controlturnossai.utils.MyApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String CONSULTA_FRAGMENT_TAG = "consultaFragmentTAG";
    private static final String INCOME_RECENTS_FRAGMENT_TAG = "income_recents_fragment_tag";
    private static final int MY_SOCKET_TIMEOUT_MS = 20000;

    public TextView userNameTextViewToolBar;
    public TextView positionTextViewToolBar;
    public TextView filterUbicationTextViewToolBar;
    public View consultaProgressMain;

    public Respuesta respuesta;
    public Resultado resultado;
    public Employee empleado;
    public UtilsMainApp urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("SAI - Control Llegada");

        setSupportActionBar(toolbar);

        consultaProgressMain = findViewById(R.id.consulta_progress_main);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //------------------------------------------------------
        //bind navigation view
        View hView =  navigationView.getHeaderView(0);
        userNameTextViewToolBar = hView.findViewById(R.id.userNameTextViewToolBar);
        positionTextViewToolBar = hView.findViewById(R.id.positionTextViewToolBar);
        filterUbicationTextViewToolBar = hView.findViewById(R.id.filterUbicationTextViewToolBar);
        //------------------------------------------------------


        respuesta = (Respuesta) getIntent().getExtras().get("respuesta");
        resultado = (Resultado) getIntent().getExtras().get("resultado");
        empleado = (Employee) getIntent().getExtras().get("empleado");
        urls = (UtilsMainApp) getIntent().getExtras().get("urls");

        if(respuesta != null){

            Log.d(TAG,  "respuesta sigue: "+respuesta.getMessage());
            Log.d(TAG,  "resultado sigue: "+resultado.getToken());
            Log.d(TAG,  "empleado sigue: "+empleado.getFirstName());
            Log.d(TAG,  "empleado filterUbication: "+empleado.getFilterUbication());

            Log.d(TAG,  "url que debe ejecutar: "+urls.getHost());

            CharSequence name = empleado.getFirstName()+" "+empleado.getLastName();

            userNameTextViewToolBar.setText(name);
            positionTextViewToolBar.setText(empleado.getPosition());
            filterUbicationTextViewToolBar.setText(empleado.getFilterUbication());

            //initialize main fragment
            initMainFragment();
        }
        //------------------------------------------------------

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    public void initMainFragment(){

        Bundle bundle = new Bundle();

        bundle.putParcelable("resultado",resultado);
        bundle.putParcelable("empleado",empleado);
        bundle.putParcelable("urls", urls);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ConsultaFragment consultaFragment = new ConsultaFragment();
        consultaFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.contentMain, consultaFragment, CONSULTA_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    public void closeSession(){

        respuesta = null;

        Intent intentCloseSession = new Intent(this, LoginActivity.class);

        //se deberia tener una instancia de LoginActivity para poder dejar null la respuesta

        startActivity(intentCloseSession);
    }

    public void checkConnection(){
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    public void showSnack(boolean isConnected){

        String message;
        int color;

        if(isConnected){
            message = "Conectado a internet!";
            color = Color.GREEN;
        }else{
            message = "Conexion a internet perdida!";
            color = Color.RED;
        }

        Snackbar snack = Snackbar.make(findViewById(R.id.fab), message, Snackbar.LENGTH_LONG);
        View sbView = snack.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snack.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_close_session) {

            closeSession();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_recent_income) {
            // Handle the camera action

            //KeyboardUtils.hideKeyboard(this);

            Log.d(TAG,"Recientes en un fragment!");

            consultarLogsRecientes();

        }else if (id == R.id.nav_consultar){

            initMainFragment();

        } /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        Log.d(TAG, "La conexion cambió");

        checkConnection();
    }

    public void consultarLogsRecientes(){

        consultaProgressMain.setVisibility(View.VISIBLE);

        //------------------------------------------------------------------------------------------
        Toast.makeText(this, "Consultando Logs recientes.", Toast.LENGTH_LONG).show();
        //------------------------------------------------------------------------------------------

        final ArrayList<RecentLog> arrRecentLogs = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        String urlApi = urls.getHost().concat("api/recentLogs");

        JSONObject req = new JSONObject();

        try {
            req.put("token",resultado.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, urlApi, req, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {

                        //executeResponseJsonApp(response);

                        consultaProgressMain.setVisibility(View.GONE);

                        JSONObject res = response;

                        Log.d(TAG, "Status de la respuesta recentLogs: " + res.toString());

                        try {

                            JSONArray resArray = res.getJSONArray("result");

                            for(Integer i = 0; i < resArray.length(); i++){

                                JSONObject logJson;

                                RecentLog recentLog = new RecentLog();

                                logJson = (JSONObject) resArray.get(i);

                                recentLog.setName(logJson.getString("name"));
                                recentLog.setArrivingControlLogId(logJson.getInt("arrivingControlLogId"));
                                recentLog.setArrivingTime(logJson.getString("arrivingTime"));
                                recentLog.setArrivingAgent(logJson.getString("arrivingAgent"));
                                recentLog.setAgentTurn(logJson.getString("agentTurn"));
                                recentLog.setAbleToEnter(logJson.getBoolean("isAbleToEnter"));
                                recentLog.setDetail(logJson.getString("detail"));

                                Log.d(TAG, recentLog.getName());

                                arrRecentLogs.add(recentLog);
                            }


                            Bundle bundle = new Bundle();

                            bundle.putParcelableArrayList("recent_logs",arrRecentLogs);
                            bundle.putParcelable("urls",urls);
                            bundle.putParcelable("resultado",resultado);

                            //fragment manager, gestiona los elementos fragment
                            FragmentManager fragmentManager = getSupportFragmentManager(); //Support porque extiende de AppCompatibility
                            //ahora se crea un fragment transaction para poner el fragment sobre la actividad que se quiere
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); //se le da la instracciona al fragment masnager que se quiere iuniciar una trasaccion
                            //va contenedor en este caso el layout que contiene el contenido principal, luego el fragment, y un tag en este caso cualquier variable tipo string que identifique el fragment
                            IncomeRecentsFragment incomeRecentsFragment = new IncomeRecentsFragment();
                            //se setea los argumentos para el fragment en este caso un bundle con el array list de recientes
                            incomeRecentsFragment.setArguments(bundle);
                            //se añade la transaccion
                            fragmentTransaction.add(R.id.contentMain, incomeRecentsFragment, INCOME_RECENTS_FRAGMENT_TAG);
                            //se realiza la transacción
                            fragmentTransaction.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        Log.d(TAG, error.toString());

                        consultaProgressMain.setVisibility(View.GONE);

                        //despliegue del error en un toast
                        Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}
