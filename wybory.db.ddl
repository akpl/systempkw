CREATE TABLE Uzytkownicy (
  id              number(10) NOT NULL, 
  login           varchar2(100) NOT NULL UNIQUE, 
  haslo           char(40) NOT NULL, 
  imie            varchar2(50) NOT NULL, 
  nazwisko        varchar2(50) NOT NULL, 
  Poziom_Dostepu_id number(10) NOT NULL,
  PRIMARY KEY (id));
CREATE TABLE Poziomy_Dostepu (
  id    number(10) NOT NULL, 
  nazwa number(10) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE Okregi (
  nr          number(10) NOT NULL, 
  nazwa       varchar2(100) NOT NULL UNIQUE, 
  wojewodztwo varchar2(50) NOT NULL, 
  miasto      varchar2(50) NOT NULL, 
  PRIMARY KEY (nr));
CREATE TABLE Wybory (
  id              number(10) NOT NULL,
  data_utworzenia date       NOT NULL,
  data_glosowania date       NOT NULL,
  Typ_Wyborow_id  number(10) NOT NULL,
  id_Tworcy       number(10) NOT NULL,
  PRIMARY KEY (id));
CREATE TABLE Typy_Wyborow (
  id    number(10) NOT NULL, 
  nazwa varchar2(100) NOT NULL UNIQUE, 
  PRIMARY KEY (id));
CREATE TABLE Komisje (
  nr                  number(10)    NOT NULL,
  nazwa               varchar2(100) NOT NULL UNIQUE,
  adres               varchar2(100) NOT NULL,
  liczba_Wyborcow     number(10),
  Okreg_Wyborczy_nr   number(10)    NOT NULL,
  id_Przewodniczacego number(10)    NOT NULL,
  PRIMARY KEY (nr));
CREATE TABLE Komitety (
  nr        number(10)    NOT NULL,
  nazwa     varchar2(100) NOT NULL UNIQUE,
  Wybory_id number(10)    NOT NULL,
  PRIMARY KEY (nr));
CREATE TABLE Kandydaci_Posel (
  id                   number(10)    NOT NULL,
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
  id                   number(10)    NOT NULL,
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
  id        number(10)    NOT NULL,
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
ALTER TABLE Wyniki_Pytania_Referendalne ADD CONSTRAINT fk_wyniki_pytania_referendalne_pytania_referendalne FOREIGN KEY (Pytanie_Referendalne_id) REFERENCES Pytania_Referendalne (id);
ALTER TABLE Wyniki_Prezydent ADD CONSTRAINT fk_wyniki_prezydent_kandydaci_prezydent FOREIGN KEY (Kandydat_Prezydent_id) REFERENCES Kandydaci_Prezydent (id);
ALTER TABLE Wyniki_Posel ADD CONSTRAINT fk_wyniki_posel_kandydaci_posel FOREIGN KEY (Kandydat_Posel_id) REFERENCES Kandydaci_Posel (id);
ALTER TABLE Wyniki_Pytania_Referendalne ADD CONSTRAINT fk_wyniki_pytania_referendalne_komisje FOREIGN KEY (Komisja_nr) REFERENCES Komisje (nr);
ALTER TABLE Wyniki_Prezydent ADD CONSTRAINT fk_wyniki_prezydent_komisje FOREIGN KEY (Komisja_nr) REFERENCES Komisje (nr);
ALTER TABLE Wyniki_Posel ADD CONSTRAINT fk_wyniki_posel_komisje FOREIGN KEY (Komisja_nr) REFERENCES Komisje (nr);
