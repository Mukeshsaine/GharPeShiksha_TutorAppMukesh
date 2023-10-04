package com.gharpeshiksha.tutorapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.gharpeshiksha.tutorapp.R;
import com.gharpeshiksha.tutorapp.constant.ConstantManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomExpendibleViewAdaptor extends BaseExpandableListAdapter {


    HashMap<String, List<String>> subjects;
    List<String> course = new ArrayList<>();
    private boolean isFromMyCategoriesFragment;
    private Context context;
    static String status[][];
    static String status1[];
    LayoutInflater inflater;
    ExpandableListView exp_list;


    ViewHolderParent viewHolderParent;
    final String TAG = CustomExpendibleViewAdaptor.this.toString();

    public CustomExpendibleViewAdaptor(Context context, List<String> course, HashMap<String, List<String>> subjects, boolean isFromMyCategoriesFragment, String status[][], String status1[],
                                       ExpandableListView exp_list
    ) {

        this.context = context;
        this.course = course;
        this.subjects = subjects;
        this.isFromMyCategoriesFragment = isFromMyCategoriesFragment;
        this.status = status;
        this.status1 = status1;
        this.exp_list = exp_list;

    }

    @Override
    public int getGroupCount() {
        return course.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return subjects.get(course.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return course.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return subjects.get(course.get(groupPosition)).get(childPosition);
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final ViewHolderParent viewHolderParent;
        if (convertView == null) {

            inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.parent_layout, null);
            viewHolderParent = new ViewHolderParent();

            viewHolderParent.grouptextchecked = convertView.findViewById(R.id.heading_item);

            convertView.setTag(viewHolderParent);
        } else {
            viewHolderParent = (ViewHolderParent) convertView.getTag();
        }

        viewHolderParent.grouptextchecked.setText(this.getGroup(groupPosition).toString());


        if (status1[groupPosition] == ConstantManager.CHECK_BOX_CHECKED_TRUE) {

            exp_list.expandGroup(groupPosition);
            onGroupExpanded(groupPosition);
            notifyDataSetChanged();


        } else {
            exp_list.collapseGroup(groupPosition);
        }
        return convertView;
    }


    @SuppressLint("ResourceType")
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final ViewHolderChild viewHolderChild;

        String child = (String) this.getChild(groupPosition, childPosition);

        if (convertView == null) {
            inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout, null);

            viewHolderChild = new ViewHolderChild();

            viewHolderChild.checkedTextView = convertView.findViewById(R.id.child_item);

            convertView.setTag(viewHolderChild);

        } else {

            viewHolderChild = (ViewHolderChild) convertView.getTag();
        }

        if (status[groupPosition][childPosition].equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
            //   Log.e(TAG, "getChildView: groupPosition: "+groupPosition+"childPosition"+childPosition +"getChildView: status: "+ConstantManager.CHECK_BOX_CHECKED_TRUE );

            viewHolderChild.checkedTextView.setChecked(true);
            notifyDataSetChanged();
        } else {
            //   Log.e(TAG, "getChildView: groupPosition: "+groupPosition+"childPosition"+childPosition +"getChildView: status: "+ConstantManager.CHECK_BOX_CHECKED_FALSE );

            viewHolderChild.checkedTextView.setChecked(false);
            notifyDataSetChanged();
        }


        viewHolderChild.checkedTextView.setText(child);

        if (viewHolderChild.checkedTextView.isChecked()) {
            status[groupPosition][childPosition] = ConstantManager.CHECK_BOX_CHECKED_TRUE;
            notifyDataSetChanged();
        } else {
            status[groupPosition][childPosition] = ConstantManager.CHECK_BOX_CHECKED_FALSE;

            notifyDataSetChanged();
        }
        // Log.e(TAG, "getChildView: groupPosition: "+groupPosition+"childPosition"+childPosition );

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolderChild {
        CheckedTextView checkedTextView;

    }


    class ViewHolderParent {
        TextView grouptextchecked;


    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }
}
