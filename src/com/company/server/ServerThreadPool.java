package com.company.server;



import com.company.objects.User;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Original file created by PR of HSRW
 * Edited by Group 2 for DS Typera project WS20/21
 */

public class ServerThreadPool {
	public static ArrayList<ServerSocketTask> threads = new ArrayList<>();
	public static ConcurrentHashMap<Integer, User> accounts = new ConcurrentHashMap<Integer, User>();
	public static CopyOnWriteArrayList<ServerSocketTask> active = new CopyOnWriteArrayList<ServerSocketTask>();
	//public static ConcurrentHashMap<String, ServerSocketTask> active = new ConcurrentHashMap<String, ServerSocketTask>();

	private static final Object lockAccounts = new Object();
	private static final Object lockActive = new Object();
	private static final ExecutorService thPoolServer = Executors.newFixedThreadPool(100); //Create a pool of threads
	public static void main(String[] args) throws IOException {
		//Create a Server Socket
		ServerSocket serverSocket = new ServerSocket(2021); //Start a new server socket on port 1234
		AssignTeams.main();
		TeamsSorter.main();
		while (true) {
			//Create Socket
			Socket connection = serverSocket.accept();//Accept when a request arrives
			ServerSocketTask serverTask = new ServerSocketTask(connection, threads);//Start a task Thread to handle client request
			threads.add(serverTask);
			thPoolServer.execute(serverTask);//Execute Thread

		}

	}

	public static void addAccount(User user){
		synchronized (lockAccounts){
			accounts.put(accounts.size(),user);
		}
	}



}
