package pkw.services;

import net.sf.uadetector.ReadableUserAgent;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import pkw.models.Logowanie;
import pkw.models.Uzytkownik;
import pkw.repositories.LogowanieRepository;
import pkw.repositories.UzytkownikRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Elimas on 2016-03-29.
 */
@Service
public class LoginLogger extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private LogowanieRepository logowanieRepository;

    @Autowired
    private UserAgentParserService userAgentParserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        User user = (User) authentication.getPrincipal();
        Uzytkownik uzytkownik = uzytkownikRepository.findOneByLogin(user.getUsername());
        Logowanie logowanie = new Logowanie();
        logowanie.setUzytkownik(uzytkownik);
        logowanie.setDataLogowania(new LocalDateTime());
        logowanie.setIp(request.getRemoteAddr());
        logowanie.setUa(request.getHeader("User-Agent"));
        ReadableUserAgent ua = userAgentParserService.parse(request.getHeader("User-Agent"));
        logowanie.setPrzegladarka(ua.getFamily().getName());
        logowanie.setOs(ua.getOperatingSystem().getName());
        logowanie.setTypUrzadzenia(ua.getDeviceCategory().getCategory().name());
        logowanie.setPrzegladarkaIkona(ua.getIcon());
        logowanie.setOsIkona(ua.getOperatingSystem().getIcon());
        logowanieRepository.save(logowanie);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
