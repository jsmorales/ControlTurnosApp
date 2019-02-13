package com.example.johanmorales.controlturnossai.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.example.johanmorales.controlturnossai.MainActivity;
import com.example.johanmorales.controlturnossai.Models.Employee;
import com.example.johanmorales.controlturnossai.Models.Resultado;
import com.example.johanmorales.controlturnossai.Models.UtilsMainApp;
import com.example.johanmorales.controlturnossai.R;
import com.example.johanmorales.controlturnossai.utils.FormatDateUtil;
import com.example.johanmorales.controlturnossai.utils.KeyboardUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    public TextView nameVerificationTextView;
    public TextView validationTextView;
    public TextView detailTextView;
    public TextView turnTextViewpublic;
    public TextView notificationsTextView;

    public Resultado resultado;
    public Employee empleado;
    public UtilsMainApp urls;

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
        validationTextView = view.findViewById(R.id.validationTextView);
        detailTextView = view.findViewById(R.id.detailTextView);
        turnTextViewpublic = view.findViewById(R.id.turnTextView);
        notificationsTextView = view.findViewById(R.id.notificationsTextView);


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

        return view;
    }

    public void getSocialNumberAndExec(){

        String socialNumber = socialNumberTextInput.getText().toString();

        Log.d(TAG, "Social number: "+socialNumber);

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(socialNumber)) {

            socialNumberTextInput.setError(getString(R.string.error_field_required));
            socialNumberTextInput.requestFocus();

        } else {

            consultar(socialNumber);
            socialNumberTextInput.requestFocus();
        }
    }

    public void consultar(String socialNumber){

        consulta_progress.setVisibility(View.VISIBLE);
        consultaForm.setVisibility(View.GONE);
        resultadoLayout.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        String urlApi = urls.getHost().concat("api/checkArriving");

        JSONObject req = new JSONObject();

        try {
            req.put("socialNumber",socialNumber);
            req.put("filterUbication",empleado.getFilterUbication());
            req.put("token",resultado.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, urlApi, req, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(final JSONObject response) {

                        //executeResponseJsonApp(response);

                        //hide the keyboard------------------------------------------------
                        //KeyboardUtils.hideKeyboard(getActivity());
                        //-----------------------------------------------------------------


                        consulta_progress.setVisibility(View.GONE);
                        consultaForm.setVisibility(View.VISIBLE);
                        resultadoLayout.setVisibility(View.VISIBLE);

                        socialNumberTextInput.setText("");
                        socialNumberTextInput.clearFocus();

                        JSONObject res = response;

                        Log.d(TAG, "Status de la respuesta: " + res.toString());

                        try {

                            if(res.getBoolean("success")) {

                                JSONObject result = response.getJSONObject("result");

                                nameVerificationTextView.setText(result.getString("name"));

                                validationTextView.setText(result.getBoolean("validation") ? "Puede Ingresar" : "No puede Ingresar");

                                if(result.getBoolean("validation")){
                                    resultadoLayout.setBackgroundResource(R.color.recentLogSuccess);
                                }else{
                                    resultadoLayout.setBackgroundResource(R.color.recentLogFail);
                                }

                                //notificactions
                                JSONArray not = result.getJSONArray("notifications");

                                notificationsTextView.setText("");

                                if(not.length() > 0){

                                    notificationsTextView.append("Notificaciones:\n");

                                    for(int i = 0; i < not.length(); i++){

                                        JSONObject notification = (JSONObject) not.get(i);

                                        notificationsTextView.append((i+1)+". "+notification.getString("message")+"\n");
                                    }
                                }

                                detailTextView.setText(result.getString("detail"));


                                turnTextViewpublic.setText(result.getString("turn"));

                                setStylesUI(result.getBoolean("validation"));

                                //exect alertDialog
                                showAlertDialogResult(result);

                            }else{
                                Toast.makeText(getContext(), res.getString("message"), Toast.LENGTH_SHORT).show();

                                nameVerificationTextView.setText("-");

                                validationTextView.setText("-");

                                detailTextView.setText("-");

                                turnTextViewpublic.setText("-");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        Log.d(TAG, error.toString());

                        consulta_progress.setVisibility(View.GONE);

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
    }/**/

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
        validationTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        detailTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
        turnTextViewpublic.setTextColor(ableToEnter ? colorSuccess : colorFail);
        notificationsTextView.setTextColor(ableToEnter ? colorSuccess : colorFail);
    }

}
