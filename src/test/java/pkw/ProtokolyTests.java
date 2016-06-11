package pkw;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import pkw.models.Wybory;
import pkw.repositories.PoziomDostepuRepository;
import pkw.repositories.TypWyborowRepository;
import pkw.repositories.UzytkownikRepository;
import pkw.repositories.WyboryRepository;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SystemPKWApplication.class)
@WebAppConfiguration
@Transactional
public class ProtokolyTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private WyboryRepository wyboryRepository;

    @Autowired
    private TypWyborowRepository typWyborowRepository;

    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private PoziomDostepuRepository poziomDostepuRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    /**
     * Sprawdza czy aplikacja się ładuje.
     */
    @Test
    public void contextLoads() {
    }

    /**
     * Sprawdza czy użytkownik OKW musi potwierdzić wysyłanie wyników wyborów wpisaniem hasła.
     * @throws Exception
     */
    @Test
    @WithMockUser(username="okw",authorities={"CZLONEK_OKW"})
    public void sprawdzBezpieczenstwoDlaOkw() throws Exception {
        Wybory wybory = dodajTestoweWybory();
        mockMvc.perform(get("/panel/protokoly/wyslij?idWybory=" + wybory.getId())).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/panel/protokoly/autoryzuj?idWybory=" + wybory.getId()));
        mockMvc.perform(get("/panel/protokoly/")).andExpect(status().isOk());
    }

    /**
     * Sprawdza czy administrator nie ma dostępu do wysyłania protokołów z wyborów.
     * @throws Exception
     */
    @Test
    @WithMockUser(username="admin",authorities={"ADMINISTRATOR"})
    public void sprawdzBezpieczenstwoDlaAdmin() throws Exception {
        Wybory wybory = dodajTestoweWybory();
        mockMvc.perform(get("/panel/protokoly/wyslij?idWybory=" + wybory.getId())).andExpect(status().isForbidden());
        mockMvc.perform(get("/panel/protokoly/")).andExpect(status().isForbidden());
    }

    /**
     * Sprawdza czy członek PKW nie ma dostępu do wysyłania protokołów z wyborów.
     * @throws Exception
     */
    @Test
    @WithMockUser(username="pkw",authorities={"CZLONEK_PKW"})
    public void sprawdzBezpieczenstwoDlaPkw() throws Exception {
        Wybory wybory = dodajTestoweWybory();
        mockMvc.perform(get("/panel/protokoly/wyslij?idWybory=" + wybory.getId())).andExpect(status().isForbidden());
        mockMvc.perform(get("/panel/protokoly/")).andExpect(status().isForbidden());
    }

    private Wybory dodajTestoweWybory() {
        Wybory wybory = new Wybory();
        wybory.setDataUtworzenia(new LocalDate());
        wybory.setDataGlosowania(new LocalDate().plusDays(10));
        wybory.setTypWyborow(typWyborowRepository.findOne(3));
        wybory.setTworca(uzytkownikRepository.findFirstByPoziomDostepuOrderByIdAsc(poziomDostepuRepository.findOne(1)));
        return wyboryRepository.save(wybory);
    }
}
