package no.ntnu.shaharib.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import no.ntnu.shaharib.helicopter.R;

/**
 * Created by ShahariarKabir on 31.01.2015.
 * shaharib@stud.ntnu.no
 */
public class Menu extends Activity {

    // screen constists of 3 buttons, adding clicklistener to them
    // so the buttons leads us to the correct task.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button task1AND2 = (Button)findViewById(R.id.buttonTask1AND2);
        Button task3 = (Button)findViewById(R.id.buttonTask3);
        Button task4 = (Button)findViewById(R.id.buttonTask4);

        task1AND2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, MyGame.class);
                intent.putExtra("menuItem", 1);
                startActivity(intent);
            }
        });

        task3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, MyGame.class);
                intent.putExtra("menuItem", 2);
                startActivity(intent);
            }
        });

        task4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this, MyGame.class);
                intent.putExtra("menuItem", 3);
                startActivity(intent);
            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
