package com.mml.drc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.mml.drc.R;
import com.mml.drc.adapter.ViewPagerAdapter;
import com.mml.drc.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.main_new_report)
    Button main_new_report;
    private ViewPager.OnPageChangeListener mOnPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 暫時不用一下代碼  2018-11-15
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        // BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        //禁止ViewPager滑动
        // mViewPager.setOnTouchListener(new View.OnTouchListener() {
        // @Override
        // public boolean onTouch(View v, MotionEvent event) {
        // return true;
        // }
        // });
        setupViewPager(mViewPager);
        main_new_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,NewReportActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(BaseFragment.newInstance("未提交"));
        adapter.addFragment(BaseFragment.newInstance("已提交"));
        adapter.addTitle("未提交");
        adapter.addTitle("已提交");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        mViewPager.removeOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setAdapter(null);
        super.onDestroy();
    }
}
