USE wybory;
START TRANSACTION;

#usuwamy stare tabele
SET foreign_key_checks = 0;
DROP TABLE IF EXISTS uzytkownicy;
DROP TABLE IF EXISTS poziomy_dostepu;
DROP TABLE IF EXISTS okregi;
DROP TABLE IF EXISTS wybory;
DROP TABLE IF EXISTS typy_wyborow;
DROP TABLE IF EXISTS komisje;
DROP TABLE IF EXISTS komitety;
DROP TABLE IF EXISTS kandydaci_posel;
DROP TABLE IF EXISTS kandydaci_prezydent;
DROP TABLE IF EXISTS pytania_referendalne;
DROP TABLE IF EXISTS wyniki_pytania_referendalne;
DROP TABLE IF EXISTS wyniki_prezydent;
DROP TABLE IF EXISTS wyniki_posel;
DROP TABLE IF EXISTS wyniki_parlamentarne;
DROP TABLE IF EXISTS logowania;
#todo czy jest potrzebne?: drop table if exists temp_wspolczynniki;
SET foreign_key_checks = 1;

CREATE TABLE uzytkownicy (
  id                INT AUTO_INCREMENT,
  login             VARCHAR(100) NOT NULL UNIQUE,
  haslo             CHAR(64)     NOT NULL,
  imie              VARCHAR(50)  NOT NULL,
  nazwisko          VARCHAR(50)  NOT NULL,
  poziom_dostepu_id INT(10)      NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE poziomy_dostepu (
  id    INT AUTO_INCREMENT,
  nazwa VARCHAR(50) NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE okregi (
  nr              INT AUTO_INCREMENT,
  nazwa           VARCHAR(100)  NOT NULL UNIQUE,
  wojewodztwo     VARCHAR(50)   NOT NULL,
  miasto          VARCHAR(50)   NOT NULL,
  liczba_mandatow INT DEFAULT 0 NOT NULL,
  PRIMARY KEY (nr)
);
CREATE TABLE wybory (
  id              INT AUTO_INCREMENT,
  data_utworzenia DATE    NOT NULL,
  data_glosowania DATE    NOT NULL,
  typ_wyborow_id  INT(10) NOT NULL,
  id_tworcy       INT(10) NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE typy_wyborow (
  id    INT AUTO_INCREMENT,
  nazwa VARCHAR(100) NOT NULL UNIQUE,
  PRIMARY KEY (id)
);
CREATE TABLE komisje (
  nr                  INT AUTO_INCREMENT,
  nazwa               VARCHAR(100) NOT NULL,
  adres               VARCHAR(100) NOT NULL,
  liczba_wyborcow     INT(10),
  okreg_wyborczy_nr   INT(10)      NOT NULL,
  id_przewodniczacego INT(10)      NOT NULL,
  PRIMARY KEY (nr)
);
CREATE TABLE komitety (
  nr        INT AUTO_INCREMENT,
  nazwa     VARCHAR(100) NOT NULL,
  wybory_id INT(10)      NOT NULL,
  PRIMARY KEY (nr)
);
CREATE TABLE kandydaci_posel (
  id                   INT AUTO_INCREMENT,
  imie                 VARCHAR(50)  NOT NULL,
  nazwisko             VARCHAR(50)  NOT NULL,
  plec                 CHAR(1)      NOT NULL,
  zawod                VARCHAR(100) NOT NULL,
  miejsce_zamieszkania VARCHAR(100) NOT NULL,
  nr_na_liscie         INT(10)      NOT NULL,
  partia               VARCHAR(100),
  komitet_nr           INT(10)      NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE kandydaci_prezydent (
  id                   INT AUTO_INCREMENT,
  imie                 VARCHAR(50)  NOT NULL,
  nazwisko             VARCHAR(50)  NOT NULL,
  plec                 CHAR(1)      NOT NULL,
  zawod                VARCHAR(100) NOT NULL,
  miejsce_zamieszkania VARCHAR(100) NOT NULL,
  nr_na_liscie         INT(10)      NOT NULL,
  partia               VARCHAR(100),
  wybory_id            INT(10)      NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE pytania_referendalne (
  id        INT AUTO_INCREMENT,
  pytanie   VARCHAR(300) NOT NULL,
  wybory_id INT(10)      NOT NULL,
  PRIMARY KEY (id)
);
CREATE TABLE wyniki_pytania_referendalne (
  id                      INT AUTO_INCREMENT,
  odpowiedzi_tak          INT(10) NOT NULL,
  odpowiedzi_nie          INT(10) NOT NULL,
  pytanie_referendalne_id INT(10) NOT NULL,
  komisja_nr              INT(10) NOT NULL,
  CONSTRAINT uc_wyniki_referendum UNIQUE (pytanie_referendalne_id,
                                          komisja_nr),
  PRIMARY KEY (id)
);
CREATE TABLE wyniki_prezydent (
  id                    INT AUTO_INCREMENT,
  liczba_glosow         INT(10) NOT NULL,
  kandydat_prezydent_id INT(10) NOT NULL,
  komisja_nr            INT(10) NOT NULL,
  CONSTRAINT uc_wyniki_prezydent UNIQUE (kandydat_prezydent_id,
                                         komisja_nr),
  PRIMARY KEY (id)
);
CREATE TABLE wyniki_posel (
  id                INT AUTO_INCREMENT,
  liczba_glosow     INT(10) NOT NULL,
  kandydat_posel_id INT(10) NOT NULL,
  komisja_nr        INT(10) NOT NULL,
  CONSTRAINT uc_wyniki_posel UNIQUE (kandydat_posel_id,
                                     komisja_nr),
  PRIMARY KEY (id)
);
CREATE TABLE wyniki_parlamentarne
(
  id                INT AUTO_INCREMENT,
  wybory_id         INT NOT NULL,
  okreg_wyborczy_nr INT NOT NULL,
  komitet_nr        INT NOT NULL,
  liczba_poslow     INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uc_wparlam_wok UNIQUE (wybory_id
    , okreg_wyborczy_nr
    , komitet_nr)
);
CREATE TABLE `logowania` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(40) NOT NULL,
  `data_logowania` datetime NOT NULL,
  `ua` varchar(300) DEFAULT NULL,
  `przegladarka` varchar(45) DEFAULT NULL,
  `os` varchar(45) DEFAULT NULL,
  `typ_urzadzenia` varchar(45) DEFAULT NULL,
  `przegladarka_ikona` varchar(45) DEFAULT NULL,
  `os_ikona` varchar(45) DEFAULT NULL,
  `uzytkownik_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_logowania_u` (`uzytkownik_id`)
);
/*
todo przepisać to do mysqla
#tabela pomocnicza do procedury
create global temporary table temp_wspolczynniki
(
  komitet_nr number not null,
  wspolczynnik number not null
)
on commit delete rows;
*/

#ograniczenia integralnościowe
ALTER TABLE uzytkownicy ADD CONSTRAINT fk_uzytkownicy_poziomy_dostepu FOREIGN KEY (poziom_dostepu_id) REFERENCES poziomy_dostepu (id);
ALTER TABLE wybory ADD CONSTRAINT fk_wybory_typy_wyborow FOREIGN KEY (typ_wyborow_id) REFERENCES typy_wyborow (id);
ALTER TABLE wybory ADD CONSTRAINT fk_wybory_uzytkownicy FOREIGN KEY (id_tworcy) REFERENCES uzytkownicy (id);
ALTER TABLE komisje ADD CONSTRAINT fk_komisje_okregi FOREIGN KEY (okreg_wyborczy_nr) REFERENCES okregi (nr);
ALTER TABLE komisje ADD CONSTRAINT fk_komisje_uzytkownicy FOREIGN KEY (id_przewodniczacego) REFERENCES uzytkownicy (id);
ALTER TABLE kandydaci_posel ADD CONSTRAINT fk_kandydaci_posel_komitety FOREIGN KEY (komitet_nr) REFERENCES komitety (nr)
  ON DELETE CASCADE;
ALTER TABLE pytania_referendalne ADD CONSTRAINT fk_pytania_referendalne_wybory FOREIGN KEY (wybory_id) REFERENCES wybory (id)
  ON DELETE CASCADE;
ALTER TABLE kandydaci_prezydent ADD CONSTRAINT fk_kandydaci_prezydent_wybory FOREIGN KEY (wybory_id) REFERENCES wybory (id)
  ON DELETE CASCADE;
ALTER TABLE komitety ADD CONSTRAINT fk_komitety_wybory FOREIGN KEY (wybory_id) REFERENCES wybory (id)
  ON DELETE CASCADE;
ALTER TABLE wyniki_pytania_referendalne ADD CONSTRAINT fk_wyniki_pytania_pytania FOREIGN KEY (pytanie_referendalne_id) REFERENCES pytania_referendalne (id);
ALTER TABLE wyniki_prezydent ADD CONSTRAINT fk_wyniki_prezydent_prezydent FOREIGN KEY (kandydat_prezydent_id) REFERENCES kandydaci_prezydent (id);
ALTER TABLE wyniki_posel ADD CONSTRAINT fk_wyniki_posel_posel FOREIGN KEY (kandydat_posel_id) REFERENCES kandydaci_posel (id);
ALTER TABLE wyniki_pytania_referendalne ADD CONSTRAINT fk_wyniki_pytania_komisje FOREIGN KEY (komisja_nr) REFERENCES komisje (nr);
ALTER TABLE wyniki_prezydent ADD CONSTRAINT fk_wyniki_prezydent_komisje FOREIGN KEY (komisja_nr) REFERENCES komisje (nr);
ALTER TABLE wyniki_posel ADD CONSTRAINT fk_wyniki_posel_komisje FOREIGN KEY (komisja_nr) REFERENCES komisje (nr);
ALTER TABLE wyniki_parlamentarne ADD CONSTRAINT fk_wyniki_parl_komitety FOREIGN KEY (komitet_nr) REFERENCES komitety (nr);
ALTER TABLE wyniki_parlamentarne ADD CONSTRAINT fk_wyniki_parl_okregi FOREIGN KEY (okreg_wyborczy_nr) REFERENCES okregi (nr);
ALTER TABLE wyniki_parlamentarne ADD CONSTRAINT fk_wyniki_parl_wybory FOREIGN KEY (wybory_id) REFERENCES wybory (id);
ALTER TABLE logowania ADD CONSTRAINT fk_logowania_u FOREIGN KEY (uzytkownik_id) REFERENCES uzytkownicy (id);
ALTER TABLE kandydaci_prezydent ADD CONSTRAINT uc_kprezydent_nr_na_liscie UNIQUE (wybory_id, nr_na_liscie);
ALTER TABLE kandydaci_posel ADD CONSTRAINT uc_kposel_nr_na_liscie UNIQUE (komitet_nr, nr_na_liscie);
ALTER TABLE komitety ADD CONSTRAINT uc_komitety_nazwa UNIQUE (wybory_id, nazwa);
ALTER TABLE komisje ADD CONSTRAINT uc_komisje_nazwa UNIQUE (nazwa, okreg_wyborczy_nr);

#indeksy
CREATE INDEX wybory_data_gl_idx ON wybory (data_glosowania);

#widoki
CREATE OR REPLACE VIEW frekwencja_wyborcza_referendum AS
  SELECT
    wybory.id AS wybory_id,
    round(100 * ((sum(wp.odpowiedzi_tak) + sum(wp.odpowiedzi_nie)) / ((SELECT sum(liczba_wyborcow)
                                                                       FROM komisje) * count(DISTINCT p.id))),
          2)  AS frekwencja
  FROM pytania_referendalne p
    LEFT JOIN wyniki_pytania_referendalne wp ON p.id = wp.pytanie_referendalne_id
    LEFT JOIN wybory ON p.wybory_id = wybory.id
  WHERE wp.id IS NOT NULL
  GROUP BY wybory.id;

CREATE OR REPLACE VIEW frekwencja_wyborcza_prezydent AS
  SELECT
    wybory.id                                              AS wybory_id,
    round(100 * sum(wp.liczba_glosow) / (SELECT sum(liczba_wyborcow)
                                         FROM komisje), 2) AS frekwencja
  FROM kandydaci_prezydent kp
    LEFT JOIN wyniki_prezydent wp ON kp.id = wp.kandydat_prezydent_id
    LEFT JOIN wybory ON kp.wybory_id = wybory.id
  WHERE wp.id IS NOT NULL
  GROUP BY wybory.id;

CREATE OR REPLACE VIEW frekwencja_wyborcza_posel AS
  SELECT
    wybory.id                                              AS wybory_id,
    round(100 * sum(wp.liczba_glosow) / (SELECT sum(liczba_wyborcow)
                                         FROM komisje), 2) AS frekwencja
  FROM kandydaci_posel kp
    LEFT JOIN wyniki_posel wp ON kp.id = wp.kandydat_posel_id
    LEFT JOIN komitety ON kp.komitet_nr = komitety.nr
    LEFT JOIN wybory ON komitety.wybory_id = wybory.id
  WHERE wp.id IS NOT NULL
  GROUP BY wybory.id;

CREATE OR REPLACE VIEW frekwencja_wyborcza AS
  SELECT *
  FROM frekwencja_wyborcza_posel
  UNION SELECT *
        FROM frekwencja_wyborcza_prezydent
  UNION SELECT *
        FROM frekwencja_wyborcza_referendum;

CREATE OR REPLACE VIEW suma_glosow_referendum AS
  SELECT
    w.pytanie_referendalne_id,
    p.pytanie,
    sum(w.odpowiedzi_tak) AS suma_odpowiedzi_tak,
    sum(w.odpowiedzi_nie) AS suma_odpowiedzi_nie
  FROM wyniki_pytania_referendalne w
    LEFT JOIN pytania_referendalne p ON w.pytanie_referendalne_id = p.id
  GROUP BY w.pytanie_referendalne_id, p.pytanie;

CREATE OR REPLACE VIEW suma_glosow_prezydent AS
  SELECT
    w.kandydat_prezydent_id,
    k.imie,
    k.nazwisko,
    sum(w.liczba_glosow) AS suma_liczba_glosow
  FROM wyniki_prezydent w
    LEFT JOIN kandydaci_prezydent k ON w.kandydat_prezydent_id = k.id
  GROUP BY w.kandydat_prezydent_id, k.imie, k.nazwisko;

CREATE OR REPLACE VIEW suma_glosow_posel AS
  SELECT
    w.kandydat_posel_id,
    k.imie,
    k.nazwisko,
    sum(w.liczba_glosow) AS suma_liczba_glosow
  FROM wyniki_posel w
    LEFT JOIN kandydaci_posel k ON w.kandydat_posel_id = k.id
  GROUP BY w.kandydat_posel_id, k.imie, k.nazwisko;

/*
TODO Trigger do przepisania
trigger
CREATE OR REPLACE TRIGGER TRIGGER_WYNIKI
AFTER INSERT OR DELETE OR UPDATE ON wyniki_posel
BEGIN
  FOR wybory IN (SELECT ID FROM wybory WHERE typ_wyborow_id=1) LOOP
    OBLICZ_WYNIKI_WYBOROW(wybory.ID);
  END LOOP;
END;
*/

#dodajemy startowe dane do tabel
INSERT INTO poziomy_dostepu (nazwa) VALUES ('ADMINISTRATOR');
INSERT INTO poziomy_dostepu (nazwa) VALUES ('CZLONEK_OKW');
INSERT INTO poziomy_dostepu (nazwa) VALUES ('CZLONEK_PKW');
INSERT INTO uzytkownicy (login, haslo, imie, nazwisko, poziom_dostepu_id)
VALUES ('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Jan', 'Kowalski', 1);
INSERT INTO uzytkownicy (login, haslo, imie, nazwisko, poziom_dostepu_id)
VALUES ('pkw', '3632e89dc06fcdfd486d1afa40b7f97420293aa23ad7db63eefe05a51d0ac6ef', 'Arkadiusz', 'Gorski', 3);
INSERT INTO uzytkownicy (login, haslo, imie, nazwisko, poziom_dostepu_id)
VALUES ('okw', '8dd3236cec0915e557c2daa36c5d346bcc2022edf48bbc5d992e11f3214105d7', 'Klemens', 'Dudek', 2);
INSERT INTO uzytkownicy (login, haslo, imie, nazwisko, poziom_dostepu_id)
VALUES ('okw2', '8dd3236cec0915e557c2daa36c5d346bcc2022edf48bbc5d992e11f3214105d7', 'Arkadiusz', 'Gorski', 2);
INSERT INTO uzytkownicy (login, haslo, imie, nazwisko, poziom_dostepu_id)
VALUES ('okw3', '8dd3236cec0915e557c2daa36c5d346bcc2022edf48bbc5d992e11f3214105d7', 'Ryszard', 'Sobczak', 2);
INSERT INTO uzytkownicy (login, haslo, imie, nazwisko, poziom_dostepu_id)
VALUES ('okw4', '8dd3236cec0915e557c2daa36c5d346bcc2022edf48bbc5d992e11f3214105d7', 'Klaudiusz', 'Grabowski', 2);
INSERT INTO uzytkownicy (login, haslo, imie, nazwisko, poziom_dostepu_id)
VALUES ('okw5', '8dd3236cec0915e557c2daa36c5d346bcc2022edf48bbc5d992e11f3214105d7', 'Marcin', 'Kwiatkowski', 2);
INSERT INTO typy_wyborow (nazwa) VALUES ('PARLAMENTARNE');
INSERT INTO typy_wyborow (nazwa) VALUES ('PREZYDENCKIE');
INSERT INTO typy_wyborow (nazwa) VALUES ('REFERENDUM');
#dodajemy dane przykładowych wyborów w przyszłości
INSERT INTO wybory (data_utworzenia, data_glosowania, typ_wyborow_id, id_tworcy)
VALUES ('2015-12-28', '2016-04-01', 1, 1);
INSERT INTO wybory (data_utworzenia, data_glosowania, typ_wyborow_id, id_tworcy)
VALUES ('2015-12-28', '2016-05-01', 2, 1);
INSERT INTO wybory (data_utworzenia, data_glosowania, typ_wyborow_id, id_tworcy)
VALUES ('2015-12-28', '2016-06-01', 3, 1);
INSERT INTO komitety (nazwa, wybory_id) VALUES ('Komitet Wyborczy Prawo i Sprawiedliwość', 1);
INSERT INTO komitety (nazwa, wybory_id) VALUES ('Komitet Wyborczy Platforma Obywatelska RP', 1);
INSERT INTO komitety (nazwa, wybory_id) VALUES ('Komitet Wyborczy Partia Razem', 1);
INSERT INTO komitety (nazwa, wybory_id) VALUES ('Komitet Wyborczy KORWiN', 1);
INSERT INTO kandydaci_posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, komitet_nr)
VALUES ('Adam', 'Lipiński', 'M', 'ekonomista', 'Warszawa', 1, 'Prawo i Sprawiedliwość', 1);
INSERT INTO kandydaci_posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, komitet_nr)
VALUES ('Elżbieta', 'Witek', 'K', 'nauczyciel', 'Jawor', 2, 'Prawo i Sprawiedliwość', 1);
INSERT INTO kandydaci_posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, komitet_nr)
VALUES ('Marzena', 'Machałek', 'K', 'parlamentarzysta', 'Jelenia Góra', 3, 'Prawo i Sprawiedliwość', 1);
INSERT INTO kandydaci_posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, komitet_nr) VALUES
  ('Stanisław', 'Huskowski', 'M', 'parlamentarzysta', 'Wrocław', 1, 'Platforma Obywatelska Rzeczypospolitej Polskiej',
   2);
