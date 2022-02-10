package com.company.server;


import com.company.objects.Message;
import com.company.objects.Serialization;
import com.company.objects.User;

import static com.company.server.ServerThreadPool.accounts;
import static com.company.server.ServerThreadPool.*;

import java.io.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
/**
 * Original file created by PR of HSRW
 * Edited by Group 2 for DS Typera project WS20/21
 */
public class ServerSocketTask implements Runnable, Serializable{
    private  Socket connection;  // Create Socket
    private ArrayList<ServerSocketTask> threads1 = new ArrayList<>();
    private final ObjectOutputStream writer;
    private final ObjectInputStream reader;
    private String username =" ";
    private String teammate =" ";
    private int teamId = 0;
    private int scriptID =0;
    private int queueId = 0;
    private  long check = 1L;
    private CountDownLatch latch;

    public ServerSocketTask(Socket s,ArrayList<ServerSocketTask> a) throws IOException {
        this.connection = s;
        this.threads1 = a;
        writer = new ObjectOutputStream(connection.getOutputStream());
        reader =  new ObjectInputStream(connection.getInputStream());
        System.out.println("Connected: " + connection);
    }

    @Override
    public void run() {
        try {
            logSys();
            if(check == 1L){
                try {
                    Thread.sleep(1000); // delay the thread. Time delay = size of request string in seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                closeResources();
            }else if(check == 2L){
                write("Waiting for a teammate to join: ", 2L);
                addActive();

                waiting();
                try {
                    Thread.sleep(500000); // delay the thread. Time delay = size of request string in seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                closeResources();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } }


    public void logSys() throws IOException, ClassNotFoundException {
        Message request = read();
        User user = (User) Serialization.deSerialize(request.getMessage());
        String reply = null;
        long l = 0L;
        if(request.getSerialVersionUID() == 1L){
            AtomicBoolean check = new AtomicBoolean(true);
            accounts.forEach((k, v) -> {
                if(v.getUsername().equals(user.getUsername())){
                    check.set(false);
                }
            });
            if(check.get()) {
                addAccount(user);
                reply = "Your registration was Successful, you can now login.";
            }
            else{
                reply = "Username already Exists, Try to be more creative.";
            }
            l = 1L;
        }else if(request.getSerialVersionUID() == 2L){
            AtomicBoolean check = new AtomicBoolean(false);
            accounts.forEach((k, v) -> {
                if(v.getUsername().equals(user.getUsername()) && v.getPassword()==(user.getPassword())){
                    check.set(true);
                }
            });
            if(check.get()) {
                reply = "login Successful. Welcome back.";
                l = 2L;
                setCheck(l);
                setUsername(user.getUsername());
                Thread.currentThread().setName(username);
                System.out.println("Thread details; " + Thread.currentThread().getName());

            }
            else{
                reply = "Wrong credentials, please check your inputs.";
                l = 1L;

            }
        }
        write(reply, l);

    }

    public void waiting() throws IOException, ClassNotFoundException {
        Message request = read();
        if(request.getMessage().toLowerCase().contains("ready")){
            latch.countDown();
        }
    }

   public void addActive(){
       for(ServerSocketTask _client: threads1){
           if(_client.getUsername().equals(username) && !active.contains(_client)){
               active.add(_client);
           }
       }

       for(ServerSocketTask _client: active){
           System.out.println(_client.getUsername() + ":" + _client.getConnection());
       }


   }




    public void toALl(String s) throws IOException {
        for(ServerSocketTask _client: threads){
            _client.write(s,2L);

        }
    }

    public void write(String m1, long l) throws IOException {
        Message m = new Message("Server", m1, l);
        writer.writeObject(m);

    }

    public Message read() throws IOException, ClassNotFoundException {
        Message m = (Message) reader.readObject();
        return m;
    }


    public void closeResources() throws IOException {
        System.out.println(username + " Disconnected");
        reader.close(); //close Request Buffer
        writer.close(); //close Reply Buffer

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getCheck() {
        return check;
    }

    public void setCheck(long check) {
        this.check = check;
    }

    public Socket getConnection() {
        return connection;
    }

    public String getTeammate() {
        return teammate;
    }

    public void setTeammate(String teammate) {
        this.teammate = teammate;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public int getScriptID() {
        return scriptID;
    }

    public void setScriptID(int scriptID) {
        this.scriptID = scriptID;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }
}
