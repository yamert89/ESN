package esn.services;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Service("live_stat")
public class LiveStat {

    private Set<Integer> idOfUsersWhichOnline = new ConcurrentSkipListSet<>();

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
}
