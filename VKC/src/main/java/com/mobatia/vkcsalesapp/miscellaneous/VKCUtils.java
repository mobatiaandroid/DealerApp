package com.mobatia.vkcsalesapp.miscellaneous;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings.Secure;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mobatia.vkcsalesapp.R;
import com.mobatia.vkcsalesapp.customview.CustomToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VKCUtils {

    public static void setImageFromUrl(Activity activity, String Url,
                                       final ImageView imageView, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);

        Glide.with(activity)
                .load(Url).centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }


                })
                .into(imageView);

    }


    public static void setImageFromUrlGrid(Activity activity, String Url,
                                           final ImageView imageView, final ProgressBar progressBar) {
        Glide.with(activity)
                .load(Url).centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }


                })
                .into(imageView);


    }

    public static String formatDateWithInput(String date, String formatOutput,
                                             String formatInput) {
        String lastLoginTimeFormat = "";
        Calendar calenderDate = Calendar.getInstance();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatInput, Locale.US);
            sdf.setTimeZone(calenderDate.getTimeZone());
            calenderDate.setTime(sdf.parse(date));
            sdf.applyLocalizedPattern(formatOutput);
            lastLoginTimeFormat = sdf.format(calenderDate.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return lastLoginTimeFormat;

    }

    public static void setImageFromUrlBaseTransprant(Activity activity,
                                                     String Url, final ImageView imageView, final ProgressBar progressBar) {
        Glide.with(activity)
                .load(Url).fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);

                        return false;
                    }


                })
                .into(imageView);
    }

    /**
     * Gets the device id.
     *
     * @param context the context
     * @return the device id
     */
    public static String getDeviceID(Context context) {


        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return telephonyManager.getDeviceId();
            }
            return telephonyManager.getDeviceId();
        } else {
            return Secure.getString(context.getContentResolver(),
                    Secure.ANDROID_ID);
        }

    }

    /**
     * Show toast message
     *
     * @param activity the activity,message
     * @return void
     */

    public static void showtoast(Activity activity, int errorType) {

        CustomToast toast = new CustomToast(activity);
        toast.show(errorType);

    }


    /**
     * Check internet connection.
     *
     * @param context the context
     * @return true, if successful
     */
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connec.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Hexa to Color value
     *
     * @param colorString hexa value
     * @return int value, if successful
     */
    public static int parseColor(String colorString) {
        if (colorString.startsWith("#")) {
            return Color.parseColor(colorString);
        } else {
            return Color.parseColor("#FFFFFFFF");
        }
    }

    public static void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context

                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {

            imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
                    .getWindowToken(), 0);

        }
    }

    /**
     * Sets the error for edit text.
     *
     * @param edt the edt
     * @param msg the msg
     */
    public static void setErrorForEditText(EditText edt, String msg) {
        edt.setError(msg);
    }

    /**
     * Text watcher for edit text.
     *
     * @param edt the edt
     * @param msg the msg
     */
    public static void textWatcherForEditText(final EditText edt,
                                              final String msg) {
        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() == 0 || s.equals("")) {
                    VKCUtils.setErrorForEditText(edt, msg);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                if (s.equals("")) {
                    VKCUtils.setErrorForEditText(edt, msg);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0 && edt.getError() != null) {
                    edt.setError(null);
                } else if (s.length() == 0 || s.equals("")) {
                    VKCUtils.setErrorForEditText(edt, msg);
                }
            }
        });
    }

    /**
     * Checks if is valid email.
     *
     * @param email the email
     * @return the boolean
     */
    public static Boolean isValidEmail(String email) {
        Boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }


}
