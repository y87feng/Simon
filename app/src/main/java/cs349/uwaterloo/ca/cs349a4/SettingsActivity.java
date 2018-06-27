package cs349.uwaterloo.ca.cs349a4;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class SettingsActivity extends AppCompatActivity implements Observer{

    Model mModel;
    Button m_level_Easy;
    Button m_level_Normal;
    Button m_level_Hard;
    Button m_Back;
    TextView m_Levels;
    TextView m_number_of_circles;
    SeekBar m_bar_of_circles;
    Button m_LastButtonSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get Model instance
        mModel = Model.getInstance();
        mModel.addObserver(this);

        // Get Buttons reference
        m_Levels = (TextView)findViewById(R.id.difficulty_levels);
        m_Levels.setText("Levels: Normal");

        m_level_Easy = (Button)findViewById(R.id.difficulty_easy);
        m_level_Easy.setText("Easy");

        m_level_Normal = (Button)findViewById(R.id.difficulty_normal);
        m_level_Normal.setText("Normal");

        m_level_Hard = (Button)findViewById(R.id.difficulty_hard);
        m_level_Hard.setText("Hard");

        m_Back = (Button)findViewById(R.id.settings_back_to_main);
        m_Back.setText("<-Back");

        // Set Level initial value
        if (mModel.getLevel() == 1) {
            m_LastButtonSelected = m_level_Easy;
        } else if (mModel.getLevel() == 2) {
            m_LastButtonSelected = m_level_Normal;
        } else {
            m_LastButtonSelected = m_level_Hard;
        }
        m_LastButtonSelected.setSelected(true);
        m_LastButtonSelected.setBackgroundColor(getResources().getColor(R.color.colorHighlight,getTheme()));

        //Get the number of circles
        m_number_of_circles = (TextView) findViewById(R.id.number_of_circles);
        m_number_of_circles.setText("Circles:" + Integer.toString(mModel.getCircles()));

        m_bar_of_circles = (SeekBar) findViewById(R.id.seekBar_of_circles);
        m_bar_of_circles.setMax(5);
        m_bar_of_circles.setProgress(mModel.getCircles()-1);

        // go to Main menu
        m_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create Intent to launch StartActivity activity
                Intent intent = new Intent(v.getContext(), StartActivity.class);

                // Start activity
                startActivity(intent);
                finish();
            }
        });

        // Set the number of circles
        m_bar_of_circles.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                m_number_of_circles.setBackgroundColor(Color.TRANSPARENT);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                m_number_of_circles.setBackgroundColor(getResources().getColor(R.color.colorHighlight,getTheme()));
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "SettingActivity: onProgressChanged");
                m_number_of_circles.setText("Circles:" + Integer.toString(progress + 1));
                mModel.setCircles(progress+1);
            }
        });

        // set level
        m_level_Normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                if (!b.isSelected()) {
                    // Enable seleced button
                    b.setSelected(true);
                    b.setBackgroundColor(getResources().getColor(R.color.colorHighlight,getTheme()));

                    m_Levels.setText("Level: " + b.getText());
                    mModel.setLevel(2);

                    // Disable last button
                    m_LastButtonSelected.setSelected(false);
                    m_LastButtonSelected.setBackgroundResource(android.R.drawable.btn_default);
                    m_LastButtonSelected = b;
                }
            }
        });

        m_level_Easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                if (!b.isSelected()) {
                    // Enable seleced button
                    b.setSelected(true);
                    b.setBackgroundColor(getResources().getColor(R.color.colorHighlight,getTheme()));

                    m_Levels.setText("Level: " + b.getText());
                    mModel.setLevel(1);

                    // Disable last button
                    m_LastButtonSelected.setSelected(false);
                    m_LastButtonSelected.setBackgroundResource(android.R.drawable.btn_default);
                    m_LastButtonSelected = b;
                }
            }
        });

        m_level_Hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button)v;
                if (!b.isSelected()) {

                    // Enable seleced button
                    b.setSelected(true);
                    b.setBackgroundColor(getResources().getColor(R.color.colorHighlight,getTheme()));

                    m_Levels.setText("Level: " + b.getText());
                    mModel.setLevel(3);

                    // Disable last button
                    m_LastButtonSelected.setSelected(false);
                    m_LastButtonSelected.setBackgroundResource(android.R.drawable.btn_default);
                    m_LastButtonSelected = b;
                }
            }
        });

        mModel.initObservers();
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "SettingActivity: onDestory");

        // Remove observer when activity is destroyed.
        mModel.deleteObserver(this);
    }


    public void update(Observable o, Object arg)
    {
        Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "SettingActivity: Update");
    }
}
