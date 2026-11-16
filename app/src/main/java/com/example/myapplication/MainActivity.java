package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView counterText;
    private TextView messageText;
    private TextView hitsText;

    private Button startButton;
    private Button stopButton;

    private Handler handler = new Handler();
    private int counter = 0;
    private int hits = 0;

    private boolean isRunning = false;

    private int delay = 500;

    private Runnable counterRunnable = new Runnable() {
        @Override
        public void run() {
            counter = (counter + 1) % 10;

            counterText.setText(String.valueOf(counter));

            handler.postDelayed(this, delay);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        counterText = findViewById(R.id.counterText);
        messageText = findViewById(R.id.messageText);
        hitsText = findViewById(R.id.hitsText);

        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);

        stopButton.setEnabled(false);

        startButton.setOnClickListener(view -> {
            if (!isRunning) {
                counter = 0;

                counterText.setText("0");
                messageText.setText("");

                isRunning = true;

                startButton.setEnabled(false);
                stopButton.setEnabled(true);

                handler.postDelayed(counterRunnable, delay);
            }
        });

        stopButton.setOnClickListener(view -> {
            if (isRunning) {
                handler.removeCallbacks(counterRunnable);

                isRunning = false;

                startButton.setEnabled(true);
                stopButton.setEnabled(false);

                if (counter == 7) {
                    messageText.setText("Trafienie!");
                    hits++;

                    hitsText.setText("Trafienia: " + hits);


                    delay = Math.max(200, delay - 50);

                } else {
                    messageText.setText(
                            "Pudło! Zatrzymano na " + counter
                    );
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(counterRunnable);
        super.onDestroy();
    }
}