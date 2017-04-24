package com.idexx.labstation.rapidcaptureapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;

import com.idexx.labstation.rapidcaptureapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mhansen on 4/24/2017.
 */
public abstract class ExpandableListAdapter<T1, T2> extends BaseExpandableListAdapter
{
    protected Context context;
    protected List<T1> headers;
    protected Map<T1, List<T2>> content;

    protected ExpandableListAdapter() {}

    public ExpandableListAdapter(Map<T1, List<T2>> content, Context context)
    {
        init(content, context);
    }

    protected void init(Map<T1, List<T2>> content, Context context)
    {
        this.content = content;
        this.context = context;
        this.headers = content == null ? null : new ArrayList<>(content.keySet());
    }

    @Override
    public int getGroupCount()
    {
        return content.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return content.get(headers.get(groupPosition)).size();
    }

    @Override
    public T1 getGroup(int groupPosition)
    {
        return headers.get(groupPosition);
    }

    @Override
    public T2 getChild(int groupPosition, int childPosition)
    {
        return content.get(headers.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return Long.valueOf(groupPosition + "" + childPosition);
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    protected View inflateIfNeeded(View convertView, int resource)
    {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource, null);
        }
        return convertView;
    }
}
