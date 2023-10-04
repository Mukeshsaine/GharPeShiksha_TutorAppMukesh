package com.gharpeshiksha.tutorapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.gharpeshiksha.tutorapp.data_model.Model_Chats;

import java.util.ArrayList;

public class ChatViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> listOfFrag = new ArrayList<>();
    ArrayList<String> titleList = new ArrayList<>();

    public void addFragment(Fragment fr, String fragName) {
        listOfFrag.add(fr);
        titleList.add(fragName);
    }

    public ChatViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return listOfFrag.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listOfFrag.get(position);
    }

//    @Override
//    public int getItemPosition(Object object) {
//        return POSITION_NONE;
//    }
}
