package com.company.server;


import com.company.objects.*;
import com.company.client.Results;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static com.company.server.TeamsSorter.teamResults1;

public class Game implements Runnable{
    private final CountDownLatch latch = new CountDownLatch(2);
    private ServerSocketTask m1;
    private ServerSocketTask m2;
    private ArrayList<String> scripts = new ArrayList<String>();

    public Game(ServerSocketTask m1, ServerSocketTask m2) {
        Random rndm = new Random();
        int x = rndm.nextInt(1 - 0 + 1);
        if(x == 0){
            this.m1 = m1;
            this.m2 = m2;
        }else if(x == 1){
            this.m1 = m2;
            this.m2 = m1;
        }



        scripts.add("The European languages are members of the same family. Their separate existence.");
        scripts.add("Far far away, behind the word mountains, far from the countries Vokalia and Cons");
        scripts.add("A wonderful serenity has taken possession of my entire soul, like these sweet mo");
        scripts.add("One morning, when Gregor Samsa woke from troubled dreams, he found himself happy");
        scripts.add("he quick, brown fox jumps over a lazy dog. DJs flock by when MTV ax quiz prog..");
    }


    @Override
    public void run() {
        m1.setLatch(latch);
        m2.setLatch(latch);
        System.out.println("Waiting for both players to be ready.");
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("All players are ready.... Launching the game.");
        ArrayList<Integer> select = new ArrayList<Integer>(random());
        m1.setScriptID(select.get(0));
        m2.setScriptID(select.get(1));


        m1.setQueueId(0);
        m2.setQueueId(1);
        Content content1 = new Content(m1.getQueueId(),"launch", scripts.get(m1.getScriptID()));
        Content content2 = new Content(m2.getQueueId(),"launch", scripts.get(m2.getScriptID()));

        try {
            m1.write(Serialization.serialize(content1), 3000L);
            m2.write(Serialization.serialize(content2), 3000L);
            Message message1 = m1.read();
            long start = System.nanoTime();
            message1 = m1.read();
            m2.write("go", 4L);
            Result result1 = (Result) Serialization.deSerialize(message1.getMessage());
            Message message2 = m2.read();
            long elapsedTime = System.nanoTime() - start;
            double seconds = (double)elapsedTime / 1_000_000_000.0;
            Result result2 = (Result) Serialization.deSerialize(message2.getMessage());

            Results checkResult1 = new Results(m1.getUsername(),m1.getScriptID(),result1.getScript());
            checkResult1.run();
            Results checkResult2 = new Results(m2.getUsername(),m2.getScriptID(),result2.getScript());
            checkResult2.run();

            double score = 0;
            if(checkResult1.isValid() && checkResult2.isValid()){
                score = (result1.getTime() + result2.getTime());
                System.out.println("Time counted by clients: " + score);
                System.out.println("Time counted by Server: " + seconds);
                Team team = new Team(m1,m2,m1.getTeamId(),seconds);

                teamResults1.put(team.getScore(),team);
                m1.write("Good job: your team score is: " + seconds, 1L);
                m2.write("Good job: your team score is: " + seconds, 1L);
            }else{
                m1.write("Your team is disqualified, please Try again later!", 1L);
                m2.write("Your team is disqualified, please Try again later!", 1L);
            }



        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> random() {
        Random rndm = new Random();
        int x = rndm.nextInt(4 - 0 + 1);
        ArrayList<Integer> select = new ArrayList<Integer>();
        while(true) {
            int y = rndm.nextInt(4 - 0 + 1);

            if (x != y) {
                select.add(x);
                select.add(y);
                break;
            }
        }
       return select;
    }
}
