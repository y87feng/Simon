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

public class InstructionActivity extends AppCompatActivity implements Observer{

    Model mModel;
    TextView m_InstructionTitle;
    TextView m_GameInstruction;
    Button m_BacktoMain;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "InstructionActivity: OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        // Get Model instance
        mModel = Model.getInstance();
        mModel.addObserver(this);

        // Get TextView Reference
        m_InstructionTitle = (TextView) findViewById(R.id.instruction_title);
        m_InstructionTitle.setText("Instructions");

        m_GameInstruction = (TextView) findViewById(R.id.game_instruction);
        String instructions = "In the game, there are one or more buttons. The computer \"plays\"" +
                " the buttons in a certain sequence by highlighting one button at a time. When the " +
                "computer is done playing, the human tries to reproduce the same sequence by " +
                "clicking on buttons one by one.";
        m_GameInstruction.setText(instructions);

        // Get Button Reference
        m_BacktoMain = (Button) findViewById(R.id.instruction_back_to_main);
        m_BacktoMain.setText("<-Back");

        // go to Main menu
        m_BacktoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Intent to launch StartActivity activity
                Intent intent = new Intent(v.getContext(), StartActivity.class);

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
        Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "InstructionActivity: onDestory");

        // Remove observer when activity is destroyed.
        mModel.deleteObserver(this);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "InstructionActivity: Update");
    }
}
