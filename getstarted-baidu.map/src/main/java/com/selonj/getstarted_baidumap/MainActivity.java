package com.selonj.getstarted_baidumap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import static android.widget.LinearLayout.VERTICAL;
import static com.selonj.getstarted_baidumap.MapBugActivity.SHOWING_MAP_FIRST;

/**
 * Created by L.x on 16-4-19.
 */
public class MainActivity extends Activity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    install(demo("Showing map at the first tab", true), demo("Showing map at the last tab", false));
  }

  private void install(View... children) {
    LinearLayout container = new LinearLayout(this);
    container.setOrientation(VERTICAL);
    for (View child : children) container.addView(child);
    setContentView(container);
  }

  private Button demo(String text, boolean showingMapTabFirst) {
    Button button = new Button(this);
    button.setText(text);
    button.setOnClickListener(startMapBugActivity(showingMapTabFirst));
    return button;
  }

  private View.OnClickListener startMapBugActivity(final boolean showingMapTabFirst) {
    return new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, MapBugActivity.class);
        intent.putExtra(SHOWING_MAP_FIRST, showingMapTabFirst);
        startActivity(intent);
      }
    };
  }
}
