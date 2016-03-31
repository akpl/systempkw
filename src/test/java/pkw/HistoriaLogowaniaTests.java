package pkw;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import pkw.models.Logowanie;
import pkw.repositories.LogowanieRepository;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SystemPKWApplication.class)
@WebAppConfiguration
@Transactional
public class HistoriaLogowaniaTests {

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private LogowanieRepository logowanieRepository;

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
	 * Sprawdza czy dla użytkownika z prawami innymi niż Admin historia logowań nie jest dostępna.
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username="admin",authorities={"ADMINISTRATOR"})
	public void sprawdzBezpieczenstwoDlaAdministratora() throws Exception {
		mockMvc.perform(get("/uzytkownik/historia")).andExpect(status().isOk());
		mockMvc.perform(get("/uzytkownik/historia/0")).andExpect(status().isOk());
	}

	/**
	 * Sprawdza czy dla użytkownika z prawami innymi niż Admin historia logowań nie jest dostępna.
	 * @throws Exception
     */
	@Test
	public void sprawdzBezpieczenstwoDlaNiezalogowanego() throws Exception {
		MvcResult mvcResult = mockMvc.perform(post("/uzytkownik/historia"))
				.andExpect(status().is3xxRedirection()).andReturn();
		String location = mvcResult.getResponse().getHeader("Location");
		Assert.assertTrue(location.contains("login"));

		MvcResult mvcResult2 = mockMvc.perform(post("/uzytkownik/historia/0"))
				.andExpect(status().is3xxRedirection()).andReturn();
		String location2 = mvcResult2.getResponse().getHeader("Location");
		Assert.assertTrue(location2.contains("login"));
	}

	/**
	 * Sprawdza czy dla użytkownika z prawami innymi niż Admin historia logowań nie jest dostępna.
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username="okw",authorities={"CZLONEK_OKW"})
	public void sprawdzBezpieczenstwoDlaCzlonkaOkw() throws Exception {
		mockMvc.perform(get("/uzytkownik/historia")).andExpect(status().isForbidden());
		mockMvc.perform(get("/uzytkownik/historia/0")).andExpect(status().isForbidden());
	}

	/**
	 * Sprawdza czy dla użytkownika z prawami innymi niż Admin historia logowań nie jest dostępna.
	 * @throws Exception
	 */
	@Test
	@WithMockUser(username="pkw",authorities={"CZLONEK_PKW"})
	public void sprawdzBezpieczenstwoDlaCzlonkaPkw() throws Exception {
		mockMvc.perform(get("/uzytkownik/historia")).andExpect(status().isForbidden());
		mockMvc.perform(get("/uzytkownik/historia/0")).andExpect(status().isForbidden());
	}
}
