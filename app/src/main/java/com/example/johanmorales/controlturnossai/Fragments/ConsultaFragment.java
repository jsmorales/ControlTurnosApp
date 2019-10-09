package com.example.johanmorales.controlturnossai.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.johanmorales.controlturnossai.Events.UbicationSelectedEvent;
import com.example.johanmorales.controlturnossai.MainActivity;
import com.example.johanmorales.controlturnossai.Models.CheckArrivingResponse;
import com.example.johanmorales.controlturnossai.Models.Employee;
import com.example.johanmorales.controlturnossai.Models.NotificationResultCheckArriving;
import com.example.johanmorales.controlturnossai.Models.ResultCheckArriving;
import com.example.johanmorales.controlturnossai.Models.Resultado;
import com.example.johanmorales.controlturnossai.Models.Ubication;
import com.example.johanmorales.controlturnossai.Models.UtilsMainApp;
import com.example.johanmorales.controlturnossai.Network.RetrofitInstance;
import com.example.johanmorales.controlturnossai.NetworkCalls.PostArrivingCheck;
import com.example.johanmorales.controlturnossai.R;
import com.example.johanmorales.controlturnossai.utils.FormatDateUtil;
import com.example.johanmorales.controlturnossai.utils.KeyboardUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

import static android.support.v4.content.ContextCompat.getSystemService;


public class ConsultaFragment extends Fragment {

    private static final String TAG = ConsultaFragment.class.getSimpleName();
    private static final int MY_SOCKET_TIMEOUT_MS = 20000;
    private static final String NOTIFICACIONES_TITLE = "Notificaciones";

    public EditText socialNumberTextInput;
    public TextInputLayout textInputSocialLayout;
    public View consulta_progress;
    public View consultaForm;
    public View resultadoLayout;

    public TextView nameVerificationTextView, socialNumberVerificationTextView;
    public TextView validationTextView;
    public TextView detailTextView;
    public TextView turnTextViewpublic;
    public TextView notificationsTextView;

    public Resultado resultado;
    public Employee empleado;
    public UtilsMainApp urls;

    public Button buttonSelectUbication;
    public String filterUbicationText;
    public ArrayList<Ubication> arrayUbications;

    public FragmentTransaction fragmentTransaction;
    public FragmentManager fragmentManager;

    public AssignUbicationFragment assignUbicationFragment;
    public Ubication filterUbicationObject;
    public TextView valueUbicationTextView;
    public static final String ASSIGN_UBICATIONS_TAG = "AssignUbicationsFragment";

    public ConsultaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para obtener los arguments
        Bundle bundle = getArguments();

        resultado = bundle.getParcelable("resultado");
        empleado = bundle.getParcelable("empleado");
        urls = bundle.getParcelable("urls");