INSERT INTO kandydaci_posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, komitet_nr)
VALUES ('Ewa', 'Drozd', 'K', 'nauczyciel', 'Głogów', 2, 'Platforma Obywatelska Rzeczypospolitej Polskiej', 2);
INSERT INTO kandydaci_posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, komitet_nr) VALUES
  ('Piotr', 'Miedziński', 'M', 'pracownik administracji samorządowej', 'Jelenia Góra', 3,
   'Platforma Obywatelska Rzeczypospolitej Polskiej', 2);
INSERT INTO kandydaci_posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, komitet_nr)
VALUES ('Szymon', 'Surmacz', 'M', 'specjalista polityki społecznej', 'Wolimierz', 1, 'Partia Razem', 3);
INSERT INTO kandydaci_posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, komitet_nr)
VALUES ('Agnieszka', 'Marcinkowska', 'K', 'technik usług kosmetycznych', 'Legnica', 2, 'Partia Razem', 3);
INSERT INTO kandydaci_posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, komitet_nr)
VALUES ('Damian', 'Cacek', 'M', 'górnik eksploatacji podziemnej', 'Lubin', 3, 'Partia Razem', 3);
INSERT INTO kandydaci_posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, komitet_nr) VALUES
  ('Klaudia', 'Witczak', 'K', 'asystent parlamentarny', 'Bolesławiec', 1,
   'KORWiN: Koalicja Odnowy Rzeczypospolitej Wolność i Nadzieja', 4);
