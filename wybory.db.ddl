CREATE SEQUENCE seq_Uzytkownik;
CREATE SEQUENCE seq_PoziomDostepu;
CREATE SEQUENCE seq_Okreg;
CREATE SEQUENCE seq_Wybory;
CREATE SEQUENCE seq_TypWyborow;
CREATE SEQUENCE seq_Komisja;
CREATE SEQUENCE seq_Komitet;
CREATE SEQUENCE seq_KandydatPosel;
CREATE SEQUENCE seq_KandydatPrezydent;
CREATE SEQUENCE seq_PytanieReferendalne;
CREATE TABLE Uzytkownik (
  id              number(10) NOT NULL, 
  login           varchar2(100) NOT NULL UNIQUE, 
  haslo           char(40) NOT NULL, 
  imie            varchar2(50) NOT NULL, 
  nazwisko        varchar2(50) NOT NULL, 
  PoziomDostepuid number(10) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE PoziomDostepu (
  id    number(10) NOT NULL, 
  nazwa number(10) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE Okreg (
  nr          number(10) NOT NULL, 
  nazwa       varchar2(100) NOT NULL UNIQUE, 
  wojewodztwo varchar2(50) NOT NULL, 
  miasto      varchar2(50) NOT NULL, 
  PRIMARY KEY (nr));
CREATE TABLE Wybory (
  id              number(10) NOT NULL, 
  data_utworzenia date NOT NULL, 
  data_glosowania date NOT NULL, 
  TypWyborowid    number(10) NOT NULL, 
  idTworcy        number(10) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE TypWyborow (
  id    number(10) NOT NULL, 
  nazwa varchar2(100) NOT NULL UNIQUE, 
  PRIMARY KEY (id));
CREATE TABLE Komisja (
  nr                 number(10) NOT NULL, 
  nazwa              varchar2(100) NOT NULL UNIQUE, 
  adres              varchar2(100) NOT NULL, 
  liczbaWyborcow     number(10), 
  OkregWyborczynr    number(10) NOT NULL, 
  idPrzewodniczacego number(10) NOT NULL, 
  PRIMARY KEY (nr));
CREATE TABLE Komitet (
  nr       number(10) NOT NULL, 
  nazwa    varchar2(100) NOT NULL UNIQUE, 
  Wyboryid number(10) NOT NULL, 
  PRIMARY KEY (nr));
CREATE TABLE KandydatPosel (
  id                   number(10) NOT NULL, 
  imie                 varchar2(50) NOT NULL, 
  nazwisko             varchar2(50) NOT NULL, 
  plec                 char(1) NOT NULL, 
  zawod                varchar2(100) NOT NULL, 
  miejsce_zamieszkania varchar2(100) NOT NULL, 
  nr_na_liscie         number(10) NOT NULL, 
  partia               varchar2(100), 
  Komitetnr            number(10) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE KandydatPrezydent (
  id                   number(10) NOT NULL, 
  imie                 varchar2(50) NOT NULL, 
  nazwisko             varchar2(50) NOT NULL, 
  plec                 char(1) NOT NULL, 
  zawod                varchar2(100) NOT NULL, 
  miejsce_zamieszkania varchar2(100) NOT NULL, 
  nr_na_liscie         number(10) NOT NULL, 
  partia               varchar2(100), 
  Wyboryid             number(10) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE PytanieReferendalne (
  id       number(10) NOT NULL, 
  pytanie  varchar2(300) NOT NULL, 
  Wyboryid number(10) NOT NULL, 
  PRIMARY KEY (id));
CREATE TABLE WynikiPytanieReferendalne (
  odpowiedzi_tak        number(10) NOT NULL, 
  odpowiedzi_nie        number(10) NOT NULL, 
  PytanieReferendalneid number(10) NOT NULL, 
  Komisjanr             number(10) NOT NULL, 
  PRIMARY KEY (PytanieReferendalneid, 
  Komisjanr));
CREATE TABLE WynikiPrezydent (
  liczbaGlosow        number(10) NOT NULL, 
  KandydatPrezydentid number(10) NOT NULL, 
  Komisjanr           number(10) NOT NULL, 
  PRIMARY KEY (KandydatPrezydentid, 
  Komisjanr));
CREATE TABLE WynikiPosel (
  liczbaGlosow    number(10) NOT NULL, 
  KandydatPoselid number(10) NOT NULL, 
  Komisjanr       number(10) NOT NULL, 
  PRIMARY KEY (KandydatPoselid, 
  Komisjanr));
ALTER TABLE Uzytkownik ADD CONSTRAINT fk_uzytkownik_poziom_dostepu FOREIGN KEY (PoziomDostepuid) REFERENCES PoziomDostepu (id);
ALTER TABLE Wybory ADD CONSTRAINT fk_wybory_typ_wyborow FOREIGN KEY (TypWyborowid) REFERENCES TypWyborow (id);
ALTER TABLE Wybory ADD CONSTRAINT fk_wybory_uzytkownik FOREIGN KEY (idTworcy) REFERENCES Uzytkownik (id);
ALTER TABLE Komisja ADD CONSTRAINT fk_komisja_okreg FOREIGN KEY (OkregWyborczynr) REFERENCES Okreg (nr);
ALTER TABLE Komisja ADD CONSTRAINT fk_komisja_uzytkownik FOREIGN KEY (idPrzewodniczacego) REFERENCES Uzytkownik (id);
ALTER TABLE KandydatPosel ADD CONSTRAINT fk_kandydat_posel_komitet FOREIGN KEY (Komitetnr) REFERENCES Komitet (nr);
ALTER TABLE PytanieReferendalne ADD CONSTRAINT fk_pytanie_referendalne_wybory FOREIGN KEY (Wyboryid) REFERENCES Wybory (id);
ALTER TABLE KandydatPrezydent ADD CONSTRAINT fk_kandydat_prezydent_wybory FOREIGN KEY (Wyboryid) REFERENCES Wybory (id);
ALTER TABLE Komitet ADD CONSTRAINT fk_komitet_wybory FOREIGN KEY (Wyboryid) REFERENCES Wybory (id);
ALTER TABLE WynikiPytanieReferendalne ADD CONSTRAINT fk_wyniki_pytanie_referendalne_pytanie_referendalne FOREIGN KEY (PytanieReferendalneid) REFERENCES PytanieReferendalne (id);
ALTER TABLE WynikiPrezydent ADD CONSTRAINT fk_wyniki_prezydent_kandydat_prezydent FOREIGN KEY (KandydatPrezydentid) REFERENCES KandydatPrezydent (id);
ALTER TABLE WynikiPosel ADD CONSTRAINT fk_wyniki_posel_kandydat_posel FOREIGN KEY (KandydatPoselid) REFERENCES KandydatPosel (id);
ALTER TABLE WynikiPytanieReferendalne ADD CONSTRAINT fk_wyniki_pytanie_referendalne_komisja FOREIGN KEY (Komisjanr) REFERENCES Komisja (nr);
ALTER TABLE WynikiPrezydent ADD CONSTRAINT fk_wyniki_prezydent_komisja FOREIGN KEY (Komisjanr) REFERENCES Komisja (nr);
ALTER TABLE WynikiPosel ADD CONSTRAINT fk_wyniki_posel_komisja FOREIGN KEY (Komisjanr) REFERENCES Komisja (nr);
