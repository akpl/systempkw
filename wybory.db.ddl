--remove old tables
drop table UZYTKOWNICY cascade constraints;
drop table POZIOMY_DOSTEPU cascade constraints;
drop table OKREGI cascade constraints;
drop table WYBORY cascade constraints;
drop table TYPY_WYBOROW cascade constraints;
drop table KOMISJE cascade constraints;
drop table KOMITETY cascade constraints;
drop table KANDYDACI_POSEL cascade constraints;
drop table KANDYDACI_PREZYDENT cascade constraints;
drop table PYTANIA_REFERENDALNE cascade constraints;
drop table WYNIKI_PYTANIA_REFERENDALNE cascade constraints;
drop table WYNIKI_PREZYDENT cascade constraints;
drop table WYNIKI_POSEL cascade constraints;
drop table Wyniki_Parlamentarne CASCADE CONSTRAINTS;
drop table Temp_Wspolczynniki CASCADE CONSTRAINTS;
DROP SEQUENCE uzytkownicy_seq;
DROP SEQUENCE poziomy_dostepu_seq;
DROP SEQUENCE okregi_seq;
DROP SEQUENCE wybory_seq;
DROP SEQUENCE typy_wyborow_seq;
DROP SEQUENCE komisje_seq;
DROP SEQUENCE komitety_seq;
DROP SEQUENCE kandydaci_posel_seq;
DROP SEQUENCE kandydaci_prezydent_seq;
DROP SEQUENCE pytania_referendalne_seq;
DROP SEQUENCE wyniki_pytania_seq;
DROP SEQUENCE wyniki_prezydent_seq;
DROP SEQUENCE wyniki_posel_seq;

CREATE SEQUENCE uzytkownicy_seq;
CREATE SEQUENCE poziomy_dostepu_seq;
CREATE SEQUENCE okregi_seq;
CREATE SEQUENCE wybory_seq;
CREATE SEQUENCE typy_wyborow_seq;
CREATE SEQUENCE komisje_seq;
CREATE SEQUENCE komitety_seq;
CREATE SEQUENCE kandydaci_posel_seq;
CREATE SEQUENCE kandydaci_prezydent_seq;
CREATE SEQUENCE pytania_referendalne_seq;
CREATE SEQUENCE wyniki_pytania_seq;
CREATE SEQUENCE wyniki_prezydent_seq;
CREATE SEQUENCE wyniki_posel_seq;

CREATE TABLE Uzytkownicy (
  id              NUMBER DEFAULT uzytkownicy_seq.nextval,
  login           varchar2(100) NOT NULL UNIQUE, 
  haslo           char(64) NOT NULL,
  imie            varchar2(50) NOT NULL, 
  nazwisko        varchar2(50) NOT NULL, 
  Poziom_Dostepu_id number(10) NOT NULL,
  PRIMARY KEY (id));
CREATE TABLE Poziomy_Dostepu (
  id    NUMBER DEFAULT poziomy_dostepu_seq.nextval,
  nazwa varchar2(50) NOT NULL,
  PRIMARY KEY (id));
CREATE TABLE Okregi (
  nr              NUMBER DEFAULT okregi_seq.nextval,
  nazwa           varchar2(100) NOT NULL UNIQUE,
  wojewodztwo     varchar2(50) NOT NULL,
  miasto          varchar2(50) NOT NULL,
  liczba_mandatow number default 0 NOT NULL,
  PRIMARY KEY (nr));
CREATE TABLE Wybory (
  id              NUMBER DEFAULT wybory_seq.nextval,
  data_utworzenia date       NOT NULL,
  data_glosowania date       NOT NULL,
  Typ_Wyborow_id  number(10) NOT NULL,
  id_Tworcy       number(10) NOT NULL,
  PRIMARY KEY (id));
CREATE TABLE Typy_Wyborow (
  id    NUMBER DEFAULT typy_wyborow_seq.nextval,
  nazwa varchar2(100) NOT NULL UNIQUE, 
  PRIMARY KEY (id));
