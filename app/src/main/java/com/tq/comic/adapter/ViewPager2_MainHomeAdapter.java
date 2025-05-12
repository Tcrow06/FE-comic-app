package com.tq.comic.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.tq.comic.fragment.HomeFragment;
import com.tq.comic.fragment.MangaFragment;

public class ViewPager2_MainHomeAdapter extends FragmentStateAdapter {

    public ViewPager2_MainHomeAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new HomeFragment();
            case 1: return new MangaFragment();
//            case 2: return new SearchFragment();
//            case 3: return new AccountFragment();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4; // số tab
    }
}
