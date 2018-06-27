package cs349.uwaterloo.ca.cs349a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

public class GameActivity extends AppCompatActivity implements Observer
{
    // Private Vars
    Model mModel;
    TextView m_player_score;
    TextView m_player_status;
    TextView m_player_sequence_length;
    boolean m_IsMenuEnabled;

    /**
     * OnCreate
     * -- Called when application is initially launched.
     *
     * @param savedInstanceState
     * @see <a href="https://developer.android.com/guide/components/activities/activity-lifecycle.html">Android LifeCycle</a>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "GameActivity: OnCreate");
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_gameplay);

        // add Game View
        View gameview = new GameView(this);
        ViewGroup viewgroup = (ViewGroup)findViewById(R.id.gameview);
        viewgroup.addView(gameview);

        m_IsMenuEnabled = false;

        // Get Model instance
        mModel = Model.getInstance();
        mModel.addObserver(this);

        // set Player score,status, sequence length
        m_player_score = (TextView)findViewById(R.id.player_score);
        m_player_score.setText("Player Score: " + Integer.toString(mModel.getScore()));

        m_player_sequence_length = (TextView) findViewById(R.id.player_sequence_length);
        m_player_sequence_length.setText("Sequence Length: " + Integer.toString(mModel.getSequenceLength()));

        m_player_status = (TextView)findViewById(R.id.player_status);
        m_player_status.setText("Touch the screen to start");

        // Init observers
        mModel.initObservers();
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "GameActivity: onDestory");
        mModel.reStart();
        mModel.setHumanWon(-1);

        // Remove observer when activity is destroyed.
        mModel.deleteObserver(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Options Menu
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gameplay_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle interaction based on item selection
        Intent intent;
        switch (item.getItemId())
        {
            case R.id.gameplay_goto_mainmenu:
                // Create Intent to launch StartActivity activity
                intent = new Intent(this, StartActivity.class);

                // Start activity
                startActivity(intent);
                finish();
                return true;

            case R.id.gameplay_goto_settings:
                // Create Intent to launch SettingsActivity activity
                intent = new Intent(this, SettingsActivity.class);

                // Start activity
                startActivity(intent);
                finish();
                return true;

            case R.id.gameplay_goto_Instruction:
                // Create Intent to launch InstructionActivity activity
                intent = new Intent(this, InstructionActivity.class);

                // Start activity
                startActivity(intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
        Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "GameActivity: Update");

        m_player_score.setText("Player Score: " + Integer.toString(mModel.getScore()));
        m_player_sequence_length.setText("Sequence Length: " + Integer.toString(mModel.getSequenceLength()));
        String s = "";
        m_player_status.setTextSize(24);
        if (!mModel.getIsStarted() && mModel.getHumanWon() == -1) { // Start
            s = "Touch the screen to start";
        } else if (!mModel.getIsStarted() && mModel.getHumanWon() == 0) { // Player loses
            s = "Player loses, touch the screen to restart";
            m_player_status.setTextSize(20);
        } else if (!mModel.getIsStarted() && mModel.getHumanWon() == 1) { // Player wins
            s = "Player wins, touch the screen to continue";
            m_player_status.setTextSize(20);
        }  else if (mModel.getIsStarted() && mModel.getIsComputerPlay()) { // Computer's turn
            s = "Computer's turn";
        } else if (mModel.getIsStarted() && !mModel.getIsComputerPlay()) { // Player's turn
            s = "Player's turn";
        }
        m_player_status.setText(s);

        //Player loses, pop up dialog
        if (mModel.getHumanWon() == 0) {
            DialogView dialog = new DialogView();
            dialog.showDialog(this,"Player loses");
        }
    }
}