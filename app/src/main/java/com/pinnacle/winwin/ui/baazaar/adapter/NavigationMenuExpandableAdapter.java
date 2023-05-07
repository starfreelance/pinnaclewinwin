package com.pinnacle.winwin.ui.baazaar.adapter;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import com.pinnacle.winwin.R;
import com.pinnacle.winwin.app.AppConstant;
import com.pinnacle.winwin.ui.baazaar.listener.NavigationMenuListener;
import com.pinnacle.winwin.ui.baazaar.model.NavigationMenuModel;
import com.pinnacle.winwin.utils.Utils;

import java.util.LinkedHashMap;
import java.util.List;

public class NavigationMenuExpandableAdapter extends BaseExpandableListAdapter {

    /*GROUP TYPE VIEW*/
    private static final int TYPE_HEADER_VIEW = 0;
    private static final int TYPE_ITEM_VIEW = 1;

    private Context mContext;
    private List<NavigationMenuModel> navigationMenuList;
    private LinkedHashMap<NavigationMenuModel, List<NavigationMenuModel>> childMenuMap;
    private NavigationMenuListener navigationMenuListener;

    public NavigationMenuExpandableAdapter(Context mContext, List<NavigationMenuModel> navigationMenuList,
                                           LinkedHashMap<NavigationMenuModel, List<NavigationMenuModel>> childMenuMap) {
        this.mContext = mContext;
        this.navigationMenuList = navigationMenuList;
        this.childMenuMap = childMenuMap;
        navigationMenuListener = (NavigationMenuListener) this.mContext;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup viewGroup) {

        int viewType  = getGroupType(groupPosition);

        NavigationMenuModel navigationMenuModel = navigationMenuList.get(groupPosition);

        switch (viewType) {
            case TYPE_HEADER_VIEW:
                HeaderViewHolder headerViewHolder;
                if (view == null) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.cell_navigation_menu_header, viewGroup, false);

                    headerViewHolder = new HeaderViewHolder(view);
                    view.setTag(headerViewHolder);
                }

                headerViewHolder = (HeaderViewHolder) view.getTag();


                headerViewHolder.textViewName.setText(navigationMenuModel.getTitle());

                break;
            case TYPE_ITEM_VIEW:
                ViewHolder viewHolder;
                if (view == null) {
                    view = LayoutInflater.from(mContext).inflate(R.layout.cell_navigation_menu_group, viewGroup, false);

                    viewHolder = new ViewHolder(view);
                    view.setTag(viewHolder);
                }

                viewHolder = (ViewHolder) view.getTag();


                viewHolder.textViewTitle.setText(navigationMenuModel.getTitle());

                if (getChildrenCount(groupPosition) == 0) {
                    viewHolder.imgViewIndicator.setVisibility(View.GONE);
                } else {
                    viewHolder.imgViewIndicator.setVisibility(View.VISIBLE);
                    viewHolder.imgViewIndicator.setImageResource(isExpanded ? R.drawable.ic_group_up : R.drawable.ic_group_down);
                }

                break;
        }

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {

        NavigationMenuModel navigationMenuModel = childMenuMap.get(navigationMenuList.
                get(groupPosition)).get(childPosition);

        ChildViewHolder childViewHolder;

        if (view == null) {

            view = LayoutInflater.from(mContext).inflate(R.layout.cell_navigation_menu_child, viewGroup, false);

            childViewHolder = new ChildViewHolder(view);
            view.setTag(childViewHolder);
        }

        childViewHolder = (ChildViewHolder) view.getTag();

        childViewHolder.textViewTitle.setText(navigationMenuModel.getTitle());

        return view;
    }

    @Override
    public int getGroupCount() {
        return navigationMenuList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (childMenuMap.get(navigationMenuList.get(groupPosition)) != null) {
            return childMenuMap.get(navigationMenuList.get(groupPosition)).size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return navigationMenuList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childMenuMap.get(navigationMenuList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getGroupTypeCount() {
        return 2;
    }

    @Override
    public int getGroupType(int groupPosition) {
        switch (groupPosition) {
            case 0:
                return TYPE_HEADER_VIEW;
            case 1:
                return TYPE_ITEM_VIEW;
            default:
                return TYPE_ITEM_VIEW;
        }
    }

    private class HeaderViewHolder {

        AppCompatTextView textViewName;
        AppCompatTextView textViewLogout;

        private ImageView imgViewProfile;

        public HeaderViewHolder(View view) {

            textViewName = view.findViewById(R.id.textViewName);
            textViewName.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewName.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

            textViewLogout = view.findViewById(R.id.textViewLogout);
            textViewLogout.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewLogout.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

            imgViewProfile = view.findViewById(R.id.imgViewProfile);
            imgViewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (navigationMenuListener != null) {
                        navigationMenuListener.onNavigationMenuClick(AppConstant.NAVIGATION_INTENT_PERSONAL_INFO);
                    }
                }
            });

            textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgViewProfile.performClick();
                }
            });

            textViewLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (navigationMenuListener != null) {
                        navigationMenuListener.onNavigationMenuClick(-1);
                    }

                }
            });

        }
    }

    private class ViewHolder {

        AppCompatTextView textViewTitle;
        ImageView imgViewIndicator;

        public ViewHolder(View view) {

            textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

            imgViewIndicator = view.findViewById(R.id.imgViewIndicator);

        }
    }

    private class ChildViewHolder {

        AppCompatTextView textViewTitle;

        public ChildViewHolder(View view) {

            textViewTitle = view.findViewById(R.id.textViewTitle);
            textViewTitle.setTypeface(Utils.getTypeFaceBodoni72(mContext));
            textViewTitle.getPaint().setShader(Utils.getTextGradient(new int[]{mContext.getResources().getColor(R.color.colorStartGold),
                    mContext.getResources().getColor(R.color.colorEndGold)}, new float[]{0, 1}));

        }
    }
}
