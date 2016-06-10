package pkw.controllers;

/**
 * Created by Elleander on 30/05/2016.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pkw.PDFGenerator;
import pkw.models.*;
import pkw.repositories.*;

import java.io.IOException;

@Controller
@RequestMapping("/drukuj")
public class DrukujController {
    @Autowired
    private WyboryRepository wyboryRepository;
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> Drukuj(@RequestParam(value = "id") int id) throws IOException {
        boolean isPDFempty = true;
        PDFGenerator protokol= new PDFGenerator();
        Wybory wybory = wyboryRepository.findOne(id);
        switch(wybory.getTypWyborowId())
        {
        case 1: //Parlamentarne?
        {
        for(Komitet komitet : wybory.getKomitety())
        {
        for(KandydatPosel posel : komitet.getKandydaciPosel())
        {
        isPDFempty = false;
        protokol.addPage();
        protokol.addLine("Imię: "+posel.getImie());
        protokol.addLine("Nazwisko: "+posel.getNazwisko());
        protokol.addLine("Płeć: "+posel.getPlec());
        protokol.addLine("Miejsce zamieszkania: "+posel.getMiejsceZamieszkania());
        protokol.addLine("Partia: "+posel.getPartia());
        protokol.addLine("Zawód: "+posel.getZawod());
        protokol.addLine("");
        for(WynikiPosel wynik : posel.getWyniki())
        {
        protokol.addLine("Czas wprowadzenia: "+wynik.getCzasWprowadzenia());
        protokol.addLine("Liczba głosów: "+wynik.getLiczbaGlosow());
        protokol.addLine("");
        }
        }
        }
        break;
        }
        case 2: //Prezydenckie?
        {
        for(KandydatPrezydent kandydat : wybory.getKandydaciPrezydent())
        {
        isPDFempty = false;
        protokol.addPage();
        protokol.addLine("Imię: "+kandydat.getImie());
        protokol.addLine("Nazwisko: "+kandydat.getNazwisko());
        protokol.addLine("Płeć: "+kandydat.getPlec());
        protokol.addLine("Miejsce zamieszkania: "+kandydat.getMiejsceZamieszkania());
        protokol.addLine("Partia: "+kandydat.getPartia());
        protokol.addLine("Zawód: "+kandydat.getZawod());
        protokol.addLine("");
        for(WynikiPrezydent wynik : kandydat.getWyniki())
        {
        protokol.addLine("Czas wprowadzenia: "+wynik.getCzasWprowadzenia());
        protokol.addLine("Liczba głosów: "+wynik.getLiczbaGlosow());
        protokol.addLine("");
        }
        }
        break;
        }
        case 3: //Referendum?
        {
        for(PytanieReferendalne pytanie : wybory.getPytaniaReferendalne())
        {
        isPDFempty = false;
        protokol.addPage();
        protokol.addLine("Pytanie: "+pytanie.getPytanie());
        protokol.addLine("");
        for(WynikiPytaniaReferendalne odpowiedz : pytanie.getWyniki())
        {
        protokol.addLine("Odpowiedzi na tak: "+odpowiedz.getOdpowiedziTak());
        protokol.addLine("Odpowiedzi na nie: "+odpowiedz.getOdpowiedziNie());
        protokol.addLine("");
        }
        }
        break;
        }
        default:
        {
        break;
        }
        }
        if(isPDFempty)
        {
            protokol.addPage();
            protokol.addLine("Brak danych dla wyborów o id "+id);
        }
        byte[] ByteArray = protokol.returnByteArray();
            return ResponseEntity.ok()
                    .contentLength(ByteArray.length)
                    .contentType(MediaType.parseMediaType("application/pdf"))
                    .body(ByteArray);
    }
}