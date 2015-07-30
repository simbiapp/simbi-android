package org.simbi.simbiapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import retrofit.RetrofitError;
import retrofit.mime.TypedInput;

public class Utils {

    public static boolean hasInternetConnectivity(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public static String handleRetrofitErrorQuietly(final RetrofitError error) {
        error.printStackTrace();

        InputStream inputStream = null;
        try {
            if (error.isNetworkError()) {
                Log.e(SimbiConstants.TAG, "Network error happened.");
            } else {
                final TypedInput body = error.getResponse().getBody();
                if (body == null) {
                    Log.e(SimbiConstants.TAG, "Unable to retrieve body");
                    return null;
                }
                inputStream = body.in();

                final String result = inputStream.toString();
                Log.e(SimbiConstants.TAG, result);
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(inputStream);
        }
        return null;
    }

    public static void closeStream(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
