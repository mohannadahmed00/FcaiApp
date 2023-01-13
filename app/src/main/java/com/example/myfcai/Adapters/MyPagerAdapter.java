package com.example.myfcai.Adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.myfcai.Fragments.CourseFragment;
import com.example.myfcai.Fragments.PostFragment;
import com.example.myfcai.Fragments.TaskFragment;
import com.example.myfcai.Fragments.TableFragment;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    int numOfTabs;
    String type,dep;
    public MyPagerAdapter(FragmentManager fm, int numOfTabs,String type,String dep) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.type=type;
        this.dep=dep;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundleType=new Bundle();
        if (type.equals("s")){
            bundleType.putString("type","s");
            bundleType.putString("dep",dep);
            //return new StudentPostFragment();
        }else {
            bundleType.putString("type","t");
            //return new TeacherPostFragment();
        }
        switch (position) {
            case 0:
                PostFragment postFragment=new PostFragment();
                postFragment.setArguments(bundleType);
                return postFragment;
            case 1:
                TableFragment tableFragment=new TableFragment();
                tableFragment.setArguments(bundleType);
                return tableFragment;
            case 2:
                CourseFragment courseFragment=new CourseFragment();
                courseFragment.setArguments(bundleType);
                return courseFragment;

            case 3:
                TaskFragment taskFragment=new TaskFragment();
                taskFragment.setArguments(bundleType);
                return taskFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}

