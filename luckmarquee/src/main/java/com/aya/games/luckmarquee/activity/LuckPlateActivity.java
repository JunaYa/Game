package com.aya.games.luckmarquee.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aya.games.luckmarquee.R;
import com.aya.games.luckmarquee.view.LuckPlate;

/**
 * Created by Single on 2015/11/24.
 */
public class LuckPlateActivity extends AppCompatActivity {
    private LuckPlate luckPlate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luck_plate);
        luckPlate = (LuckPlate) findViewById(R.id.luck_plate);
        luckPlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!luckPlate.isStartLuck()) {
                    luckPlate.luckStart();
                } else {
                    if (!luckPlate.isShouldEnd()) {
                        luckPlate.luckEnd();
                    }
                }
            }
        });
    }
}