        Log.d(TAG,resultado.getToken());
        Log.d(TAG, "Se debe ejecutar: "+urls.getHost());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_consulta, container, false);

        //------------------------------------------------------
        //bind main fragment components UI
        socialNumberTextInput = view.findViewById(R.id.socialNumberTextInput);
        textInputSocialLayout = view.findViewById(R.id.textInputSocialLayout);
        consulta_progress = view.findViewById(R.id.consulta_progress);
        consultaForm = view.findViewById(R.id.consultaForm);
        resultadoLayout = view.findViewById(R.id.resultadoLayout);
        //------------------------------------------------------
        //bind result layout
        nameVerificationTextView = view.findViewById(R.id.nameVerificationTextView);
        socialNumberVerificationTextView = view.findViewById(R.id.socialNumberVerificationTextView);
        validationTextView = view.findViewById(R.id.validationTextView);
        detailTextView = view.findViewById(R.id.detailTextView);
        turnTextViewpublic = view.findViewById(R.id.turnTextView);
        notificationsTextView = view.findViewById(R.id.notificationsTextView);

        buttonSelectUbication = view.findViewById(R.id.buttonSelectUbication);
        valueUbicationTextView = view.findViewById(R.id.valueUbicationTextView);

        buttonSelectUbication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUbications();
            }
        });


        Button consultaButton = view.findViewById(R.id.consultaButton);

        consultaButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                getSocialNumberAndExec();

            }
        });

        /*lectura honeywell*/
        socialNumberTextInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                Log.d(TAG, "Event: "+event.getAction());
                Log.d(TAG, "keycode: "+keyCode);

                if(event.getAction() == 1 && keyCode == 0){

                    getSocialNumberAndExec();
                }

                return false;
            }
        });

        socialNumberTextInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.d(TAG, "onFocusChange: " + hasFocus);
                //KeyboardUtils.hideKeyboard(Objects.requireNonNull(getActivity()));
            }
        });

        //filterUbication for default

        if (filterUbicationObject == null) {

            Log.d(TAG, "filterUbicationObject is null.");

            filterUbicationObject = new Ubication();
            filterUbicationObject.setAirport("BOG");
            filterUbicationObject.setId("1");
            filterUbicationObject.setPosition("CRUJIA SUR");

            valueUbicationTextView.setText(String.format("%s", filterUbicationObject.getPosition()));

        }

        return view;
    }

    public void getSocialNumberAndExec(){

        String socialNumber = socialNumberTextInput.getText().toString();

        Log.d(TAG, "Social number: "+socialNumber);

        // Check for a valid password, if the user entered one.
        if (validateFormRequest()) {

            checkArriving(socialNumber); //TODO: change for the new method to post the new data.
            socialNumberTextInput.requestFocus();

        }
    }

    public boolean validateFormRequest(){

        // Reset errors.
        socialNumberTextInput.setError(null);

        String socialNumber = socialNumberTextInput.getText().toString();
        Ubication ubication = filterUbicationObject;

        if (TextUtils.isEmpty(socialNumber)) {

            socialNumberTextInput.setError(getString(R.string.error_field_required));
            socialNumberTextInput.requestFocus();

            return false;

        } else if (ubication == null) {

            socialNumberTextInput.setError("Debe seleccionar una ubicación");
            socialNumberTextInput.setText("");

            return false;

        } else {

            return true;

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
    public void onMessageEvent(UbicationSelectedEvent ubicationSelectedEvent) {
        filterUbicationObject = ubicationSelectedEvent.getUbication();
        Toast.makeText(getContext(), "Ubicación " + filterUbicationObject.getPosition() + " seleccionado.", Toast.LENGTH_SHORT).show();
        valueUbicationTextView.setText(String.format("%s", filterUbicationObject.getPosition()));
    }

    public void checkArriving(String socialNumber) {

        consulta_progress.setVisibility(View.VISIBLE);
        consultaForm.setVisibility(View.GONE);
        resultadoLayout.setVisibility(View.GONE);

        RetrofitInstance retroInstance = new RetrofitInstance(urls.getHost());

        PostArrivingCheck postArrivingCheck = retroInstance.getRetrofitInstance().create(PostArrivingCheck.class);

        Call<CheckArrivingResponse> call = postArrivingCheck.postArrivingCheck(resultado.getToken(), socialNumber, filterUbicationObject.getPosition());

        call.enqueue(new Callback<CheckArrivingResponse>() {
            @Override
            public void onResponse(Call<CheckArrivingResponse> call, retrofit2.Response<CheckArrivingResponse> response) {

                consulta_progress.setVisibility(View.GONE);
                consultaForm.setVisibility(View.VISIBLE);
                resultadoLayout.setVisibility(View.VISIBLE);

                socialNumberTextInput.setText("");
                //socialNumberTextInput.requestFocus();

                Log.d(TAG, "onResponse success: " + response.isSuccessful());
                
                if( response.isSuccessful() ) {

                    Log.d(TAG, "onResponse: " + response.body().getMessage());
                    Log.d(TAG, "onResponse: " + response.body().isSuccess());
                    Log.d(TAG, "onResponse: " + response.body().getResult());

                    //{"result":{"socialNumber":"79736546","name":"JHON JAIRO ARDILA OCHOA","agentPosition":"OPERARIO EQUIPOS","validation":false,"detail":"Por fuera del rango de tiempo","filterUbication":"CRUJIA SUR","turn":"17:00","ubication":"","notifications":[{"message":"mensaje de prueba 1"},{"message":"mensaje de test 2"}]}}

                    ResultCheckArriving result = response.body().getResult();

                    if (response.body().isSuccess()) {

                        Log.d(TAG, "onResponseResult: " + result.getName());

                        //TODO: execute the method to validate content

                        validateContent(result, "", true);

                    } else {

                        validateContent(result, "No se pudo validar el turno del agente.", false);

                    }

                } else {
                    resultadoLayout.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse: " + response.raw());
                    Toast.makeText(getContext(), "No se pudo verificar el número de cédula.", Toast.LENGTH_SHORT).show();
                    //validateContent(result, "No se pudo validar el turno del agente.", false);
                }

            }

            @Override
            public void onFailure(Call<CheckArrivingResponse> call, Throwable t) {

                Log.d(TAG, t.getMessage());

                consulta_progress.setVisibility(View.GONE);
                consultaForm.setVisibility(View.VISIBLE);
                resultadoLayout.setVisibility(View.GONE);
                socialNumberTextInput.setText("");
                socialNumberTextInput.clearFocus();

                //despliegue del error en un toast
                Toast.makeText(getContext(),"Hubo un error del sistema. Por favor reporte este error al administrador.",Toast.LENGTH_LONG).show();

            }
        });
    }

    public void validateContent(ResultCheckArriving result, String message, boolean type) {

        if(type) {

            socialNumberVerificationTextView.setText(result.getSocialNumber());
            nameVerificationTextView.setText(result.getName());
            validationTextView.setText(result.isValidation() ? "Puede Ingresar" : "No puede Ingresar");
            if (result.isValidation()) {
                resultadoLayout.setBackgroundResource(R.color.recentLogSuccess);
            } else {
                resultadoLayout.setBackgroundResource(R.color.recentLogFail);
            }
            detailTextView.setText(result.getDetail());
            turnTextViewpublic.setText(result.getTurn());
            setStylesUI(result.isValidation());

            ArrayList<NotificationResultCheckArriving> not = null;
            not = result.getNotifications();

            notificationsTextView.setText("");

            if(not != null) {

                if (not.size() > 0) {
                    notificationsTextView.append("Notificaciones:\n");
                    for (int i = 0; i < not.size(); i++) {
                        //JSONObject notification = (JSONObject) not.get(i);
                        notificationsTextView.append((i + 1) + ". " + not.get(i).getMessage() + "\n");
                    }
                } else {
                    notificationsTextView.setText("-");
                }

            }

        } else {

            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            resultadoLayout.setBackgroundResource(R.color.recentLogFail);
            nameVerificationTextView.setText("-");
            socialNumberVerificationTextView.setText("-");
            validationTextView.setText("-");
            detailTextView.setText("-");
            turnTextViewpublic.setText("-");
            notificationsTextView.setText("-");
            setStylesUI(false);
        }
    }

    public void showAlertDialogResult(JSONObject result){

        try {

            String notifications;

            //notificactions
            JSONArray not = result.getJSONArray("notifications");

            Log.d("not", not.toString());
            Log.d("cant:",String.valueOf(not.length()));

            if(not.length() > 0) {

                notifications = getNotificationsMessage(not);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                alertDialogBuilder.setMessage(notifications);
                alertDialogBuilder.setTitle(NOTIFICACIONES_TITLE);

                alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlertDialog", "Pulsado el boton neutral!");
                    }
                });

                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getNotificationsMessage(JSONArray notificationsArray){

        StringBuilder stringBuilder = new StringBuilder();
        String notifications = "";

        try {

            if(notificationsArray.length() > 0){

                for(int i = 0; i < notificationsArray.length(); i++){

                    JSONObject notification;

                    notification = (JSONObject) notificationsArray.get(i);

                    stringBuilder.append(i + 1).append(". ").append(notification.getString("message")).append("\n");
                }

                notifications = stringBuilder.toString();

                Log.d("notifications: ",notifications);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notifications;
    }

    public void setStylesUI(Boolean ableToEnter){

        int colorSuccess = ContextCompat.getColor(getContext(), R.color.recentLogFontSuccess);
        int colorFail = ContextCompat.getColor(getContext(), R.color.recentLogFontFail);

        nameVerificationTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        socialNumberVerificationTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        validationTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        detailTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        turnTextViewpublic.setTextColor(ableToEnter ? colorSuccess : colorFail);
        notificationsTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
    }

    public void getUbications(){

        consulta_progress.setVisibility(View.VISIBLE);
        buttonSelectUbication.setText("Cargando Ubicaciones...");

        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        // urls.getHost() + "/api/filterUbications?control=rampa"
        String urlApi = urls.getHost().concat("api/filterUbications?token=" + resultado.getToken()); //?type=rampa

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

                        consulta_progress.setVisibility(View.GONE);

                        JSONObject res = response;

                        arrayUbications = new ArrayList<>();

                        Log.d(TAG, "Status de la respuesta: " + res.toString());

                        try {

                            if(res.getBoolean("success")) {

                                JSONArray resArray = res.getJSONArray("result");

                                arrayUbications.clear();

                                for(Integer i = 0; i < resArray.length(); i++){

                                    JSONObject logJson;

                                    Ubication ubication = new Ubication();

                                    logJson = (JSONObject) resArray.get(i);

                                    ubication.setId(logJson.getString("id"));
                                    ubication.setPosition(logJson.getString("position"));
                                    ubication.setAirport(logJson.getString("airport"));

                                    Log.d(TAG, ubication.getPosition());

                                    arrayUbications.add(ubication);
                                }
                                buttonSelectUbication.setText("Seleccionar Ubicación");
                                initUbicationsFragment();

                            }else{
                                Toast.makeText(getContext(), res.getString("message"), Toast.LENGTH_SHORT).show();
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
                        consulta_progress.setVisibility(View.GONE);
                        buttonSelectUbication.setText("Seleccionar Ubicación");
                        //despliegue del error en un toast
                        Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    public void initUbicationsFragment() {

        Bundle bundle = new Bundle();

        bundle.putParcelable("resultado",resultado);
        bundle.putParcelable("urls", urls);
        // bundle.putParcelable("service", service);
        bundle.putParcelableArrayList("arrayUbications", arrayUbications);

        fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager(); //((AppCompatActivity) context).getSupportFragmentManager();
        // ChangeStatusServiceFragment savedServicesFragment = (ServicesFragment) fragmentManager.findFragmentByTag(SERVICES_FRAGMENT_TAG);
        fragmentTransaction = fragmentManager.beginTransaction();

        assignUbicationFragment = new AssignUbicationFragment();
        assignUbicationFragment.setArguments(bundle);

        fragmentTransaction.add(R.id.fragmentConsultaContainer, assignUbicationFragment, ASSIGN_UBICATIONS_TAG);

        //para poder regresar
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }


}