CREATE TABLE Komisje (
  nr                  NUMBER DEFAULT komisje_seq.nextval,
  nazwa               varchar2(100) NOT NULL,
  adres               varchar2(100) NOT NULL,
  liczba_Wyborcow     number(10),
  Okreg_Wyborczy_nr   number(10)    NOT NULL,
  id_Przewodniczacego number(10)    NOT NULL,
  PRIMARY KEY (nr));
CREATE TABLE Komitety (
  nr        NUMBER DEFAULT komitety_seq.nextval,
  nazwa     varchar2(100) NOT NULL,
  Wybory_id number(10)    NOT NULL,
  PRIMARY KEY (nr));
CREATE TABLE Kandydaci_Posel (
  id                   NUMBER DEFAULT kandydaci_posel_seq.nextval,
  imie                 varchar2(50)  NOT NULL,
  nazwisko             varchar2(50)  NOT NULL,
  plec                 char(1)       NOT NULL,
  zawod                varchar2(100) NOT NULL,
  miejsce_zamieszkania varchar2(100) NOT NULL,
  nr_na_liscie         number(10)    NOT NULL,
  partia               varchar2(100),
  Komitet_nr           number(10)    NOT NULL,
  PRIMARY KEY (id));
CREATE TABLE Kandydaci_Prezydent (
  id                   NUMBER DEFAULT kandydaci_prezydent_seq.nextval,
  imie                 varchar2(50)  NOT NULL,
  nazwisko             varchar2(50)  NOT NULL,
  plec                 char(1)       NOT NULL,
  zawod                varchar2(100) NOT NULL,
  miejsce_zamieszkania varchar2(100) NOT NULL,
  nr_na_liscie         number(10)    NOT NULL,
  partia               varchar2(100),
  Wybory_id            number(10)    NOT NULL,
  PRIMARY KEY (id));
CREATE TABLE Pytania_Referendalne (
  id        NUMBER DEFAULT pytania_referendalne_seq.nextval,
  pytanie   varchar2(300) NOT NULL,
  Wybory_id number(10)    NOT NULL,
  PRIMARY KEY (id));
CREATE TABLE Wyniki_Pytania_Referendalne (
  id        NUMBER DEFAULT wyniki_pytania_seq.nextval,
  odpowiedzi_tak          number(10) NOT NULL,
  odpowiedzi_nie          number(10) NOT NULL,
  Pytanie_Referendalne_id number(10) NOT NULL,
  Komisja_nr              number(10) NOT NULL,
  CONSTRAINT uc_wyniki_referendum UNIQUE (Pytanie_Referendalne_id,
               Komisja_nr),
  PRIMARY KEY (id));
CREATE TABLE Wyniki_Prezydent (
  id        NUMBER DEFAULT wyniki_prezydent_seq.nextval,
  liczba_Glosow         number(10) NOT NULL,
  Kandydat_Prezydent_id number(10) NOT NULL,
  Komisja_nr            number(10) NOT NULL,
  CONSTRAINT uc_wyniki_prezydent UNIQUE (Kandydat_Prezydent_id,
               Komisja_nr),
  PRIMARY KEY (id));
CREATE TABLE Wyniki_Posel (
  id        NUMBER DEFAULT wyniki_posel_seq.nextval,
  liczba_Glosow     number(10) NOT NULL,
  Kandydat_Posel_id number(10) NOT NULL,
  Komisja_nr        number(10) NOT NULL,
  CONSTRAINT uc_wyniki_posel UNIQUE (Kandydat_Posel_id,
               Komisja_nr),
  PRIMARY KEY (id));
CREATE TABLE Wyniki_Parlamentarne
(
  wybory_id NUMBER NOT NULL,
  okreg_wyborczy_nr NUMBER NOT NULL,
  komitet_nr NUMBER NOT NULL,
  liczba_poslow NUMBER NOT NULL,
  PRIMARY KEY (wybory_id
             , okreg_wyborczy_nr
             , komitet_nr));

