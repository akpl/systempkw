create or replace PROCEDURE OBLICZ_WYNIKI_W_OKREGU
  (
      NR_OKREGU IN NUMBER
    , ID_WYBOROW IN NUMBER
    , MIN_GLOSOW IN NUMBER
  ) IS
  wspolczynnik NUMBER;
  liczba_mandatow_w_okregu NUMBER;
  liczba_glosow_na_komitet NUMBER;
  BEGIN
    SELECT LICZBA_MANDATOW INTO liczba_mandatow_w_okregu FROM OKREGI WHERE NR = NR_OKREGU;

    DELETE FROM TEMP_WSPOLCZYNNIKI;
    FOR wynik IN (SELECT kp.KOMITET_NR, SUM(wp.LICZBA_GLOSOW) AS GLOSY
                  FROM WYNIKI_POSEL wp
                    JOIN KANDYDACI_POSEL kp ON kp.ID = wp.KANDYDAT_POSEL_ID
                  WHERE wp.KOMISJA_NR IN (SELECT NR FROM KOMISJE WHERE OKREG_WYBORCZY_NR = NR_OKREGU)
                        AND kp.KOMITET_NR IN (SELECT NR FROM KOMITETY WHERE WYBORY_ID = ID_WYBOROW)
                  GROUP BY kp.KOMITET_NR)
    LOOP
      SELECT SUM(wp.LICZBA_GLOSOW) INTO liczba_glosow_na_komitet
      FROM WYNIKI_POSEL wp
        JOIN KANDYDACI_POSEL kp ON kp.ID = wp.KANDYDAT_POSEL_ID
      WHERE kp.KOMITET_NR = wynik.KOMITET_NR;
      IF liczba_glosow_na_komitet >= MIN_GLOSOW THEN --czy komitet przekroczyl prog wyborczy w kraju?
        --suma glosow dla danego komitetu jest dzielona przez kolejne liczby naturalne
        FOR dzielnik IN 1..LICZBA_MANDATOW_W_OKREGU LOOP
          wspolczynnik := TRUNC(wynik.GLOSY / dzielnik); --zapisujemy wylacznie czesc calkowita
          INSERT INTO TEMP_WSPOLCZYNNIKI (KOMITET_NR, WSPOLCZYNNIK) VALUES (wynik.KOMITET_NR, wspolczynnik);
        END LOOP;
      END IF;
    END LOOP;

    DELETE FROM WYNIKI_PARLAMENTARNE wp WHERE wp.WYBORY_ID = ID_WYBOROW AND wp.OKREG_WYBORCZY_NR = NR_OKREGU;
    INSERT INTO WYNIKI_PARLAMENTARNE
    (ID, WYBORY_ID, OKREG_WYBORCZY_NR, KOMITET_NR, LICZBA_POSLOW)
      SELECT "WYNIKI_PARLAMENTARNE_SEQ"."NEXTVAL", ID_WYBOROW, NR_OKREGU, KOMITET_NR, LICZBA_POSLOW FROM (SELECT KOMITET_NR, COUNT(*) AS LICZBA_POSLOW FROM (SELECT * FROM (SELECT * FROM TEMP_WSPOLCZYNNIKI ORDER BY WSPOLCZYNNIK DESC) WHERE rownum <= LICZBA_MANDATOW_W_OKREGU) GROUP BY KOMITET_NR);
  END OBLICZ_WYNIKI_W_OKREGU;

create or replace PROCEDURE OBLICZ_WYNIKI_WYBOROW
  (
      ID_WYBOROW IN NUMBER
    , PROG_WYBORCZY IN NUMBER DEFAULT 5 -- 5% to przyjety w Polsce prog wyborczy
  ) IS
  liczba_glosow_w_wyborach NUMBER;
  minimalna_liczba_glosow NUMBER;
  BEGIN
    SELECT SUM(wp.LICZBA_GLOSOW) INTO liczba_glosow_w_wyborach
    FROM WYNIKI_POSEL wp
      JOIN KANDYDACI_POSEL kp ON kp.ID = wp.KANDYDAT_POSEL_ID
    WHERE kp.KOMITET_NR IN (SELECT NR FROM KOMITETY WHERE WYBORY_ID = ID_WYBOROW);

    minimalna_liczba_glosow := TRUNC((PROG_WYBORCZY / 100) * liczba_glosow_w_wyborach);

    FOR okreg IN (SELECT NR FROM OKREGI) LOOP
      OBLICZ_WYNIKI_W_OKREGU(okreg.NR, ID_WYBOROW, minimalna_liczba_glosow);
    END LOOP;
  END OBLICZ_WYNIKI_WYBOROW;