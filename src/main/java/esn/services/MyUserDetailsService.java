package esn.services;

import esn.db.UserDAO;
import esn.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/*@Service*/
public class MyUserDetailsService implements UserDetailsService {


    private UserDAO user_dao;

    @Autowired
    public void setUser_dao(UserDAO user_dao) {
        this.user_dao = user_dao;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = user_dao.getUserByLogin(s);
        if (user == null) throw new UsernameNotFoundException(s);
        return user.isAdmin() ? new MyAdminPrincipal(user) : new MyUserPrincipal(user);
    }
}