INSERT INTO kandydaci_posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, komitet_nr)
VALUES ('Maciej', 'Gwoździewicz', 'M', 'nauczyciel akademicki - nauki techniczne', 'Jawor', 2, NULL, 4);
INSERT INTO kandydaci_posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, komitet_nr)
VALUES ('Bogusław', 'Strzelecki', 'M', 'przedsiębiorca', 'Pobiedna', 3, NULL, 4);
INSERT INTO kandydaci_prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, wybory_id)
VALUES ('Bronisław', 'Komorowski', 'M', 'nauczyciel, polityk', 'Warszawa', 1, 'Platforma Obywatelska', 2);
INSERT INTO kandydaci_prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, wybory_id)
VALUES ('Andrzej', 'Duda', 'M', 'nauczyciel akademicki', 'Kraków', 2, 'Prawo i Sprawiedliwość', 2);
INSERT INTO kandydaci_prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, wybory_id)
VALUES ('Adam', 'Jarubas', 'M', 'nauczyciel historii', 'Miedziana Góra', 3, 'Polskie Stronnictwo Ludowe', 2);
INSERT INTO kandydaci_prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, wybory_id)
VALUES ('Janusz', 'Palikot', 'M', 'przedsiębiorca, polityk', 'Lublin', 4, 'Twój Ruch', 2);
INSERT INTO kandydaci_prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, wybory_id)
VALUES
  ('Janusz', 'Korwin-Mikke', 'M', 'polityk', 'Józefów', 5, 'Koalicja Odnowy Rzeczypospolitej Wolność i Nadzieja', 2);
