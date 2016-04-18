package com.selonj.getstarted_baidumap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.astuetz.PagerSlidingTabStrip;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static java.util.Arrays.asList;

public class MapBugActivity extends FragmentActivity {

  public static final String SHOWING_MAP_FIRST = "showBaiduMapAtFirstTab";
  private int COLOR_PINK = 0xFFF4842D;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    initTabUI(makeTabs(isShowingMapAtTheFirstTab() ?
        asList(new BaiduMapFragment(), TextualFragment.of("主营产品"), TextualFragment.of("农场实景")) :
        asList(TextualFragment.of("主营产品"), TextualFragment.of("农场实景"), new BaiduMapFragment())
    ));
    enableBaiduMap();
  }

  private boolean isShowingMapAtTheFirstTab() {
    return getIntent().getBooleanExtra(SHOWING_MAP_FIRST, false);
  }

  private void initTabUI(TabBasedPagerAdapter tabsAdapter) {
    PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
    ViewPager pager = (ViewPager) findViewById(R.id.pager);
    pager.setAdapter(tabsAdapter);
    pager.setPageMargin(dp(4));
    tabs.setViewPager(pager);
    tabs.setIndicatorColor(COLOR_PINK);
  }

  private TabBasedPagerAdapter makeTabs(List<Fragment> fragments) {
    return new TabBasedPagerAdapter(getSupportFragmentManager(), fragments);
  }

  private void enableBaiduMap() {
    SDKInitializer.initialize(getApplicationContext());
  }

  private int dp(float value) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
  }

  public class TabBasedPagerAdapter<T extends Fragment & Descriable> extends FragmentPagerAdapter {

    private List<T> fragments;

    public TabBasedPagerAdapter(FragmentManager fm, List<T> fragments) {
      super(fm);
      this.fragments = fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return fragment(position).description();
    }

    @Override
    public int getCount() {
      return fragments.size();
    }

    @Override
    public Fragment getItem(final int position) {
      return fragment(position);
    }

    private T fragment(int position) {
      return fragments.get(position);
    }
  }

  public static interface Descriable {
    String description();
  }

  public static class TextualFragment extends Fragment implements Descriable {

    private static final String TEXT_KEY = "text";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      return makeATextView(inflater, getText());
    }

    private TextView makeATextView(final LayoutInflater inflater, final String text) {

      return new TextView(inflater.getContext()) {{
        setText(text);
        setGravity(Gravity.CENTER);
        setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
      }};
    }

    public static Fragment of(String text) {
      TextualFragment fragment = new TextualFragment();
      fragment.setText(text);
      return fragment;
    }

    private void setText(String text) {
      Bundle bundle = getRequiredArguments();
      bundle.putString(TEXT_KEY, text);
    }

    private Bundle getRequiredArguments() {
      Bundle arguments = getArguments();
      if (arguments == null) {
        arguments = new Bundle();
        setArguments(arguments);
      }
      return arguments;
    }

    private String getText() {
      return getRequiredArguments().getString(TEXT_KEY);
    }

    @Override public String description() {
      return getText();
    }
  }

  public static class BaiduMapFragment extends Fragment implements Descriable {
    private MapView mMapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      return mMapView = new MapView(getActivity());
    }

    @Override
    public void onResume() {
      super.onResume();
      mMapView.onResume();
    }

    @Override
    public void onPause() {
      super.onPause();
      mMapView.onPause();
    }

    @Override
    public void onDestroyView() {
      super.onDestroyView();
      mMapView.onDestroy();
    }

    @Override public String description() {
      return "农场详情";
    }
  }
}
