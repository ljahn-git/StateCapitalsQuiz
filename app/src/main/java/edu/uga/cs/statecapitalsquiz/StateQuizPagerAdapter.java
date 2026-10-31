package edu.uga.cs.statecapitalsquiz;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class StateQuizPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragments = new ArrayList<>();
    public StateQuizPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    @Override
    public Fragment createFragment(int position) {
        //return StateQuizFragment.newInstance(position);
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        //return StateQuizFragment.getNumberOfQuestions();
        return fragments.size();
    }
}
