package pkw;

import org.junit.*;
import pkw.models.KandydatPrezydent;
import pkw.models.Komisja;
import pkw.models.WynikiPrezydent;

import java.util.LinkedList;

public class KandydatPrezydentTests {
    KandydatPrezydent prezydent;

    @Before
    public void setup() {
        prezydent = new KandydatPrezydent();
        prezydent.setWyniki(new LinkedList<>());
    }

    @Test
    public void znajdzWynikiDlaKomisjiPuste() {
        Komisja komisja1 = new Komisja();

        WynikiPrezydent wynikiDlaKomisji = prezydent.getWynikiDlaKomisji(komisja1);

        Assert.assertNull(wynikiDlaKomisji);
    }

    @Test
    public void znajdzWynikiDlaKomisji() {
        Komisja komisja1 = new Komisja();
        Komisja komisja2 = new Komisja();
        WynikiPrezydent wyniki1 = new WynikiPrezydent();
        wyniki1.setKomisja(komisja1);
        prezydent.getWyniki().add(wyniki1);
        WynikiPrezydent wyniki2 = new WynikiPrezydent();
        wyniki2.setKomisja(komisja2);
        prezydent.getWyniki().add(wyniki2);

        WynikiPrezydent wynikiDlaKomisji = prezydent.getWynikiDlaKomisji(komisja1);

        Assert.assertNotNull(wynikiDlaKomisji);
        Assert.assertEquals(komisja1, wynikiDlaKomisji.getKomisja());
    }

    @Test
    public void znajdzWynikiLaczne() {
        WynikiPrezydent wyniki1 = new WynikiPrezydent();
        wyniki1.setKandydatPrezydent(prezydent);
        wyniki1.setLiczbaGlosow(50);
        prezydent.getWyniki().add(wyniki1);
        WynikiPrezydent wyniki2 = new WynikiPrezydent();
        wyniki2.setKandydatPrezydent(prezydent);
        wyniki2.setLiczbaGlosow(100);
        prezydent.getWyniki().add(wyniki2);

        WynikiPrezydent wynikiLaczny = prezydent.getWynikLaczny();

        Assert.assertNotNull(wynikiLaczny);
        Assert.assertEquals(150, wynikiLaczny.getLiczbaGlosow());
        Assert.assertEquals(prezydent, wynikiLaczny.getKandydatPrezydent());
    }
}
