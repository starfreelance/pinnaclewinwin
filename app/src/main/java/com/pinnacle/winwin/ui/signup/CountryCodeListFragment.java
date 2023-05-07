package com.pinnacle.winwin.ui.signup;

import android.app.Activity;
import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.base.ASOnlineBaseFragment;
import com.pinnacle.winwin.network.model.CountryCodeData;
import com.pinnacle.winwin.network.model.CountryCodeResponse;
import com.pinnacle.winwin.network.model.GenericResponse;
import com.pinnacle.winwin.ui.signup.adapter.CountryCodeListAdapter;
import com.pinnacle.winwin.ui.signup.listener.CountryCodeAdapterListener;
import com.pinnacle.winwin.ui.signup.listener.CountryCodeListFragmentListener;
import com.pinnacle.winwin.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class CountryCodeListFragment extends ASOnlineBaseFragment implements CountryCodeAdapterListener, View.OnClickListener {

    private AppCompatTextView textViewTitle;

    private AppCompatEditText editTextSearch;

    private AppCompatImageView imgViewBack;

    private RecyclerView recyclerViewCountryCode;
    private CountryCodeListAdapter mAdapter;

    private Activity mActivity;
    private CountryCodeListFragmentListener countryCodeListFragmentListener;
    private String previousSearchString = "";
    private ArrayList<CountryCodeData> searchCountryList = new ArrayList<>();

    public static CountryCodeListFragment newInstance() {
        Bundle args = new Bundle();

        CountryCodeListFragment fragment = new CountryCodeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
            countryCodeListFragmentListener = (CountryCodeListFragmentListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_country_code_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (AppConstant.countryCodeList == null || AppConstant.countryCodeList.isEmpty()) {
            callGetCountryCodeListApi();
        } else {
            loadAdapter(AppConstant.countryCodeList);
        }
    }

    private void initViews(View view) {

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        textViewTitle.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        editTextSearch = view.findViewById(R.id.editTextSearch);
        editTextSearch.setTypeface(Utils.getTypeFaceBodoni72(mActivity));
        editTextSearch.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));
        editTextSearch.addTextChangedListener(searchTextWatcher);

        imgViewBack = view.findViewById(R.id.imgViewBack);
        imgViewBack.setOnClickListener(this);

        recyclerViewCountryCode = view.findViewById(R.id.recyclerViewCountryCode);
        recyclerViewCountryCode.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
    }

    private void loadAdapter(ArrayList<CountryCodeData> countryList) {
        if (mAdapter == null) {
            mAdapter = new CountryCodeListAdapter(mActivity, countryList);
            mAdapter.setCountryCodeAdapterListener(this);
            recyclerViewCountryCode.setAdapter(mAdapter);
        } else {
            mAdapter.updateData(countryList);
        }
    }

    private void callGetCountryCodeListApi() {
        callAppServer(AppConstant.REQ_API_TYPE_GET_COUNTRY_CODE_LIST, null, true);
    }

    private TextWatcher searchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            String searchString = charSequence.toString();
            if (!searchString.equalsIgnoreCase(previousSearchString)) {
                if (searchString.length() >= 1) {
                    searchCountryList.clear();
                    if (AppConstant.countryCodeList != null) {
                        for (CountryCodeData countryCodeData : AppConstant.countryCodeList) {
                            if (countryCodeData.getCountryName().toLowerCase().
                                    contains(searchString.toLowerCase())) {
                                searchCountryList.add(countryCodeData);
                            } else if (countryCodeData.getCountryCode().contains(searchString)) {
                                searchCountryList.add(countryCodeData);
                            }
                        }
                        previousSearchString = searchString;
                        loadAdapter(searchCountryList);
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onSuccess(Object response) {
        if (response instanceof CountryCodeResponse) {
            CountryCodeResponse countryCodeResponse = (CountryCodeResponse) response;
            if (countryCodeResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
                AppConstant.countryCodeList = (ArrayList<CountryCodeData>) countryCodeResponse.getCountryCodeDataList();
                if (AppConstant.countryCodeList != null && !AppConstant.countryCodeList.isEmpty()) {
                    loadAdapter(AppConstant.countryCodeList);
                }
            }
        }
    }

    @Override
    protected void onFailure(Object response) {
        if (response instanceof GenericResponse) {
            GenericResponse genericResponse = (GenericResponse) response;
            if (genericResponse.getError() != null && !genericResponse.getError().isEmpty()) {
                Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                        genericResponse.getError(),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            } else {
                Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                        getResources().getString(R.string.something_went_wrong_error),
                        Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
            }
        } else {
            Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                    getResources().getString(R.string.something_went_wrong_error),
                    Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
        }
    }

    @Override
    protected void showInternetError() {
        Utils.showCustomSnackBarMessageView(mActivity, mActivity.findViewById(R.id.parentLayout),
                getResources().getString(R.string.internet_unavailable_error),
                Snackbar.LENGTH_LONG, getResources().getString(R.string.btn_okay)).show();
    }

    @Override
    public void onItemSelected(CountryCodeData countryCodeData) {
        if (countryCodeListFragmentListener != null) {
            getParentFragmentManager().popBackStack();
            countryCodeListFragmentListener.onCountrySelected(countryCodeData);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.imgViewBack) {
            getParentFragmentManager().popBackStack();
        }
    }
}
