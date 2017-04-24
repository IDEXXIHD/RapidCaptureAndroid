package com.idexx.labstation.rapidcaptureapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.idexx.labstation.rapidcaptureapp.R;
import com.idexx.labstation.rapidcaptureapp.adapter.model.HomeOptionItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mhansen on 4/24/2017.
 */
public class HomeOptionExpandableListAdapter extends ExpandableListAdapter<String, HomeOptionItem>
{
    public HomeOptionExpandableListAdapter(Context context)
    {
        this(HomeOptionItem.values(), context);
    }
    public HomeOptionExpandableListAdapter(HomeOptionItem[] items, Context context)
    {
        Map<String, List<HomeOptionItem>> map = new HashMap<>();
        map.put("Placeholder", Arrays.asList(items));
        init(map, context);
    }

    public void updateTitle(String title)
    {
        List<HomeOptionItem> items = content.get(getGroup(0));
        content.remove(getGroup(0));
        content.put(title, items);
        headers.remove(0);
        headers.add(title);

        notifyDataSetChanged();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        convertView = inflateIfNeeded(convertView, R.layout.template_home_option_header);
        String label = getGroup(groupPosition);
        TextView homeOptionLabel = (TextView) convertView.findViewById(R.id.homeOptionHeaderLabel);
        homeOptionLabel.setText(label);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        convertView = inflateIfNeeded(convertView, R.layout.template_home_option_item);
        HomeOptionItem optionItem = getChild(groupPosition, childPosition);
        TextView itemView = (TextView) convertView.findViewById(R.id.homeOptionItemText);
        itemView.setText(optionItem.label);
        return convertView;
    }
}
