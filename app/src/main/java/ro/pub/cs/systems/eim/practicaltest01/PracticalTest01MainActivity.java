package ro.pub.cs.systems.eim.practicaltest01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest01MainActivity extends AppCompatActivity {

    private EditText leftEditText;
    private EditText rightEditText;
    private Button pressMeButton, pressMeTooButton;
    private Button navigateToSecondaryActivityButton;
    private int serviceStatus = Constants.SERVICE_STOPPED;
    private IntentFilter intentFilter = new IntentFilter();
    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int leftNumberOfClicks = Integer.valueOf(leftEditText.getText().toString());
            int rightNumberOfClicks = Integer.valueOf(rightEditText.getText().toString());

            switch(view.getId()) {
                case R.id.demoButton1:
                    leftNumberOfClicks++;
                    leftEditText.setText(String.valueOf(leftNumberOfClicks));
                    break;
                case R.id.demoButton2:
                    rightNumberOfClicks++;
                    rightEditText.setText(String.valueOf(rightNumberOfClicks));
                    break;
                case R.id.demoButton5:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                    int numberOfClicks = Integer.parseInt(leftEditText.getText().toString()) +
                            Integer.parseInt(rightEditText.getText().toString());
                    intent.putExtra("Total", numberOfClicks);
                    startActivityForResult(intent, 1);
                    break;
            }
            if (leftNumberOfClicks + rightNumberOfClicks > Constants.NUMBER_OF_CLICKS_THRESHOLD
                    && serviceStatus == Constants.SERVICE_STOPPED) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra(Constants.FIRST_NUMBER, leftNumberOfClicks);
                intent.putExtra(Constants.SECOND_NUMBER, rightNumberOfClicks);
                getApplicationContext().startService(intent);
                serviceStatus = Constants.SERVICE_STARTED;
            }
        }
    }
    private MessageBroadcastReceiver messageBroadcastReceiver = new MessageBroadcastReceiver();
    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(Constants.BROADCAST_RECEIVER_TAG, intent.getStringExtra(Constants.BROADCAST_RECEIVER_EXTRA));
        }
    }
    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        leftEditText = (EditText)findViewById(R.id.demoEdit1);
        rightEditText = (EditText)findViewById(R.id.demoEdit2);

        pressMeButton = (Button)findViewById(R.id.demoButton1);
        pressMeTooButton = (Button)findViewById(R.id.demoButton2);

        leftEditText.setText(String.valueOf(0));
        rightEditText.setText(String.valueOf(0));

        pressMeButton.setOnClickListener(buttonClickListener);
        pressMeTooButton.setOnClickListener(buttonClickListener);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("Left")) {
                leftEditText.setText(savedInstanceState.getString("Left"));
            } else {
                leftEditText.setText(String.valueOf(0));
            }
            if (savedInstanceState.containsKey("Right")) {
                rightEditText.setText(savedInstanceState.getString("Right"));
            } else {
                rightEditText.setText(String.valueOf(0));
            }
        } else {
            leftEditText.setText(String.valueOf(0));
            rightEditText.setText(String.valueOf(0));
        }
        navigateToSecondaryActivityButton = (Button)findViewById(R.id.demoButton5);
        navigateToSecondaryActivityButton.setOnClickListener(buttonClickListener);
        for (int index = 0; index < Constants.actionTypes.length; index++) {
            intentFilter.addAction(Constants.actionTypes[index]);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("Left", leftEditText.getText().toString());
        savedInstanceState.putString("Right", rightEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey("Left")) {
            leftEditText.setText(savedInstanceState.getString("Left"));
        } else {
            leftEditText.setText(String.valueOf(0));
        }
        if (savedInstanceState.containsKey("Right")) {
            rightEditText.setText(savedInstanceState.getString("Right"));
        } else {
            rightEditText.setText(String.valueOf(0));
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 1) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }
}