package cs349.uwaterloo.ca.cs349a4;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by fy on 2017/12/4.
 */

public class DialogView {
    public void showDialog(final Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.menu_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.gameplay_text);
        text.setText(msg);

        Button restart = (Button) dialog.findViewById(R.id.game_restart);
        restart.setText("Restart");

        Button mainMenu = (Button) dialog.findViewById(R.id.main_menu);
        mainMenu.setText("Main Menu");

        Button settings = (Button) dialog.findViewById(R.id.settings);
        settings.setText("Settings");

        Button instructions = (Button) dialog.findViewById(R.id.instructions);
        instructions.setText("Instructions");


        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(activity, StartActivity.class);

                // Start activity
                activity.startActivity(intent);
                activity.finish();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(activity, SettingsActivity.class);

                // Start activity
                activity.startActivity(intent);
                activity.finish();
            }
        });

        instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(activity, InstructionActivity.class);

                // Start activity
                activity.startActivity(intent);
                activity.finish();
            }
        });

        dialog.show();
    }
}
