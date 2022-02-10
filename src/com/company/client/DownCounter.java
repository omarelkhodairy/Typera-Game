package com.company.client;
import java.util.Timer;
import java.util.TimerTask;

public class DownCounter{
    private long delay = 0L;
    private  int counter = 5;



    public DownCounter(long d) {
        this.delay = d;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task,delay,1000);
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (counter !=0) {
                counter--;
                System.out.println(counter +1);
            }
        }
    };
}
