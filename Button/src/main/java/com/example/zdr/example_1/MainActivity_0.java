package com.example.zdr.example_1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity_0 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_0);
        //给button添加点击事件，当点击button时弹出一个Toast，显示“单击按钮”
        Button button = (Button) findViewById(R.id.Button);

            button.setOnClickListener(
                    new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Toast.makeText(MainActivity_0.this, "单击按钮", Toast.LENGTH_LONG).show();
                        }
                    }
            );
    }

}
