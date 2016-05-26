package pkw;

import org.junit.Assert;
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
import pkw.models.Uzytkownik;
import pkw.repositories.UzytkownikRepository;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SystemPKWApplication.class)
@WebAppConfiguration
@Transactional
public class UzytkownikTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private UzytkownikRepository uzytkownikRepository;

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
	 * Loguje się jako admin, blokuje użytkownika okw i sprawdza czy jest zablokowany, a potem odblokowuje i sprawdza czy jest odblokowany.
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username="admin",authorities={"ADMINISTRATOR"})
	public void sprawdzBlokowanieKont() throws Exception {
		Uzytkownik uzytkownik = uzytkownikRepository.findOneByLogin("okw");
		//blokowanie
		mockMvc.perform(get("/panel/uzytkownik/zablokuj").param("id", String.valueOf(uzytkownik.getId()))).andExpect(status().isOk());
		uzytkownik = uzytkownikRepository.findOneByLogin("okw");
		Assert.assertEquals(false, uzytkownik.isAktywny());
		//odblokowanie
		mockMvc.perform(get("/panel/uzytkownik/odblokuj").param("id", String.valueOf(uzytkownik.getId()))).andExpect(status().isOk());
		uzytkownik = uzytkownikRepository.findOneByLogin("okw");
		Assert.assertEquals(true, uzytkownik.isAktywny());
	}
}
