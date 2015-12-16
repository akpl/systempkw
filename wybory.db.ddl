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

CREATE TABLE Uzytkownicy (
  id              NUMBER GENERATED ALWAYS AS IDENTITY,
  login           varchar2(100) NOT NULL UNIQUE, 
  haslo           char(64) NOT NULL,
  imie            varchar2(50) NOT NULL, 
  nazwisko        varchar2(50) NOT NULL, 
  Poziom_Dostepu_id number(10) NOT NULL,
  PRIMARY KEY (id));
CREATE TABLE Poziomy_Dostepu (
  id    NUMBER GENERATED ALWAYS AS IDENTITY,
  nazwa varchar2(50) NOT NULL,
  PRIMARY KEY (id));
CREATE TABLE Okregi (
  nr          NUMBER GENERATED ALWAYS AS IDENTITY,
  nazwa       varchar2(100) NOT NULL UNIQUE, 
  wojewodztwo varchar2(50) NOT NULL, 
  miasto      varchar2(50) NOT NULL, 
  PRIMARY KEY (nr));
CREATE TABLE Wybory (
  id              NUMBER GENERATED ALWAYS AS IDENTITY,
  data_utworzenia date       NOT NULL,
  data_glosowania date       NOT NULL,
  Typ_Wyborow_id  number(10) NOT NULL,
  id_Tworcy       number(10) NOT NULL,
  PRIMARY KEY (id));
CREATE TABLE Typy_Wyborow (
  id    NUMBER GENERATED ALWAYS AS IDENTITY,
  nazwa varchar2(100) NOT NULL UNIQUE, 
  PRIMARY KEY (id));
CREATE TABLE Komisje (
  nr                  NUMBER GENERATED ALWAYS AS IDENTITY,
  nazwa               varchar2(100) NOT NULL UNIQUE,
  adres               varchar2(100) NOT NULL,
  liczba_Wyborcow     number(10),
  Okreg_Wyborczy_nr   number(10)    NOT NULL,
  id_Przewodniczacego number(10)    NOT NULL,
  PRIMARY KEY (nr));
CREATE TABLE Komitety (
  nr        NUMBER GENERATED ALWAYS AS IDENTITY,
  nazwa     varchar2(100) NOT NULL UNIQUE,
  Wybory_id number(10)    NOT NULL,
  PRIMARY KEY (nr));
CREATE TABLE Kandydaci_Posel (
  id                   NUMBER GENERATED ALWAYS AS IDENTITY,
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
  id                   NUMBER GENERATED ALWAYS AS IDENTITY,
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
  id        NUMBER GENERATED ALWAYS AS IDENTITY,
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
ALTER TABLE Kandydaci_Posel ADD CONSTRAINT fk_kandydaci_posel_komitety FOREIGN KEY (Komitet_nr) REFERENCES Komitety (nr);
ALTER TABLE Pytania_Referendalne ADD CONSTRAINT fk_pytania_referendalne_wybory FOREIGN KEY (Wybory_id) REFERENCES Wybory (id);
ALTER TABLE Kandydaci_Prezydent ADD CONSTRAINT fk_kandydaci_prezydent_wybory FOREIGN KEY (Wybory_id) REFERENCES Wybory (id);
ALTER TABLE Komitety ADD CONSTRAINT fk_komitety_wybory FOREIGN KEY (Wybory_id) REFERENCES Wybory (id);
ALTER TABLE Wyniki_Pytania_Referendalne ADD CONSTRAINT fk_wyniki_pytania_pytania FOREIGN KEY (Pytanie_Referendalne_id) REFERENCES Pytania_Referendalne (id);
ALTER TABLE Wyniki_Prezydent ADD CONSTRAINT fk_wyniki_prezydent_prezydent FOREIGN KEY (Kandydat_Prezydent_id) REFERENCES Kandydaci_Prezydent (id);
ALTER TABLE Wyniki_Posel ADD CONSTRAINT fk_wyniki_posel_posel FOREIGN KEY (Kandydat_Posel_id) REFERENCES Kandydaci_Posel (id);
ALTER TABLE Wyniki_Pytania_Referendalne ADD CONSTRAINT fk_wyniki_pytania_komisje FOREIGN KEY (Komisja_nr) REFERENCES Komisje (nr);
ALTER TABLE Wyniki_Prezydent ADD CONSTRAINT fk_wyniki_prezydent_komisje FOREIGN KEY (Komisja_nr) REFERENCES Komisje (nr);
ALTER TABLE Wyniki_Posel ADD CONSTRAINT fk_wyniki_posel_komisje FOREIGN KEY (Komisja_nr) REFERENCES Komisje (nr);

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

COMMIT;