package com.gharpeshiksha.tutorapp.Adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gharpeshiksha.tutorapp.fragments.ChatsFragment;
import com.gharpeshiksha.tutorapp.fragments.ClassesForMeFragment;
import com.gharpeshiksha.tutorapp.fragments.EnquiriesFragment;
import com.gharpeshiksha.tutorapp.fragments.Search;

public class HomeSectionsPageAdapter extends FragmentPagerAdapter {


    public HomeSectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                return new ClassesForMeFragment("", "", "", "", "","");
            case 1:
                return new Search();
            case 2:
                return new EnquiriesFragment();
           /* case 3:
                return new SearchViewFragment();*/
            case 3:
                return new ChatsFragment();

                default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Classes Matched";
            case 1:
                return "All Classes";
            case 2:
                return "Applied Leads";
            case 3:
                return "Favourites";
            case 4:
                return "Chats";


        }
        return "";
    }


}
