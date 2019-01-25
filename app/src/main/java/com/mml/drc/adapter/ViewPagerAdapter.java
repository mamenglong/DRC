package com.mml.drc.adapter;

/**
 * 项目名称：Base
 * Created by Long on 2018/11/15.
 * 修改时间：2018/11/15 14:30
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    private final List<Fragment> mFragmentList = new ArrayList<>();
    private ArrayList<String> titleLists=new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {

        super(manager);

    }

    @Override
    public Fragment getItem(int position) {

        return mFragmentList.get(position);

    }


    @Override
    public int getCount() {
        return mFragmentList.size();

    }


    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);

    }

    public void addTitle(String title){
        titleLists.add(title);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titleLists.get(position);
    }

}