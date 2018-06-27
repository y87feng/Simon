package cs349.uwaterloo.ca.cs349a4;

import android.graphics.Point;
import android.util.Log;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * MVC2 Model
 * <p>
 * Created by J. J. Hartmann on 11/19/2017.
 * Email: j3hartma@uwaterloo.ca
 * Copyright 2017
 */

class Model extends Observable
{
    // Create static instance of this mModel
    private static final Model ourInstance = new Model();
    static Model getInstance()
    {
        return ourInstance;
    }

    // Private Variables
    private int m_Circles;
    private int m_SeqenceLength;
    private int m_Score;
    private int m_level; // 1 is easy, 2 is normal, 3 is hard
    private boolean m_IsStarted;
    private boolean m_IsComputerPlay;
    private ArrayList<Point> m_CirclesPosition;
    private ArrayList<Point> m_ComputerPlay;
    private int m_HumanWon; // -1 is playing, 0 is lose, 1 is won


    private int fps = 60;

    Model() {
        m_Circles = 4;
        m_SeqenceLength = 1;
        m_level = 2;
        m_Score = 0;
        m_IsStarted = false;
        m_ComputerPlay = new ArrayList<>();
        m_HumanWon = -1;
    }

    public int getCircles() { return m_Circles; }
    public void setCircles(int n) {
        m_Circles = n;
        setChanged();
        notifyObservers();
    }

    public int getLevel() { return m_level;}
    public void setLevel(int i) {
        m_level = i;
        setChanged();
        notifyObservers();
    }

    public int getScore() { return m_Score;}
    public int getSequenceLength() { return m_SeqenceLength;}
    public boolean getIsStarted () { return m_IsStarted;}
    public void setIsStarted(boolean isstarted) {
        m_IsStarted = isstarted;
        setChanged();
        notifyObservers();
    }

    public void setCirclesPositions(ArrayList<Point> positions) {
        m_CirclesPosition = positions;
    }

    public boolean getIsComputerPlay() {return m_IsComputerPlay;}
    public void setIsComputerPlay(boolean play) {
        m_IsComputerPlay = play;
        setChanged();
        notifyObservers();
    }

    public ArrayList<Point> getComputerPlay() { return m_ComputerPlay;}

    public int getHumanWon() { return m_HumanWon; }
    public void setHumanWon(int won) {
        m_HumanWon = won;

        // if won is -1, no need to notifyObservers
        if (won == -1) return;

        if (won == 0) {
            m_IsStarted = false;
            reStart();
        } else {
            m_SeqenceLength ++;
            m_Score++;
            m_IsStarted = false;
        }
        setChanged();
        notifyObservers();

    }

    // Computer randomly play
    public void ComputerPlay() {
        Random r = new Random();
        for (int i = 0; i < m_SeqenceLength; i++) {
            int random = r.nextInt(m_Circles - 1) + 1;
            m_ComputerPlay.add(m_CirclesPosition.get(random));
        }
    }

    // restart the game
    public void reStart() {
        m_ComputerPlay.clear();
        m_IsStarted = false;
        m_Score = 0;
        m_SeqenceLength = 1;
    }

    // check the input from touching the screen
    public boolean CheckInput(Point input) {
        if (m_ComputerPlay.get(0) != input) {
            return false;
        } else {
            m_ComputerPlay.remove(0);
            return true;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Observable Methods
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Helper method to make it easier to initialize all observers
     */
    public void initObservers()
    {
        setChanged();
        notifyObservers();
    }

    /**
     * Deletes an observer from the set of observers of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     *
     * @param o the observer to be deleted.
     */
    @Override
    public synchronized void deleteObserver(Observer o)
    {
        super.deleteObserver(o);
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    @Override
    public synchronized void addObserver(Observer o)
    {
        super.addObserver(o);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    @Override
    public synchronized void deleteObservers()
    {
        super.deleteObservers();
    }

    /**
     * If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then notify all of its observers
     * and then call the <code>clearChanged</code> method to
     * indicate that this object has no longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and <code>null</code>. In other
     * words, this method is equivalent to:
     * <blockquote><tt>
     * notifyObservers(null)</tt></blockquote>
     *
     * @see Observable#clearChanged()
     * @see Observable#hasChanged()
     * @see Observer#update(Observable, Object)
     */
    @Override
    public void notifyObservers()
    {
        super.notifyObservers();
    }
}
