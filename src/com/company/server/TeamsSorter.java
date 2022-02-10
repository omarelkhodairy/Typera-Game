package com.company.server;

import com.company.objects.Serialization;
import com.company.objects.Team;
import com.company.objects.TeamResult;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


public class TeamsSorter implements Runnable {
    public static CopyOnWriteArrayList<Team> teamResults = new CopyOnWriteArrayList<Team>();
    //public static ConcurrentHashMap<Double, Team> temp = new ConcurrentHashMap<Double, Team>();
    public static ConcurrentHashMap<Double, Team> teamResults1 = new ConcurrentHashMap<Double, Team>();
    public static TreeMap<Double, Team> sorted = new TreeMap<>();
    public static TreeMap<Double, TeamResult> publishResults = new TreeMap<>();


    public static void main() {
        Thread thread = new Thread(new TeamsSorter());
        thread.start();
    }


    @Override
    public void run() {
        while (true) {
            if (!teamResults1.isEmpty()) {
                boolean isNewRecord = false;
                double record = 0;
                ConcurrentHashMap<Double, Team> temp = new ConcurrentHashMap<Double, Team>(teamResults1);
                teamResults1.clear();
                if(!publishResults.isEmpty()){
                    record = publishResults.firstKey();
                }else{
                    isNewRecord = true;
                }
                // TreeMap to store values of HashMap
                sorted.putAll(temp);

                if(record > sorted.firstKey()){
                    isNewRecord = true;
                }

                if(isNewRecord){
                    sorted.firstEntry().getValue().setNewRecord(true);
                }

                for (Map.Entry<Double, Team> entry : sorted.entrySet()){
                    TeamResult tem = new TeamResult();
                    tem.setMember1(entry.getValue().getMember1().getUsername());
                    tem.setMember2(entry.getValue().getMember2().getUsername());
                    tem.setScore(entry.getKey());
                    tem.setNewRecord(entry.getValue().isNewRecord());
                    tem.setTeamID(entry.getValue().getTeamID());
                    tem.setScore(entry.getKey());
                    publishResults.put(entry.getKey(), tem);
                }

                try {
                    toALl(Serialization.serialize(publishResults));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (Map.Entry<Double, TeamResult> entry : publishResults.entrySet())
                    System.out.println("Key = " + entry.getKey() +
                            ", Value = " + entry.getValue());
                System.out.println("separator");
                // Display the TreeMap which is naturally sorted
                for (Map.Entry<Double, Team> entry : sorted.entrySet())
                    System.out.println("Key = " + entry.getKey() +
                            ", Value = " + entry.getValue());
/*
                for (int i = 0; i < temp.size(); i++) {
                    for (int j = 1; j < temp.size(); j++) {
                        if (temp.get(i).getScore() > temp.get(j).getScore()) {
                            Team team = temp.get(j);
                            temp.set(j, temp.get(i));
                            temp.set(i, team);
                        }
                    }
                }

 */


            }
        }
    }

    public void toALl(String s) throws IOException {
        for(Team _team: sorted.values()){
            long check = 6L;
            ServerSocketTask m1 = _team.getMember1();
            ServerSocketTask m2 = _team.getMember2();
            if(_team.isNewRecord()){
                check = 7L;
            }
            m1.write(s,check);
            m2.write(s,check);
        }
        sorted.clear();
    }
}

