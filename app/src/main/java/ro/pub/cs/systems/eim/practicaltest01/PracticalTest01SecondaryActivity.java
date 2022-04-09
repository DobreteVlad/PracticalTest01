package ro.pub.cs.systems.eim.practicaltest01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PracticalTest01SecondaryActivity extends AppCompatActivity {
    private EditText numberOfClicksTextView;
    private Button okButton, cancelButton;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.demoButton3:
                    setResult(RESULT_OK, null);
                    break;
                case R.id.demoButton4:
                    setResult(RESULT_CANCELED, null);
                    break;
            }
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_secondary);

        numberOfClicksTextView = (EditText) findViewById(R.id.demoEdit3);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras().containsKey("Total")) {
            int numberOfClicks = intent.getIntExtra("Total", -1);
            numberOfClicksTextView.setText(String.valueOf(numberOfClicks));
        }
        okButton = (Button)findViewById(R.id.demoButton3);
        okButton.setOnClickListener(buttonClickListener);
        cancelButton = (Button)findViewById(R.id.demoButton4);
        cancelButton.setOnClickListener(buttonClickListener);
    }
}