INSERT INTO pytania_referendalne (pytanie, wybory_id) VALUES (
  'Czy jest Pani/Pan za wprowadzeniem jednomandatowych okręgów wyborczych w wyborach do Sejmu Rzeczypospolitej Polskiej?',
  3);
INSERT INTO pytania_referendalne (pytanie, wybory_id) VALUES
  ('Czy jest Pani/Pan za utrzymaniem dotychczasowego sposobu finansowania partii politycznych z budżetu państwa?', 3);
INSERT INTO pytania_referendalne (pytanie, wybory_id) VALUES (
  'Czy jest Pani/Pan za wprowadzeniem zasady ogólnej rozstrzygania wątpliwości co do wykładni przepisów prawa podatkowego na korzyść podatnika?',
  3);
#przykładowe okręgi
INSERT INTO okregi (nazwa, wojewodztwo, miasto, liczba_mandatow)
VALUES ('Okręgowa Komisja Wyborcza nr 19', 'Małopolskie', 'Kraków', 4);
INSERT INTO okregi (nazwa, wojewodztwo, miasto, liczba_mandatow)
VALUES ('Okręgowa Komisja Wyborcza nr 21', 'Małopolskie', 'Nowy Sącz', 2);
INSERT INTO komisje (nazwa, adres, liczba_Wyborcow, okreg_wyborczy_nr, id_przewodniczacego)
VALUES ('Komisja nr 1', 'Gimnazjum Nr 3, ul. Wąska 5, Kraków', 1297, 1, 3);
INSERT INTO komisje (nazwa, adres, liczba_Wyborcow, okreg_wyborczy_nr, id_przewodniczacego)
VALUES ('Komisja nr 2', 'Szkoła Podstawowa Nr 11, ul. Miodowa 36, Kraków', 1149, 1, 4);
INSERT INTO komisje (nazwa, adres, liczba_Wyborcow, okreg_wyborczy_nr, id_przewodniczacego)
VALUES ('Komisja nr 3', 'Zespół Szkół Mechanicznych Nr 4, ul. Podbrzezie 10, Kraków', 773, 1, 5);
INSERT INTO komisje (nazwa, adres, liczba_Wyborcow, okreg_wyborczy_nr, id_przewodniczacego)
VALUES ('Komisja nr 1', 'Zespół Szkół Ogólnokształcących (sala nr I), Dobra', 1157, 2, 6);
INSERT INTO komisje (nazwa, adres, liczba_Wyborcow, okreg_wyborczy_nr, id_przewodniczacego)
VALUES ('Komisja nr 2', 'Zespół Szkół Ogólnokształcących (sala nr II), Dobra', 1357, 2, 7);
#przykładowe archiwialne wybory prezydenckie z wynikami
INSERT INTO wybory (data_utworzenia, data_glosowania, typ_wyborow_id, id_tworcy)
VALUES ('2010-05-01', '2010-07-04', 2, 2);
INSERT INTO kandydaci_prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, wybory_id)
VALUES ('Bronisław', 'Komorowski', 'M', 'nauczyciel, polityk', 'Warszawa', 1, 'Platforma Obywatelska', 4);
INSERT INTO kandydaci_prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, wybory_id)
VALUES ('Jarosław', 'Kaczyński', 'M', 'polityk, adwokat', 'Warszawa', 2, 'Prawo i Sprawiedliwość', 4);
INSERT INTO wyniki_prezydent (liczba_Glosow, kandydat_prezydent_id, komisja_nr) VALUES (551, 6, 1);
INSERT INTO wyniki_prezydent (liczba_Glosow, kandydat_prezydent_id, komisja_nr) VALUES (255, 7, 1);
INSERT INTO wyniki_prezydent (liczba_Glosow, kandydat_prezydent_id, komisja_nr) VALUES (394, 6, 2);
INSERT INTO wyniki_prezydent (liczba_Glosow, kandydat_prezydent_id, komisja_nr) VALUES (261, 7, 2);
INSERT INTO wyniki_prezydent (liczba_Glosow, kandydat_prezydent_id, komisja_nr) VALUES (295, 6, 3);
INSERT INTO wyniki_prezydent (liczba_Glosow, kandydat_prezydent_id, komisja_nr) VALUES (170, 7, 3);
INSERT INTO wyniki_prezydent (liczba_Glosow, kandydat_prezydent_id, komisja_nr) VALUES (224, 6, 4);
INSERT INTO wyniki_prezydent (liczba_Glosow, kandydat_prezydent_id, komisja_nr) VALUES (408, 7, 4);
INSERT INTO wyniki_prezydent (liczba_Glosow, kandydat_prezydent_id, komisja_nr) VALUES (261, 6, 5);
INSERT INTO wyniki_prezydent (liczba_Glosow, kandydat_prezydent_id, komisja_nr) VALUES (483, 7, 5);
#przykładowe archiwialne referendum z wynikami
INSERT INTO wybory (data_utworzenia, data_glosowania, typ_wyborow_id, id_tworcy)
VALUES ('2003-01-12', '2003-06-07', 3, 2);
INSERT INTO pytania_referendalne (pytanie, wybory_id)
VALUES ('Czy wyraża Pani / Pan zgodę na przystąpienie Rzeczypospolitej Polskiej do Unii Europejskiej?', 5);
INSERT INTO wyniki_pytania_referendalne (odpowiedzi_tak, odpowiedzi_nie, pytanie_referendalne_id, komisja_nr)
VALUES (632, 421, 4, 1);
INSERT INTO wyniki_pytania_referendalne (odpowiedzi_tak, odpowiedzi_nie, pytanie_referendalne_id, komisja_nr)
VALUES (532, 431, 4, 2);
INSERT INTO wyniki_pytania_referendalne (odpowiedzi_tak, odpowiedzi_nie, pytanie_referendalne_id, komisja_nr)
VALUES (300, 221, 4, 3);
INSERT INTO wyniki_pytania_referendalne (odpowiedzi_tak, odpowiedzi_nie, pytanie_referendalne_id, komisja_nr)
VALUES (476, 123, 4, 4);
INSERT INTO wyniki_pytania_referendalne (odpowiedzi_tak, odpowiedzi_nie, pytanie_referendalne_id, komisja_nr)
VALUES (754, 321, 4, 5);
#przykładowe wyniki z wyborów parlamentarnych
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (134, 1, 1);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (44, 2, 1);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (27, 4, 1);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (16, 5, 1);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (81, 7, 1);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (112, 8, 1);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (11, 10, 1);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (7, 11, 1);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (33, 1, 4);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (8, 2, 4);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (67, 4, 4);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (78, 5, 4);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (72, 7, 4);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (92, 8, 4);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (6, 10, 4);
INSERT INTO wyniki_posel (liczba_glosow, kandydat_posel_id, komisja_nr) VALUES (19, 11, 4);

COMMIT;