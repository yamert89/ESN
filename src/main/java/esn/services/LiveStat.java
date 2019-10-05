package esn.services;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Service("live_stat")
public class LiveStat {

    private Set<Integer> idOfUsersWhichOnline = new ConcurrentSkipListSet<>();
    private Map<String, Integer> countRequest = new ConcurrentHashMap<>();

    public LiveStat() {
        new Timer().schedule(new RequestCountCleaner(), 600000, 60000);
    }

    public void userLogged(int id){
        idOfUsersWhichOnline.add(id);
    }

    public void userLogout(int id){
        idOfUsersWhichOnline.remove(id);
    }

    public boolean userIsOnline(int id){
        return idOfUsersWhichOnline.contains(id);
    }

    public Set<Integer> getIdOfUsersWhichOnline() {
        return idOfUsersWhichOnline;
    }

    public int getCountUsersOnline(){
        return idOfUsersWhichOnline.size();
    }

    public void request(String ip){
        Integer count = countRequest.get(ip);
        if (count == null) countRequest.put(ip, 1);
        else countRequest.put(ip, ++count);
    }

    public boolean canRequest(String ip){
        return countRequest.get(ip) < 1000;
    }

    @PostConstruct
    public void post(){
        System.out.println("POST CONSTRUCT " + this);
    }

    private class RequestCountCleaner extends TimerTask{

        @Override
        public void run() {
            Spliterator<Map.Entry<String, Integer>> spliterator = countRequest.entrySet().spliterator();
            spliterator.forEachRemaining(el -> el.setValue(0));
        }
    }


}
