package edu.uga.cs.statecapitalsquiz;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StateQuizPagerAdapter extends FragmentStateAdapter {
    public StateQuizPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public Fragment createFragment(int position) {
        return StateQuizFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return StateQuizFragment.getNumberOfQuestions();
    }
}
