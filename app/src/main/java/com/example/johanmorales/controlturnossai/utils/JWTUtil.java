package com.example.johanmorales.controlturnossai.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JWTUtil {

    public static String getDecodedJwt(String jwt) {
        String result = "";

        Log.d("JWTUtil", android.os.Build.VERSION.SDK_INT + " <--> " + android.os.Build.VERSION_CODES.O);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            String[] parts = jwt.split("[.]");
            try {
                int index = 0;
                for (String part : parts) {
                    if (index >= 2)
                        break;

                    index++;
                    byte[] partAsBytes = part.getBytes("UTF-8");
                    String decodedPart = null;

                    decodedPart = new String(java.util.Base64.getUrlDecoder().decode(partAsBytes), "UTF-8");


                    result += decodedPart;
                }
            } catch (Exception e) {
                throw new RuntimeException("Couldnt decode jwt", e);
            }

        } else {

            String[] parts = jwt.split("[.]");
            try {
                int index = 0;
                for (String part : parts) {
                    if (index >= 2)
                        break;

                    index++;
                    byte[] partAsBytes = part.getBytes("UTF-8");
                    String decodedPart = null;

                    decodedPart = new String(android.util.Base64.decode(partAsBytes, android.util.Base64.DEFAULT));

                    result += decodedPart;
                }
            } catch (Exception e) {
                throw new RuntimeException("Couldnt decode jwt", e);
            }

        }

        return result.substring(27);
    }

    public static boolean havePermissionRequested(JSONArray permissions, String permissionRequested) {

        boolean found = false;
        for (int i = 0; i < permissions.length(); i++) {

            try {

                JSONObject permissionObjt = permissions.getJSONObject(i);
                String permission = permissionObjt.getString("application");
                String permissionType = permissionObjt.getString("permissionType");

                if ( (permission.equals(permissionRequested)) && (permissionType.equals("Activo")) )
                    found = true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return found;
    }
}
