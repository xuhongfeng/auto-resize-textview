package me.cocodrum.autoresize;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * 
 * @author xuhongfeng
 *
 */
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final TextView view = (TextView) findViewById(R.id.text);
        view.postDelayed(new Runnable() {
            
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
                params.width = 200;
                params.height= 200;
                view.setLayoutParams(params);
            }
        }, 3000L);
        view.postDelayed(new Runnable() {
            
            @Override
            public void run() {
                view.setText("Text Changed!");
            }
        }, 6000L);
    }
}
