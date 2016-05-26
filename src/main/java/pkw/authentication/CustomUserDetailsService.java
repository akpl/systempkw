package pkw.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pkw.models.Uzytkownik;
import pkw.repositories.UzytkownikRepository;

import java.util.ArrayList;

/**
 * User details service. This class is responsible for finding user when he tries to login.
 */
@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Uzytkownik user = uzytkownikRepository.findOneByLogin(username);
        if (user == null) throw new UsernameNotFoundException("No user found with name or username: " + username);
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getPoziomDostepu().getNazwa());
        authorities.add(grantedAuthority);
        UserDetails userDetails = new User(username, user.getHaslo(), user.isAktywny(), true, true, true, authorities);
        return userDetails;
    }
}
