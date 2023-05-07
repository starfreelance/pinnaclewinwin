package com.pinnacle.winwin.ui.enquiry;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.ASOnlinePreferenceManager;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseActivity;
import com.pinnacle.winwin.network.model.ChatData;
import com.pinnacle.winwin.network.model.ChatDataResponse;
import com.pinnacle.winwin.network.model.ChatRequest;
import com.pinnacle.winwin.network.model.ChatResponse;
import com.pinnacle.winwin.network.model.ChatThreadRequest;
import com.pinnacle.winwin.network.model.ChatThreadResponse;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.ui.enquiry.adapter.EnquiryAdapter;
import com.pinnacle.winwin.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.LinkedList;

public class EnquiryScreenActivity extends ASOnlineBaseActivity implements View.OnClickListener {

    private AppCompatTextView textViewTitle;
    private AppCompatEditText editTextEnquiry;

    private RecyclerView recyclerViewEnquires;
    private LinearLayoutManager linearLayoutManager;
    private EnquiryAdapter enquiryAdapter;

    private Button btnSend;

    private ImageView imgViewLeft;

    private ProgressBar progressBarHeader;

    private View parentLayout;

    private ArrayList<ChatData> chats;
    private LinkedList<ChatData> chatList;
    private int currentPage = 1;
    private static int scrollPosY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_screen);

        initViews();
        callGetChatThreadApi(true);
    }

    @Override
    protected void onDestroy() {
        EnquiryAdapter.isEntireListLoaded = false;
        super.onDestroy();
    }

    private void initViews() {

        parentLayout = findViewById(R.id.parentLayout);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(this));
        textViewTitle.setText(getResources().getString(R.string.title_enquiry));
        textViewTitle.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        imgViewLeft = findViewById(R.id.imgViewLeft);
        imgViewLeft.setOnClickListener(this);
        imgViewLeft.setImageResource(R.drawable.ic_arrow_back);

        editTextEnquiry = findViewById(R.id.editTextEnquiry);
        editTextEnquiry.setTypeface(Utils.getTypeFaceBodoni72(this));
        editTextEnquiry.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));
        editTextEnquiry.addTextChangedListener(enquiryTextWatcher);

        recyclerViewEnquires = findViewById(R.id.recyclerViewEnquires);
        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerViewEnquires.setLayoutManager(linearLayoutManager);
        recyclerViewEnquires.addOnScrollListener(scrollListener);

        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        progressBarHeader = findViewById(R.id.progressBarHeader);
        // Change the default color of progress bar programmatically
        progressBarHeader.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_IN);

    }

    private void loadEnquiryAdapter() {
        if (enquiryAdapter == null) {
            enquiryAdapter = new EnquiryAdapter(this, chats);
            recyclerViewEnquires.setAdapter(enquiryAdapter);
        } else {
            enquiryAdapter.updateData(chats);
        }
    }

    private void toggleSendBtn(boolean isEnabled) {
        btnSend.setEnabled(isEnabled);
    }

    private void toggleProgressBarHeader(boolean isVisible) {
        if (isVisible) {
            progressBarHeader.setVisibility(View.VISIBLE);
        } else {
            progressBarHeader.setVisibility(View.GONE);
        }
    }

    private void clearEnquiryText() {
        editTextEnquiry.setText("");
    }

    private void updateChatData() {
        if (chats == null) {
            chats = new ArrayList<>();
        } else {
            chats.clear();
        }
        chats.addAll(chatList);
    }

    private void callGetChatThreadApi(boolean isProgressVisible) {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_GET_CHAT_THREAD_REQUEST, getChatThreadRequest());
        callAppServer(AppConstant.REQ_API_TYPE_GET_CHAT_THREAD, intent, isProgressVisible);
    }

    private ChatThreadRequest getChatThreadRequest() {
        ChatThreadRequest chatThreadRequest = new ChatThreadRequest();
        chatThreadRequest.setCurrentPage(currentPage);
        chatThreadRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        return chatThreadRequest;
    }

    private void callSendMessageApi() {
        Intent intent = new Intent();
        intent.putExtra(AppConstant.KEY_SEND_MESSAGE_REQUEST, getChatRequest());
        callAppServer(AppConstant.REQ_API_TYPE_SEND_MESSAGE, intent, true);
    }

    private ChatRequest getChatRequest() {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setCustId(ASOnlinePreferenceManager.getInteger(this, AppConstant.KEY_USER_CUST_ID, -1));
        chatRequest.setMessage(editTextEnquiry.getText().toString().trim());

        return chatRequest;
    }

    private TextWatcher enquiryTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            String enquiryString = charSequence.toString();
            if (enquiryString.length() > 0) {
                toggleSendBtn(true);
            } else {
                toggleSendBtn(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (scrollPosY < 0 && linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0 &&
                        !EnquiryAdapter.isEntireListLoaded) {
                    toggleProgressBarHeader(true);
                    currentPage++;
                    callGetChatThreadApi(false);
                } else {
                    toggleProgressBarHeader(false);
                }
            } else {
                toggleProgressBarHeader(false);
            }
        }

        @Override
        public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            scrollPosY = dy;
        }
    };

    /**
     * @param response
     */
    @Override
    protected void onSuccess(Object response) {
        if (response instanceof ChatThreadResponse) {
            ChatThreadResponse chatThreadResponse = (ChatThreadResponse) response;
            if (chatThreadResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                toggleProgressBarHeader(false);
                ChatDataResponse chatDataResponse = chatThreadResponse.getChatDataResponse();
                if (chatDataResponse != null) {
                    if (chatDataResponse.getChats() != null &&
                            !chatDataResponse.getChats().isEmpty()) {
//                        if (chats == null || chats.isEmpty()) {
//                            chats = (ArrayList<ChatData>) chatDataResponse.getChats();
//                            recyclerViewEnquires.smoothScrollToPosition(chats.size() - 1);
//                            loadEnquiryAdapter();
//                        } else {
//                            chats.addAll(0, chatDataResponse.getChats());
//                            //This is used to maintain the scroll position that tries to keep the user looking
//                            // at "the same thing" as before the operation.
//                            enquiryAdapter.notifyItemRangeInserted(0, chatDataResponse.getChats().size());
//                        }
                        if (chatList == null || chatList.isEmpty()) {
                            chatList = new LinkedList<>(chatDataResponse.getChats());
                            recyclerViewEnquires.smoothScrollToPosition(chatList.size() - 1);
                            updateChatData();
                            loadEnquiryAdapter();
                        } else {
                            chatList.addAll(0, chatDataResponse.getChats());
                            //This is used to maintain the scroll position that tries to keep the user looking
                            // at "the same thing" as before the operation.
                            updateChatData();
                            enquiryAdapter.notifyItemRangeInserted(0, chatDataResponse.getChats().size());
                        }
                        if (chatDataResponse.getTotalData() == chatList.size()) {
                            EnquiryAdapter.isEntireListLoaded = true;
                        }
                    }
                }
            }
        } else if (response instanceof ChatResponse) {
            ChatResponse chatResponse = (ChatResponse) response;
            if (chatResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                clearEnquiryText();
                if (chatResponse.getChatData() != null) {
//                    if (chats == null) {
//                        chats = new ArrayList<>();
//                    }
//                    if (chats.size() > 0) {
//                        if (chats.size() == 10) {
//                            chats.remove(0);
//                            chats.add(chatResponse.getChatData());
//                        } else {
//                            chats.add(chatResponse.getChatData());
//                        }
//                    } else {
//                        chats.add(chatResponse.getChatData());
//                    }
                    if (chatList == null) {
                        chatList = new LinkedList<>();
                    }
                    if (chatList.size() > 0) {
                        if (chatList.size() == 10) {
                            chatList.remove(0);
                            chatList.add(chatResponse.getChatData());
                        } else {
                            chatList.add(chatResponse.getChatData());
                        }
                    } else {
                        chatList.add(chatResponse.getChatData());
                    }
                    updateChatData();
                    loadEnquiryAdapter();
                    recyclerViewEnquires.smoothScrollToPosition(chatList.size() - 1);
                }
            }
        }
    }

    /**
     * @param response
     */
    @Override
    protected void onFailure(Object response) {
        if (response instanceof GenericResponse) {
            GenericResponse genericResponse = (GenericResponse) response;
            if (genericResponse.getError() != null && !genericResponse.getError().isEmpty()) {
                Utils.showCustomSnackBarMessageView(this, findViewById(R.id.parentLayout),
                        genericResponse.getError(),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            } else {
                Utils.showCustomSnackBarMessageView(this, findViewById(R.id.parentLayout),
                        getResources().getString(R.string.something_went_wrong_error),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            }
        } else {
            Utils.showCustomSnackBarMessageView(this, parentLayout,
                    getResources().getString(R.string.something_went_wrong_error),
                    Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
        }
    }

    /**
     *
     */
    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(this, parentLayout,
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSend) {
            callSendMessageApi();
        } else if (id == R.id.imgViewLeft) {
            onBackPressed();
        }
    }
}