CREATE GLOBAL TEMPORARY TABLE Temp_Wspolczynniki
(
  komitet_nr NUMBER NOT NULL,
  wspolczynnik NUMBER NOT NULL
)
ON COMMIT DELETE ROWS;

ALTER TABLE Uzytkownicy ADD CONSTRAINT fk_uzytkownicy_poziomy_dostepu FOREIGN KEY (Poziom_Dostepu_id) REFERENCES Poziomy_Dostepu (id);
ALTER TABLE Wybory ADD CONSTRAINT fk_wybory_typy_wyborow FOREIGN KEY (Typ_Wyborow_id) REFERENCES Typy_Wyborow (id);
ALTER TABLE Wybory ADD CONSTRAINT fk_wybory_uzytkownicy FOREIGN KEY (id_Tworcy) REFERENCES Uzytkownicy (id);
ALTER TABLE Komisje ADD CONSTRAINT fk_komisje_okregi FOREIGN KEY (Okreg_Wyborczy_nr) REFERENCES Okregi (nr);
ALTER TABLE Komisje ADD CONSTRAINT fk_komisje_uzytkownicy FOREIGN KEY (id_Przewodniczacego) REFERENCES Uzytkownicy (id);
ALTER TABLE Kandydaci_Posel ADD CONSTRAINT fk_kandydaci_posel_komitety FOREIGN KEY (Komitet_nr) REFERENCES Komitety (nr) ON DELETE CASCADE;
ALTER TABLE Pytania_Referendalne ADD CONSTRAINT fk_pytania_referendalne_wybory FOREIGN KEY (Wybory_id) REFERENCES Wybory (id) ON DELETE CASCADE;
ALTER TABLE Kandydaci_Prezydent ADD CONSTRAINT fk_kandydaci_prezydent_wybory FOREIGN KEY (Wybory_id) REFERENCES Wybory (id) ON DELETE CASCADE;
ALTER TABLE Komitety ADD CONSTRAINT fk_komitety_wybory FOREIGN KEY (Wybory_id) REFERENCES Wybory (id) ON DELETE CASCADE;
ALTER TABLE Wyniki_Pytania_Referendalne ADD CONSTRAINT fk_wyniki_pytania_pytania FOREIGN KEY (Pytanie_Referendalne_id) REFERENCES Pytania_Referendalne (id);
ALTER TABLE Wyniki_Prezydent ADD CONSTRAINT fk_wyniki_prezydent_prezydent FOREIGN KEY (Kandydat_Prezydent_id) REFERENCES Kandydaci_Prezydent (id);
ALTER TABLE Wyniki_Posel ADD CONSTRAINT fk_wyniki_posel_posel FOREIGN KEY (Kandydat_Posel_id) REFERENCES Kandydaci_Posel (id);
ALTER TABLE Wyniki_Pytania_Referendalne ADD CONSTRAINT fk_wyniki_pytania_komisje FOREIGN KEY (Komisja_nr) REFERENCES Komisje (nr);
ALTER TABLE Wyniki_Prezydent ADD CONSTRAINT fk_wyniki_prezydent_komisje FOREIGN KEY (Komisja_nr) REFERENCES Komisje (nr);
ALTER TABLE Wyniki_Posel ADD CONSTRAINT fk_wyniki_posel_komisje FOREIGN KEY (Komisja_nr) REFERENCES Komisje (nr);
ALTER TABLE Wyniki_Parlamentarne ADD CONSTRAINT fk_wyniki_parl_komitety FOREIGN KEY (komitet_nr) REFERENCES KOMITETY (nr);
ALTER TABLE Wyniki_Parlamentarne ADD CONSTRAINT fk_wyniki_parl_okregi FOREIGN KEY (okreg_wyborczy_nr) REFERENCES OKREGI (nr);
ALTER TABLE Wyniki_Parlamentarne ADD CONSTRAINT fk_wyniki_parl_wybory FOREIGN KEY (wybory_id) REFERENCES WYBORY (id);
ALTER TABLE Kandydaci_Prezydent ADD CONSTRAINT uc_kprezydent_nr_na_liscie UNIQUE (nr_na_liscie, Wybory_id);
ALTER TABLE Kandydaci_Posel ADD CONSTRAINT uc_kposel_nr_na_liscie UNIQUE (nr_na_liscie, Komitet_nr);
ALTER TABLE Komitety ADD CONSTRAINT uc_komitety_nazwa UNIQUE (nazwa, Wybory_id);
ALTER TABLE Komisje ADD CONSTRAINT uc_komisje_nazwa UNIQUE (nazwa, Okreg_Wyborczy_nr);

