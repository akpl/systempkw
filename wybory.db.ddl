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
  nr          NUMBER DEFAULT okregi_seq.nextval,
  nazwa       varchar2(100) NOT NULL UNIQUE, 
  wojewodztwo varchar2(50) NOT NULL, 
  miasto      varchar2(50) NOT NULL, 
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
  nazwa               varchar2(100) NOT NULL UNIQUE,
  adres               varchar2(100) NOT NULL,
  liczba_Wyborcow     number(10),
  Okreg_Wyborczy_nr   number(10)    NOT NULL,
  id_Przewodniczacego number(10)    NOT NULL,
  PRIMARY KEY (nr));
CREATE TABLE Komitety (
  nr        NUMBER DEFAULT komitety_seq.nextval,
  nazwa     varchar2(100) NOT NULL UNIQUE,
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
  odpowiedzi_tak          number(10) NOT NULL,
  odpowiedzi_nie          number(10) NOT NULL,
  Pytanie_Referendalne_id number(10) NOT NULL,
  Komisja_nr              number(10) NOT NULL,
  PRIMARY KEY (Pytanie_Referendalne_id,
               Komisja_nr));
CREATE TABLE Wyniki_Prezydent (
  liczba_Glosow         number(10) NOT NULL,
  Kandydat_Prezydent_id number(10) NOT NULL,
  Komisja_nr            number(10) NOT NULL,
  PRIMARY KEY (Kandydat_Prezydent_id,
               Komisja_nr));
CREATE TABLE Wyniki_Posel (
  liczba_Glosow     number(10) NOT NULL,
  Kandydat_Posel_id number(10) NOT NULL,
  Komisja_nr        number(10) NOT NULL,
  PRIMARY KEY (Kandydat_Posel_id,
               Komisja_nr));

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
ALTER TABLE Kandydaci_Prezydent ADD CONSTRAINT uc_nr_na_liscie UNIQUE (nr_na_liscie, Wybory_id);

--add data
INSERT INTO Poziomy_Dostepu (nazwa) VALUES ('ADMINISTRATOR');
INSERT INTO Poziomy_Dostepu (nazwa) VALUES ('CZLONEK_OKW');
INSERT INTO Poziomy_Dostepu (nazwa) VALUES ('CZLONEK_PKW');
INSERT INTO Uzytkownicy (login, haslo, imie, nazwisko, Poziom_Dostepu_id) VALUES ('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Jan', 'Kowalski', 1);
INSERT INTO Uzytkownicy (login, haslo, imie, nazwisko, Poziom_Dostepu_id) VALUES ('okw', '8dd3236cec0915e557c2daa36c5d346bcc2022edf48bbc5d992e11f3214105d7', 'Klemens', 'Dudek', 2);
INSERT INTO Uzytkownicy (login, haslo, imie, nazwisko, Poziom_Dostepu_id) VALUES ('pkw', '3632e89dc06fcdfd486d1afa40b7f97420293aa23ad7db63eefe05a51d0ac6ef', 'Arkadiusz', 'Gorski', 3);
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

COMMIT;