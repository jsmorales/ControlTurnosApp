package com.example.johanmorales.controlturnossai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.johanmorales.controlturnossai.Models.Employee;
import com.example.johanmorales.controlturnossai.Models.Respuesta;
import com.example.johanmorales.controlturnossai.Models.Resultado;
import com.example.johanmorales.controlturnossai.Models.UtilsMainApp;
import com.example.johanmorales.controlturnossai.utils.Md5Manager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Constantes
    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String REGION = "America/Bogota";
    private static final int MY_SOCKET_TIMEOUT_MS = 200000;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    //private Spinner mFilterUbicationSpinner;
    private ImageView logoInit;

    public String user;
    public String password;
    public String region = REGION;
    public TextView versionTextView;
    //public String filterUbication;


    public Respuesta respuesta;
    private Resultado resultado;
    private Employee empleado;

    private UtilsMainApp urls = new UtilsMainApp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        //mFilterUbicationSpinner = findViewById(R.id.filterUbicationSpinner);
        logoInit = findViewById(R.id.logoInit);

        versionTextView = findViewById(R.id.versionTextView);

        //fill the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.filter_ubications_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //mFilterUbicationSpinner.setAdapter(adapter);
        //set the listeners
        //mFilterUbicationSpinner.setOnItemSelectedListener(this);

        //define endpoint
        //---------------------------------------------------------------------------------------
        logoInit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                urls.setTouched(urls.getTouched()+1);

                Log.d(TAG,urls.getTouched().toString());

                if(urls.getTouched().equals(urls.getNumTouch())){

                    logoInit.setImageResource(R.drawable.icon);
                }

            }
        });
        //---------------------------------------------------------------------------------------

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        versionTextView.setText(getVersion());
    }

    public String getVersion(){

        String version = "";

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);


        //mFilterUbicationSpinner

        user = mEmailView.getText().toString();
        String pass = mPasswordView.getText().toString();

        //boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.  filterUbication
        if (TextUtils.isEmpty(pass)) {

            mPasswordView.setError(getString(R.string.error_invalid_password));

            focusView = mPasswordView;
            focusView.requestFocus();

        }else if(TextUtils.isEmpty(user)){

            mEmailView.setError(getString(R.string.error_invalid_email));

            focusView = mEmailView;
            focusView.requestFocus();

        } else {

            password = Md5Manager.encode(pass);

            login(user, password, region);
        }

        /**
         * if(TextUtils.isEmpty(filterUbication)){
         *
         *  mPasswordView.setError("Debe seleccionar una ubicación.");
         *
         *} else
         * */

    }

    //validar que no deba iniciar sesion nuevamente si ya existen las credenciales
    @Override
    protected void onResume() {
        super.onResume();

        if(respuesta != null){
            Log.d(TAG, "Ya existe la respuesta, debe iniciar main activity");
            initMainActivity();
        }else{
            Log.d(TAG, "No existe la respuesta");
        }
    }

    private void login(String user, String password, String region){

        //Log.d(TAG,"Se pasaron los parámetros user: "+user+" y pass: "+password);

        mProgressView.setVisibility(View.VISIBLE);
        mLoginFormView.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(this);

        String urlApi = urls.getHostAuth();

        JSONObject req = new JSONObject();

        try {
            req.put("username",user);
            req.put("password",password);
            req.put("region",region);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, urlApi, req, new Response.Listener<JSONObject>() {

                    //cuOfJs

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(final JSONObject response) {

                        //executeResponseJsonApp(response);

                        JSONObject res = response;

                        Log.d(TAG,"Status de la respuesta: "+res.toString());

                        respuesta = new Respuesta();
                        resultado = new Resultado();
                        empleado = new Employee();

                        try {

                            JSONObject result = (JSONObject) res.get("result");
                            JSONObject employee = (JSONObject) result.get("employee");

                            respuesta.setSucces(res.getBoolean("success"));
                            respuesta.setMessage(res.getString("message"));

                            //---------------------------------------------------

                            empleado.setFirstName(employee.getString("firstName"));
                            empleado.setLastName(employee.getString("lastName"));
                            empleado.setPosition(employee.getString("position"));
                            empleado.setRegion(employee.getString("region"));
                            //empleado.setFilterUbication(filterUbication);
                            //---------------------------------------------------

                            resultado.setToken(result.getString("token"));
                            resultado.setEmployee(empleado);

                            respuesta.setResult(resultado);

                            Log.d(TAG, "El mensaje de la respuesta es: "+respuesta.getMessage());

                            Log.d(TAG,"token: "+resultado.getToken());
                            //----------------------------------------------------
                            /**
                             *  Intent hourlyActivityIntent = new Intent(MainActivity.this,HourlyForecastActivity.class);
                             *
                             *  //en este intent se añade un extra llamado socialNumber con el valor de tipo int
                             *  hourlyActivityIntent.putExtra("SocialNumber",1024524163);
                             *
                             *  hourlyActivityIntent.putParcelableArrayListExtra("hours",arrListHours);
                             *
                             *  startActivity(hourlyActivityIntent);
                             * */

                            initMainActivity();
                            //----------------------------------------------------

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        Log.d(TAG, error.toString());

                        mProgressView.setVisibility(View.GONE);
                        mLoginFormView.setVisibility(View.VISIBLE);

                        //despliegue del error en un toast
                        Toast.makeText(LoginActivity.this,getString(R.string.error_response)+": "+error.toString(),Toast.LENGTH_LONG).show();

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    private void initMainActivity(){

        Intent mainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);

        //Bundle bundle = new Bundle();
        //bundle.putParcelable("respuesta", respuesta);

        mainActivityIntent.putExtra("respuesta", respuesta);

        //Log.d(TAG, "Token desde respuesta antes de init MainActivity: --> "+respuesta.getResult().getToken());

        //mainActivityIntent.putExtra("respuesta", respuesta);

        mainActivityIntent.putExtra("resultado", resultado);

        mainActivityIntent.putExtra("empleado", empleado);

        mainActivityIntent.putExtra("urls", urls);

        startActivity(mainActivityIntent);
    }

    @Override
    public void onBackPressed() {

        if (respuesta == null) {
            Log.d(TAG, "Se debe generar la respuesta nuevamente.");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //Log.d(TAG, "Se ha seleccionado: "+parent.getItemAtPosition(position).toString());
        //filterUbication = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        Log.d(TAG, "Se ha seleccionado nada: "+parent.toString());
    }
}