--add data
INSERT INTO Poziomy_Dostepu (nazwa) VALUES ('ADMINISTRATOR');
INSERT INTO Poziomy_Dostepu (nazwa) VALUES ('CZLONEK_OKW');
INSERT INTO Poziomy_Dostepu (nazwa) VALUES ('CZLONEK_PKW');
INSERT INTO Uzytkownicy (login, haslo, imie, nazwisko, Poziom_Dostepu_id) VALUES ('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Jan', 'Kowalski', 1);
INSERT INTO Uzytkownicy (login, haslo, imie, nazwisko, Poziom_Dostepu_id) VALUES ('pkw', '3632e89dc06fcdfd486d1afa40b7f97420293aa23ad7db63eefe05a51d0ac6ef', 'Arkadiusz', 'Gorski', 3);
INSERT INTO Uzytkownicy (login, haslo, imie, nazwisko, Poziom_Dostepu_id) VALUES ('okw', '8dd3236cec0915e557c2daa36c5d346bcc2022edf48bbc5d992e11f3214105d7', 'Klemens', 'Dudek', 2);
INSERT INTO Uzytkownicy (login, haslo, imie, nazwisko, Poziom_Dostepu_id) VALUES ('okw2', '3632e89dc06fcdfd486d1afa40b7f97420293aa23ad7db63eefe05a51d0ac6ef', 'Arkadiusz', 'Gorski', 2);
INSERT INTO Uzytkownicy (login, haslo, imie, nazwisko, Poziom_Dostepu_id) VALUES ('okw3', '3632e89dc06fcdfd486d1afa40b7f97420293aa23ad7db63eefe05a51d0ac6ef', 'Arkadiusz', 'Gorski', 2);
INSERT INTO Uzytkownicy (login, haslo, imie, nazwisko, Poziom_Dostepu_id) VALUES ('okw4', '3632e89dc06fcdfd486d1afa40b7f97420293aa23ad7db63eefe05a51d0ac6ef', 'Arkadiusz', 'Gorski', 2);
INSERT INTO Uzytkownicy (login, haslo, imie, nazwisko, Poziom_Dostepu_id) VALUES ('okw5', '3632e89dc06fcdfd486d1afa40b7f97420293aa23ad7db63eefe05a51d0ac6ef', 'Arkadiusz', 'Gorski', 2);

