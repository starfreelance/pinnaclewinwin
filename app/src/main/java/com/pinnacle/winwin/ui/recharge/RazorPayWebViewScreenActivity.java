package com.pinnacle.winwin.ui.recharge;

import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.pinnacle.winwin.BuildConfig;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.ui.rechargehistory.RechargeHistoryScreenActivity;
import com.pinnacle.winwin.utils.LogUtils;
import com.pinnacle.winwin.utils.Utils;

public class RazorPayWebViewScreenActivity extends ASOnlineBaseActivity implements
        View.OnClickListener {

    private static final String TAG = RazorPayWebViewScreenActivity.class.getSimpleName();

    private AppCompatTextView textViewTitle;
    private WebView webViewRazorPay;

    private View parentLayout;

    private ImageView imgViewLeft;

    private String billId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razor_pay_web_view_screen);

        processIntentData();
        initViews();
        setUpWebView();
        loadWebViewUrl();
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.setText(getResources().getString(R.string.title_recharge));
        textViewTitle.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        imgViewLeft = findViewById(R.id.imgViewLeft);
        imgViewLeft.setOnClickListener(this);
        imgViewLeft.setImageResource(R.drawable.ic_arrow_back);

        webViewRazorPay = findViewById(R.id.webViewRazorPay);
    }

    private void processIntentData() {
        if (getIntent() != null) {
            if (getIntent().hasExtra(AppConstant.KEY_BILL_ID) &&
                    getIntent().getStringExtra(AppConstant.KEY_BILL_ID) != null) {
                billId = getIntent().getStringExtra(AppConstant.KEY_BILL_ID);
            }
        }
    }

    private void setUpWebView() {
        WebSettings webSettings = webViewRazorPay.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
//        webSettings.setPluginState(WebSettings.PluginState.ON);
//        webSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webViewRazorPay.addJavascriptInterface(new PaymentInterface(), "PaymentInterface");
//        if (Build.VERSION.SDK_INT >= 21) {
//            CookieManager.getInstance().setAcceptThirdPartyCookies(webViewRazorPay, true);
//        } else {
//            CookieManager.getInstance().setAcceptCookie(true);
//        }
        webViewRazorPay.setWebChromeClient(razorPayChromeWebClient);
    }

    private void loadWebViewUrl() {
        webViewRazorPay.loadUrl(BuildConfig.BASE_URL_RAZOR_PAY + billId);
    }

    private void navigateToRechargeHistoryScreen() {
        Intent intent = new Intent(this, RechargeHistoryScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private WebViewClient razorPayWebClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            LogUtils.d(TAG, url);
            showProgressDialog(true, "");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LogUtils.d(TAG, "ONFINISH" + url);
            showProgressDialog(false, "");
            if (url.contains("/SUCCESS")) {
                navigateToRechargeHistoryScreen();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            Log.e("ERROR", error.toString());
        }
    };

    private WebChromeClient razorPayChromeWebClient = new WebChromeClient() {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {

            WebView newWebView = new WebView(RazorPayWebViewScreenActivity.this);
            newWebView.getSettings().setJavaScriptEnabled(true);
            newWebView.getSettings().setSupportZoom(true);
            newWebView.getSettings().setBuiltInZoomControls(true);
            newWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
            newWebView.getSettings().setSupportMultipleWindows(true);
            view.addView(newWebView);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();

            newWebView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    LogUtils.d(TAG, "ONFINISH" + url);
                    showProgressDialog(false, "");
                    if (url.contains("/SUCCESS")) {
                        navigateToRechargeHistoryScreen();
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });

            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.e("CONSOLE MESSAGE", consoleMessage.message());
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            Log.e("JS PROMPT", url + message);
            return true;
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.e("JS ALERT", url + message);
            return true;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            Log.e("CUSTOM VIEW", callback.toString());
            super.onShowCustomView(view, callback);
        }
    };

    @Override
    protected void onSuccess(Object response) {

    }

    @Override
    protected void onFailure(Object response) {

    }

    @Override
    protected void showInternetError() {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        /*if (id == R.id.imgViewLeft) {
            onBackPressed();
        }*/
    }

    class PaymentInterface{
        @JavascriptInterface
        public void success(String data){
            Log.i("RAZORPAY_LOG","success: "+data);
        }

        @JavascriptInterface
        public void error(String data){
            Log.i("RAZORPAY_LOG","success: "+data);
        }
    }
}