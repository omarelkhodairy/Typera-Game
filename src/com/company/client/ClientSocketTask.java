package com.company.client;

import com.company.objects.Message;
import com.company.objects.Serialization;
import com.company.objects.User;
import com.company.objects.Content;
import com.company.objects.Result;
import com.company.objects.TeamResult;


import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.TreeMap;

import static com.company.client.ClientThreadPool.input;

/**
 * Original file created by PR of HSRW
 * Edited by Group 2 for DS Typera project WS20/21
 */

public class ClientSocketTask implements Runnable{

    private Socket connection;  //Create Socket
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private int scriptID = 0;


    public void setUsername(String username) {
        ClientSocketTask.username = username;
    }

    public void startConnection() throws IOException {
        connection = connection = new Socket(ip, port); //Create a Client Socket for "localhost" address and port;
        writer = new ObjectOutputStream(connection.getOutputStream());
        reader =  new ObjectInputStream(connection.getInputStream());
        System.out.println("Connected: " + connection);
    }

    public String getUsername() {
        return username;
    }



    private static String username;
    private long check = 0L;
    private static final int port = 2021; // initialize port number
    private static final String ip = "localhost"; // localhost ip address = 127.0.0.1

    public ClientSocketTask() {

    }
    @Override
    public void run()  {
        try {
            System.out.println("Client Thread Booted");
            logSys();
            Message response = read();
            System.out.println(response.getSender() + ", " + response.getMessage());
            response = read();
            System.out.println(response.getSender() + ", " + response.getMessage());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Enter 'ready' when you're ready to play the game.");
            String isReady = input();
            write(isReady, 3L);
            response = read();
            Content content = (Content) Serialization.deSerialize(response.getMessage());
            if(content.getOperation().contains("launch")){
                System.out.println("Enter the text below as fast as you can.");
                DownCounter downCounter = new DownCounter(response.getSerialVersionUID());
                if(content.getQueueID() == 0){
                    try {
                        Thread.sleep(8000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(content.getScript());
                    write("count", 4L);
                    long start = System.nanoTime();
                    String text = input();
                    long elapsedTime = System.nanoTime() - start;
                    double seconds = (double)elapsedTime / 1_000_000_000.0;
                    Result result = new Result(text,seconds);
                    write(Serialization.serialize(result), 4L);
                    System.out.println(text);
                }else if(content.getQueueID() == 1){
                    String script = content.getScript();
                    response = read();
                    if(response.getSerialVersionUID() == 4L){
                        System.out.println(script);
                        long start = System.nanoTime();
                        String text = input();
                        long elapsedTime = System.nanoTime() - start;
                        double seconds = (double)elapsedTime / 1_000_000_000.0;
                        Result result = new Result(text,seconds);
                        write(Serialization.serialize(result), 4L);
                        System.out.println(text);
                    }
                }
                response = read();
                System.out.println(response.getSender() + ": " + response.getMessage() + ", " + response.getSerialVersionUID());

                response = read();
                TreeMap<Double, TeamResult> scores = new TreeMap<>();
                scores = (TreeMap<Double, TeamResult>) Serialization.deSerialize(response.getMessage());
                int counter = 1;
                for (Map.Entry<Double, TeamResult> entry : scores.entrySet())
                    System.out.println(counter + ".  Team ID: "+ entry.getValue().getTeamID() +"Score: " + entry.getKey() +
                            ", Players:  " + entry.getValue().getMember1() + ", " + entry.getValue().getMember2());

                if(response.getSerialVersionUID() == 7L){
                    System.out.println("Congratulation, You have a new game record! " + scores.firstKey());
                }




            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            closeResources();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void logSys() throws IOException, ClassNotFoundException {
        while (true) {
            User user = new User();
            long l = 0;
            while (true) {
                System.out.println("New here? Enter 'Sign up', or 'Login' if you already have an account. ");
                String operation = input().toLowerCase();
                if (operation.equals("sign up")) {
                    l = 1L;
                    break;
                } else if (operation.equals("login")) {
                    l = 2L;
                    break;
                } else {
                    System.out.println("Error, check your input!");
                }
            }
            System.out.println(l);
            System.out.println("Please enter a Username: ");
            String username = input().toLowerCase();
            user.setUsername(username);
            setUsername(username);
            System.out.println("Please enter a Password: ");
            String pass = input();
            user.setPassword(pass);
            startConnection();
            write(Serialization.serialize(user), l);
            Message response = read();
            System.out.println(response.getSender() + ": " + response.getMessage());
            if (response.getSerialVersionUID() == 1L) {
                closeResources();
            }else if(response.getSerialVersionUID() == 2L){
                break;
            }
        }
    }



    public  void write(String m1, long l) throws IOException {
        Message m = new Message(username, m1, l);
        writer.writeObject(m);
    }

    public  Message read() throws IOException, ClassNotFoundException {
        return (Message) reader.readObject();
    }


    public  void closeResources() throws IOException {
        System.out.println("Disconnected");
        reader.close(); //close Request Buffer
        writer.close(); //close Reply Buffer

    }

    public int getScriptID() {
        return scriptID;
    }

    public void setScriptID(int scriptID) {
        this.scriptID = scriptID;
    }
}
