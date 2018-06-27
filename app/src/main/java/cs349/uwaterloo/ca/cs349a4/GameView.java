package cs349.uwaterloo.ca.cs349a4;

/**
 * Created by fy on 2017/12/3.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class GameView extends View implements Observer {

    private Paint paint;
    private Model mModel;
    private int m_circles;
    private ArrayList<Point> m_circlePositions;
    private int height;
    private int width;
    private int radius = 150;
    private Point m_Selected;
    private Handler mHandler;
    private long m_FlushStartTime;
    private Point m_ClickPosition;
    private int index = 0;

    public GameView(Context context) {
        super(context);

        mModel = Model.getInstance();
        // create the Paint and set its color
        paint = new Paint();
        paint.setColor(Color.BLACK);

        post(new Runnable() {
                 @Override
                 public void run() {
                     width = getWidth();
                     height = getHeight();
                     SetPositions(m_circles);
                 }
             });

        m_circlePositions = new ArrayList<>();
        m_circles = mModel.getCircles();

        m_Selected = new Point(-1,-1);


        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if the game is not started yet, start
                if (!mModel.getIsStarted()) {
                    mModel.setHumanWon(-1);
                    mModel.setIsStarted(true);
                    mModel.ComputerPlay();
                    mModel.setIsComputerPlay(true);
                    return;
                }

                // check if computer is playing
                if (mModel.getIsComputerPlay()) return;

                m_Selected = new Point(-1,-1);

                // check that if the click is inside one of the circles
                for (int i = 0; i < m_circlePositions.size();i++) {
                    if (Math.abs(m_circlePositions.get(i).x - m_ClickPosition.x) <= 150 &&
                            Math.abs(m_circlePositions.get(i).y - m_ClickPosition.y) <= 150) {
                        m_Selected = m_circlePositions.get(i);
                        break;
                    }
                }

                // if the click is inside one of the circles
                if (m_Selected.x != -1) {
                   Flush();
                    if (!mModel.CheckInput(m_Selected)) { // the point is incorrect
                        mModel.setHumanWon(0);
                    } else { // the point is correct
                        if (mModel.getComputerPlay().size() == 0) { // player's sequence is the same to computer
                            mModel.setHumanWon(1);
                        }
                    }
                }
            }
        });

        this.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    m_ClickPosition = new Point(x,y);
                    v.performClick();
                }
                return true;
            }
        });

        mHandler = new Handler();

        mModel.addObserver(this);
        mModel.initObservers();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);

        // Draw Circles
        for (int i = 0; i < m_circlePositions.size() ; i++) {
            canvas.drawCircle(m_circlePositions.get(i).x, m_circlePositions.get(i).y, radius,paint);
        }

        // Draw "Flush"
        if (m_Selected.x != -1 && m_Selected.y != -1) {
            double percent = (double)(System.currentTimeMillis() - m_FlushStartTime) / 500;
            canvas.drawCircle(m_Selected.x, m_Selected.y, (float) (radius * (1-percent)), paint);
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();

        return true;
    }

    @Override
    public void update(Observable o, Object arg)
    {
        Log.d(String.valueOf(R.string.DEBUG_MVC_ID), "GameView: Update");
        if (mModel.getCircles() != m_circles) { // check if the number of circles changed
            m_circles = mModel.getCircles();
            SetPositions(m_circles);
        }

        if (mModel.getIsComputerPlay()) { // Flush computer play
            ShowComputerPlay(mModel.getComputerPlay());
        }

        invalidate();
    }

    // Helper function

    // set the positions of circles according to the number of circles
    void SetPositions(int n) {
        m_circlePositions.clear();
        int TwoCirclesPerRow = (width-2*radius*2) / 3; //
        int OneCirclePerRow = width/2;
        int rows = n % 2 == 0 ? n / 2 : n / 2 + 1;
        int gap = (height - rows * 2 * radius) / (rows+1);

        for (int i = 1 ; i <= rows; i++) {
            if (i == rows && n % 2 == 1) {
                Point last = new Point(OneCirclePerRow,gap*i + (2*(i-1)*radius) + radius);
                m_circlePositions.add(last);
            } else {
                Point first = new Point(TwoCirclesPerRow + radius,gap*i + (2*(i-1)*radius) + radius);
                Point second = new Point(TwoCirclesPerRow*2 + radius*3,gap*i + (2*(i-1)*radius) + radius);
                m_circlePositions.add(first);
                m_circlePositions.add(second);
            }
        }

        mModel.setCirclesPositions(m_circlePositions);
    }

    // Flush animation
    void Flush() {
        m_FlushStartTime = System.currentTimeMillis();

        final Runnable run = new Runnable() {
            @Override
            public void run() {
                if((System.currentTimeMillis() - m_FlushStartTime) / 10000 > 5)
                { // check if duration is larger that 0.5 seconds
                    mHandler.removeCallbacks(this);
                }
                else
                {
                    invalidate();
                    mHandler.postDelayed(this, 1000/60);
                }
            }
        };
        mHandler.postDelayed(run, 1000/60);
    }

    // Show computer play
    void ShowComputerPlay(final ArrayList<Point> computerplay) {
        index = 0;
        Runnable run = new Runnable() {
            @Override
            public void run() {
                if (index < computerplay.size()) {
                    m_Selected = computerplay.get(index);
                    Flush();
                    index++;

                    // different level has different delay
                    mHandler.postDelayed(this,2000-mModel.getLevel()*500);
                } else {
                    mModel.setIsComputerPlay(false); // computer play is done
                    mHandler.removeCallbacks(this);
                }
            }
        };
        mHandler.postDelayed(run,500);
    }

}