INSERT INTO Typy_Wyborow (nazwa) VALUES ('PARLAMENTARNE');
INSERT INTO Typy_Wyborow (nazwa) VALUES ('PREZYDENCKIE');
INSERT INTO Typy_Wyborow (nazwa) VALUES ('REFERENDUM');
--add sample data
INSERT INTO Wybory (data_utworzenia, data_glosowania, Typ_Wyborow_id, id_Tworcy) VALUES ('2015-12-28', '2016-04-01', 1, 1);
INSERT INTO Wybory (data_utworzenia, data_glosowania, Typ_Wyborow_id, id_Tworcy) VALUES ('2015-12-28', '2016-05-01', 2, 1);
INSERT INTO Wybory (data_utworzenia, data_glosowania, Typ_Wyborow_id, id_Tworcy) VALUES ('2015-12-28', '2016-06-01', 3, 1);
INSERT INTO Komitety (nazwa, Wybory_id) VALUES ('Komitet Wyborczy Prawo i Sprawiedliwość', 1);
INSERT INTO Komitety (nazwa, Wybory_id) VALUES ('Komitet Wyborczy Platforma Obywatelska RP', 1);
INSERT INTO Komitety (nazwa, Wybory_id) VALUES ('Komitet Wyborczy Partia Razem', 1);
INSERT INTO Komitety (nazwa, Wybory_id) VALUES ('Komitet Wyborczy KORWiN', 1);
INSERT INTO Kandydaci_Posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Komitet_nr) VALUES ('Adam', 'Lipiński', 'M', 'ekonomista', 'Warszawa', 1, 'Prawo i Sprawiedliwość', 1);
INSERT INTO Kandydaci_Posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Komitet_nr) VALUES ('Elżbieta', 'Witek', 'K', 'nauczyciel', 'Jawor', 2, 'Prawo i Sprawiedliwość', 1);
INSERT INTO Kandydaci_Posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Komitet_nr) VALUES ('Marzena', 'Machałek', 'K', 'parlamentarzysta', 'Jelenia Góra', 3, 'Prawo i Sprawiedliwość', 1);
INSERT INTO Kandydaci_Posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Komitet_nr) VALUES ('Stanisław', 'Huskowski', 'M', 'parlamentarzysta', 'Wrocław', 1, 'Platforma Obywatelska Rzeczypospolitej Polskiej', 2);
INSERT INTO Kandydaci_Posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Komitet_nr) VALUES ('Ewa', 'Drozd', 'K', 'nauczyciel', 'Głogów', 2, 'Platforma Obywatelska Rzeczypospolitej Polskiej', 2);
INSERT INTO Kandydaci_Posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Komitet_nr) VALUES ('Piotr', 'Miedziński', 'M', 'pracownik administracji samorządowej', 'Jelenia Góra', 3, 'Platforma Obywatelska Rzeczypospolitej Polskiej', 2);
INSERT INTO Kandydaci_Posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Komitet_nr) VALUES ('Szymon', 'Surmacz', 'M', 'specjalista polityki społecznej', 'Wolimierz', 1, 'Partia Razem', 3);
INSERT INTO Kandydaci_Posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Komitet_nr) VALUES ('Agnieszka', 'Marcinkowska', 'K', 'technik usług kosmetycznych', 'Legnica', 2, 'Partia Razem', 3);
INSERT INTO Kandydaci_Posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Komitet_nr) VALUES ('Damian', 'Cacek', 'M', 'górnik eksploatacji podziemnej', 'Lubin', 3, 'Partia Razem', 3);
INSERT INTO Kandydaci_Posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Komitet_nr) VALUES ('Klaudia', 'Witczak', 'K', 'asystent parlamentarny', 'Bolesławiec', 1, 'KORWiN: Koalicja Odnowy Rzeczypospolitej Wolność i Nadzieja', 4);
INSERT INTO Kandydaci_Posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Komitet_nr) VALUES ('Maciej', 'Gwoździewicz', 'M', 'nauczyciel akademicki - nauki techniczne', 'Jawor', 2, null, 4);
INSERT INTO Kandydaci_Posel (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Komitet_nr) VALUES ('Bogusław', 'Strzelecki', 'M', 'przedsiębiorca', 'Pobiedna', 3, null, 4);
INSERT INTO Kandydaci_Prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Wybory_id) VALUES ('Bronisław', 'Komorowski', 'M', 'nauczyciel, polityk', 'Warszawa', 1, 'Platforma Obywatelska', 2);
INSERT INTO Kandydaci_Prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Wybory_id) VALUES ('Andrzej', 'Duda', 'M', 'nauczyciel akademicki', 'Kraków', 2, 'Prawo i Sprawiedliwość', 2);
INSERT INTO Kandydaci_Prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Wybory_id) VALUES ('Adam', 'Jarubas', 'M', 'nauczyciel historii', 'Miedziana Góra', 3, 'Polskie Stronnictwo Ludowe', 2);
INSERT INTO Kandydaci_Prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Wybory_id) VALUES ('Janusz', 'Palikot', 'M', 'przedsiębiorca, polityk', 'Lublin', 4, 'Twój Ruch', 2);
INSERT INTO Kandydaci_Prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Wybory_id) VALUES ('Janusz', 'Korwin-Mikke', 'M', 'polityk', 'Józefów', 5, 'Koalicja Odnowy Rzeczypospolitej Wolność i Nadzieja', 2);
INSERT INTO Pytania_Referendalne (pytanie, Wybory_id) VALUES ('Czy jest Pani/Pan za wprowadzeniem jednomandatowych okręgów wyborczych w wyborach do Sejmu Rzeczypospolitej Polskiej?', 3);
INSERT INTO Pytania_Referendalne (pytanie, Wybory_id) VALUES ('Czy jest Pani/Pan za utrzymaniem dotychczasowego sposobu finansowania partii politycznych z budżetu państwa?', 3);
INSERT INTO Pytania_Referendalne (pytanie, Wybory_id) VALUES ('Czy jest Pani/Pan za wprowadzeniem zasady ogólnej rozstrzygania wątpliwości co do wykładni przepisów prawa podatkowego na korzyść podatnika?', 3);
--przykładowe wybory prezydenckie z wynikami
INSERT INTO Okregi (nazwa, wojewodztwo, miasto, liczba_mandatow) VALUES ('Okręgowa Komisja Wyborcza nr 19', 'Małopolskie', 'Kraków', 4);
INSERT INTO Okregi (nazwa, wojewodztwo, miasto, liczba_mandatow) VALUES ('Okręgowa Komisja Wyborcza nr 21', 'Małopolskie', 'Nowy Sącz', 2);
INSERT INTO Komisje (nazwa, adres, liczba_Wyborcow, Okreg_Wyborczy_nr, id_Przewodniczacego) VALUES ('Komisja nr 1', 'Gimnazjum Nr 3, ul. Wąska 5, Kraków', 1297, 1, 3);
INSERT INTO Komisje (nazwa, adres, liczba_Wyborcow, Okreg_Wyborczy_nr, id_Przewodniczacego) VALUES ('Komisja nr 2', 'Szkoła Podstawowa Nr 11, ul. Miodowa 36, Kraków', 1149, 1, 4);
INSERT INTO Komisje (nazwa, adres, liczba_Wyborcow, Okreg_Wyborczy_nr, id_Przewodniczacego) VALUES ('Komisja nr 3', 'Zespół Szkół Mechanicznych Nr 4, ul. Podbrzezie 10, Kraków', 773, 1, 5);
INSERT INTO Komisje (nazwa, adres, liczba_Wyborcow, Okreg_Wyborczy_nr, id_Przewodniczacego) VALUES ('Komisja nr 1', 'Zespół Szkół Ogólnokształcących (sala nr I), Dobra', 1157, 2, 6);
INSERT INTO Komisje (nazwa, adres, liczba_Wyborcow, Okreg_Wyborczy_nr, id_Przewodniczacego) VALUES ('Komisja nr 2', 'Zespół Szkół Ogólnokształcących (sala nr II), Dobra', 1357, 2, 7);
INSERT INTO Wybory (data_utworzenia, data_glosowania, Typ_Wyborow_id, id_Tworcy) VALUES ('2010-05-01', '2010-07-04', 2, 2);
INSERT INTO Kandydaci_Prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Wybory_id) VALUES ('Bronisław', 'Komorowski', 'M', 'nauczyciel, polityk', 'Warszawa', 1, 'Platforma Obywatelska', 4);
INSERT INTO Kandydaci_Prezydent (imie, nazwisko, plec, zawod, miejsce_zamieszkania, nr_na_liscie, partia, Wybory_id) VALUES ('Jarosław', 'Kaczyński', 'M', 'polityk, adwokat', 'Warszawa', 2, 'Prawo i Sprawiedliwość', 4);
INSERT INTO Wyniki_Prezydent (liczba_Glosow, Kandydat_Prezydent_id, Komisja_nr) VALUES (551, 6, 1);
INSERT INTO Wyniki_Prezydent (liczba_Glosow, Kandydat_Prezydent_id, Komisja_nr) VALUES (255, 7, 1);
INSERT INTO Wyniki_Prezydent (liczba_Glosow, Kandydat_Prezydent_id, Komisja_nr) VALUES (394, 6, 2);
INSERT INTO Wyniki_Prezydent (liczba_Glosow, Kandydat_Prezydent_id, Komisja_nr) VALUES (261, 7, 2);
INSERT INTO Wyniki_Prezydent (liczba_Glosow, Kandydat_Prezydent_id, Komisja_nr) VALUES (295, 6, 3);
INSERT INTO Wyniki_Prezydent (liczba_Glosow, Kandydat_Prezydent_id, Komisja_nr) VALUES (170, 7, 3);
INSERT INTO Wyniki_Prezydent (liczba_Glosow, Kandydat_Prezydent_id, Komisja_nr) VALUES (224, 6, 4);
INSERT INTO Wyniki_Prezydent (liczba_Glosow, Kandydat_Prezydent_id, Komisja_nr) VALUES (408, 7, 4);
INSERT INTO Wyniki_Prezydent (liczba_Glosow, Kandydat_Prezydent_id, Komisja_nr) VALUES (261, 6, 5);
INSERT INTO Wyniki_Prezydent (liczba_Glosow, Kandydat_Prezydent_id, Komisja_nr) VALUES (483, 7, 5);
--przykładowe referendum z wynikami
INSERT INTO Wybory (data_utworzenia, data_glosowania, Typ_Wyborow_id, id_Tworcy) VALUES ('2003-01-12', '2003-06-07', 3, 2);
INSERT INTO Pytania_Referendalne (pytanie, Wybory_id) VALUES ('Czy wyraża Pani / Pan zgodę na przystąpienie Rzeczypospolitej Polskiej do Unii Europejskiej?', 5);
INSERT INTO Wyniki_Pytania_Referendalne (odpowiedzi_tak, odpowiedzi_nie, Pytanie_Referendalne_id, Komisja_nr) VALUES (632, 421, 4, 1);
INSERT INTO Wyniki_Pytania_Referendalne (odpowiedzi_tak, odpowiedzi_nie, Pytanie_Referendalne_id, Komisja_nr) VALUES (532, 431, 4, 2);
INSERT INTO Wyniki_Pytania_Referendalne (odpowiedzi_tak, odpowiedzi_nie, Pytanie_Referendalne_id, Komisja_nr) VALUES (300, 221, 4, 3);
INSERT INTO Wyniki_Pytania_Referendalne (odpowiedzi_tak, odpowiedzi_nie, Pytanie_Referendalne_id, Komisja_nr) VALUES (476, 123, 4, 4);
INSERT INTO Wyniki_Pytania_Referendalne (odpowiedzi_tak, odpowiedzi_nie, Pytanie_Referendalne_id, Komisja_nr) VALUES (754, 321, 4, 5);

--widoki
--CREATE OR REPLACE VIEW FREKWENCJA_WYBORCZA_REFERENDUM AS
--  SELECT w.*, (w.odpowiedzi_nie + w.odpowiedzi_tak) / k.liczba_Wyborcow FROM Wyniki_Pytania_Referendalne w LEFT JOIN Komisje k ON w.Komisja_nr = k.nr;

--CREATE OR REPLACE VIEW WYNIKI_PROCENTOWE_REFERENDUM AS
--  SELECT w.*, w.odp FROM Wyniki_Pytania_Referendalne w

COMMIT;