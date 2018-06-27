package cs349.uwaterloo.ca.cs349a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class StartActivity extends AppCompatActivity implements Observer
{
    // Private Vars
    Model mModel;
    TextView mWelcome;
    Button m_StartGame;
    Button m_Setting;
    Button m_Instruction;


    /**
     * OnCreate
     * -- Called when application is initially launched.
     *    @see <a href="https://developer.android.com/guide/components/activities/activity-lifecycle.html">Android LifeCycle</a>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "StartActivity: OnCreate");
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_main);

        // Get Model instance
        mModel = Model.getInstance();
        mModel.addObserver(this);

        // Get text reference
        mWelcome = (TextView) findViewById(R.id.welcome);
        mWelcome.setText("Welcome to Simon");

        // Get button reference.
        m_StartGame = (Button) findViewById(R.id.m_StartGame);
        m_StartGame.setText("Start Game");

        m_Setting = (Button)findViewById(R.id.m_Setting);
        m_Setting.setText("Settings");

        m_Instruction = (Button)findViewById(R.id.m_Instruction);
        m_Instruction.setText("How to Play?");


        m_StartGame.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Create Intent to launch GameActivity activity
                Intent intent = new Intent(v.getContext(), GameActivity.class);

                // Start activity
                startActivity(intent);
                finish();
            }
        });

        m_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Intent to launch SettingsActivity activity
                Intent intent = new Intent(v.getContext(), SettingsActivity.class);

                // Start activity
                startActivity(intent);
                finish();
            }
        });

        m_Instruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Intent to launch InstructionActivity activity
                Intent intent = new Intent(v.getContext(), InstructionActivity.class);

                // Start activity
                startActivity(intent);
                finish();
            }
        });

        // Init observers
        mModel.initObservers();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "StartActivity: onDestory");

        // Remove observer when activity is destroyed.
        mModel.deleteObserver(this);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg)
    {
    }
}
