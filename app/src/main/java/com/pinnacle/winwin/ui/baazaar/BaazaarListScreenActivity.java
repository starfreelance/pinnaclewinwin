package com.pinnacle.winwin.ui.baazaar;

import android.content.Intent;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ImageView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.ui.baazaar.adapter.BaazaarAdapter;
import com.pinnacle.winwin.ui.baazaar.model.BaazaarModel;
import com.pinnacle.winwin.ui.home.HomeScreenActivity;
import com.pinnacle.winwin.utils.Utils;

import java.util.ArrayList;

public class BaazaarListScreenActivity extends AppCompatActivity/* implements BaazaarListener*/ {

    private AppCompatTextView textViewTitle;

    private ImageView imgViewLeft;
    private ImageView imgViewRight;

    private RecyclerView recyclerViewBaazaars;
    private BaazaarAdapter mAdapter;

    private ArrayList<BaazaarModel> baazaarList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baazaar_list_screen);

        initViews();
        baazaarList = getBaazaarList();
        if (baazaarList != null && !baazaarList.isEmpty()) {
            /*loadAdapter();*/
        }
    }

    private void initViews() {

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setTypeface(Utils.getTypeFaceBodoni72OS(this));
        textViewTitle.setText(getResources().getString(R.string.title_home));
        textViewTitle.getPaint().setShader(new LinearGradient(0, 0, 0, 0, new int[]{getResources().getColor(R.color.colorStartGold),
                getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}, Shader.TileMode.REPEAT));

        recyclerViewBaazaars = findViewById(R.id.recyclerViewBaazaars);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewBaazaars.setLayoutManager(layoutManager);

    }

    private ArrayList<BaazaarModel> getBaazaarList() {

        ArrayList<BaazaarModel> baazaarList = new ArrayList<>();

        String[] baazaarNameArray = getResources().getStringArray(R.array.baazaar_name_array);
        String[] baazaarTimeArray = getResources().getStringArray(R.array.baazaar_time_array);
        String[] baazaarPastResultArray = getResources().getStringArray(R.array.baazaar_past_result_array);
        int[] baazaarTypeArray = {AppConstant.BAAZAAR_TYPE_KALYAN_OPEN, AppConstant.BAAZAAR_TYPE_KALYAN_CLOSE, AppConstant.BAAZAAR_TYPE_MAIN_OPEN,
                AppConstant.BAAZAAR_TYPE_MAIN_CLOSE, AppConstant.BAAZAAR_TYPE_KAATA_CHAAPA};
        int[] imgResourceArray = {R.drawable.ic_dice, R.drawable.ic_poker_chip, R.drawable.ic_cards, R.drawable.ic_chip, R.drawable.ic_chip};

        for (int i = 0; i < baazaarNameArray.length; i++) {
            BaazaarModel baazaarModel = new BaazaarModel();
            baazaarModel.setBaazaarName(baazaarNameArray[i]);
            baazaarModel.setBaazaarTime(baazaarTimeArray[i]);
            baazaarModel.setBaazaarPastResult(baazaarPastResultArray[i]);
            baazaarModel.setBaazaarType(baazaarTypeArray[i]);
            baazaarModel.setImgResource(imgResourceArray[i]);

            baazaarList.add(baazaarModel);
        }

        return baazaarList;
    }

    /*private void loadAdapter() {
        if (mAdapter == null) {
            mAdapter = new BaazaarAdapter(this, baazaarList);
            recyclerViewBaazaars.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }*/

    private void navigateToHomeScreen() {
        Intent intent = new Intent(this, HomeScreenActivity.class);
        startActivity(intent);
    }

    //Event Handlers
    //BaazaarListener
    /*@Override
    public void onBaazaarSelectListener(BaazaarModel baazaarModel) {
        if (baazaarModel != null) {
            switch (baazaarModel.getBaazaarType()) {
                case AppConstant.BAAZAAR_TYPE_KALYAN_OPEN:
                case AppConstant.BAAZAAR_TYPE_KALYAN_CLOSE:
                case AppConstant.BAAZAAR_TYPE_MAIN_OPEN:
                case AppConstant.BAAZAAR_TYPE_MAIN_CLOSE:
                    navigateToHomeScreen();
                    break;
            }
        }

    }*/
}
