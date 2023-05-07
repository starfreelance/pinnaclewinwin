package com.pinnacle.winwin.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlineApplication;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.custom.CustomTypefaceSpan;
import com.pinnacle.winwin.network.model.ASDataRequest;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static Typeface getTypeFaceBodoni72(Context context) {
//        return Typeface.createFromAsset(context.getAssets(), "fonts/Bodoni Seventytwo ITC Bold OS.ttf");
        return Typeface.createFromAsset(context.getAssets(), "fonts/baskerville_bold_bt.ttf");
        /*return Typeface.createFromAsset(context.getAssets(), "fonts/Bodoni_72.ttc");*/
    }

    public static Typeface getTypeFaceBodoni72OS(Context context) {
        /*return Typeface.createFromAsset(context.getAssets(), "fonts/Bodoni Seventytwo ITC Bold OS.ttf");*/
        return Typeface.createFromAsset(context.getAssets(), "fonts/Bodoni_72_OS.ttc");
    }

    public static Typeface getTypeFaceBodoni72Book(Context context) {
        /*return Typeface.createFromAsset(context.getAssets(), "fonts/Bodoni Seventytwo ITC Bold OS.ttf");*/
        return Typeface.createFromAsset(context.getAssets(), "fonts/Bodoni_72_Book.ttf");
    }

    public static Typeface getTypeFaceGeorgiaBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/georgia_bold.ttf");
    }

    /*This method is used to set gradient color to your text*/
    public static LinearGradient getTextGradient(int[] colors, float[] positions) {
        return new LinearGradient(0, 0, 0, 0, colors, positions, Shader.TileMode.REPEAT);
    }

    public static Snackbar showCustomSnackBarMessageView(Context context, View view, String message, int length, String actionTitle) {
        final Snackbar snackbar = Snackbar.make(view, message, length);
        TextView textView = (TextView) snackbar.getView().findViewById(R.id.snackbar_text);
        textView.setTypeface(getTypeFaceBodoni72(context));
        snackbar.setAction(actionTitle, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.setActionTextColor(context.getResources().getColor(R.color.colorPrimary));
        return snackbar;
    }

    public static Spannable getCustomSpannableStringWithGradient(Context context, String text, int fontColor, Typeface typeface) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new CustomTypefaceSpan("", typeface, true), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static Spannable getCustomSpannableString(Context context, String text, int fontColor, Typeface typeface) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new ForegroundColorSpan(fontColor), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("", typeface, false), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    public static Spannable getCustomSpannableString(Context context, String text, int fontColor,
                                                     Typeface typeface, float size, boolean isBold) {
        Spannable spannable = new SpannableString(text);
        spannable.setSpan(new ForegroundColorSpan(fontColor), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("", typeface, false), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new RelativeSizeSpan(size), 0, text.length(),  0);
        return spannable;
    }

    /*public static Toast showCustomToastMessageView(Context context, String message, int length) {
        Toast toast = Toast.makeText(context, message, length);
        View view = toast.getView();
        *//*view.setBackgroundResource(R.drawable.bg_black_gradient);*//*
        view.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
        TextView textView = view.findViewById(android.R.id.message);
        textView.setTextColor(context.getResources().getColor(android.R.color.white));
        textView.setTypeface(getTypeFaceBodoni72(context));
        return toast;
    }*/

    public static boolean isPasswordValid(String password) {
        /*Pattern pattern = Pattern.compile("/^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$/");*/
        /*Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*])(?=.{8,})");*/
        /*Pattern pattern = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})");*/
        Pattern pattern = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,40})");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = ASOnlineApplication.getInstance().getApplicationContext().
                getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor == null) {
            return null;
        } else {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
            return res;
        }
    }

    public static byte[] getByteArrayFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream .toByteArray();
    }

    public static String convertBitmapToBase64(Bitmap bitmap) {
        String encodedString = null;
        if (bitmap != null) {
            encodedString = Base64.encodeToString(Utils.getByteArrayFromBitmap(bitmap), Base64.DEFAULT);
        }
        return encodedString;
    }

    public static String getHeaderToken(Context context) {
        String tokenType = "Bearer";
        return String.format("%s %s", tokenType,
                ASOnlinePreferenceManager.getString(context, AppConstant.KEY_USER_TOKEN, ""));
    }

    public static ASDataRequest getAsDataRequest(String requestString) {
        ASDataRequest asDataRequest = new ASDataRequest();
        asDataRequest.setAsData(requestString);
        return asDataRequest;
    }

    public static int getVersionCode(Context context) {
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getOTCBaazaarId(int baazaarId) {
        switch (baazaarId) {
            case AppConstant.BAAZAAR_TYPE_KALYAN_OPEN:
                return AppConstant.BAAZAAR_TYPE_KALYAN_CLOSE;
            case AppConstant.BAAZAAR_TYPE_KALYAN_CLOSE:
                return AppConstant.BAAZAAR_TYPE_KALYAN_OPEN;
            case AppConstant.BAAZAAR_TYPE_MAIN_OPEN:
                return AppConstant.BAAZAAR_TYPE_MAIN_CLOSE;
            case AppConstant.BAAZAAR_TYPE_MAIN_CLOSE:
                return AppConstant.BAAZAAR_TYPE_MAIN_OPEN;
            case AppConstant.BAAZAAR_TYPE_TIME_OPEN:
                return AppConstant.BAAZAAR_TYPE_TIME_CLOSE;
            case AppConstant.BAAZAAR_TYPE_MILAN_DAY_OPEN:
                return AppConstant.BAAZAAR_TYPE_MILAN_DAY_CLOSE;
            case AppConstant.BAAZAAR_TYPE_MILAN_NIGHT_OPEN:
                return AppConstant.BAAZAAR_TYPE_MILAN_NIGHT_CLOSE;
                default:
                    return -1;
        }
    }

    public static int getValueInDP(Context context, int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
