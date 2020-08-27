# ***Projekat:*** Studentska služba - Tim 2

***Opis:*** Web server studentske službe. Server napisan u Javi korišćenjem Springboot-a, sa MariaDB bazom i Thymeleaf template engine-om.

# Sadržaj:

- [Uputstvo](#uputstvo)
    * [Navigacija](#navigacija)
        + [Uvod](#uvod)
        + [Administrator](#administrator)
        + [Predavač](#predavač)
        + [Student](#student)
    * [Dodatno](#dodatno)
- [Credits](#credits)

# Uputstvo:

## Navigacija:

### Uvod

Pri ulasku na sajt korisniku će biti prikazana log-in stranica gde će nakon uspešne prijave dobiti pristup jednoj od sledećih stranica, zavisno od korisnikove uloge: administrator, student, predavač.

![Log-in dijagram](/diagrams/login_diagram.png)

Na svakoj stranici osim log-in strane se u desnom gornjem uglu nalazi user widget gde korisnik može da se izloguje ili da promeni korisničko ime i/ili lozinku.

### Administrator

![Administrator dijagram](/diagrams/admin_diagram.png)

Na ovoj strani korisnik može izabrati da izabere podatke kojima želi da pristupi:
- Studenti, Predavači, Smerovi, Predmeti: Ove stranice prikazuju tabele istoimenih podataka.
Moguće je filtrirati redove tabele i menjati njihov redosled prikazivanja. Moguće je dodavanje novih redova (Pritiskom na dugme desno od filtera) i modifikovanje/brisanje postojećih (klikom na DELETE dugme koje se pojavi pri hover-u nad redom).

- Ispiti: ovde se mogu videti ispiti i ispitni rokovi. Moguće je dodavanje (Klikom na zadnji red u tabelama) i brisanje (klikom na DELETE dugme koje se pojavi pri hover-u nad redom).

- Korisnici: ovde se mogu videti svi korisnici koji postoje, sortirani po ulogama u tri istoimene tabele.

### Predavač

![Predavač dijagram](/diagrams/predavac_diagram.png)

Na stranici predavač se nalaze:
- Osnovne informacije o predavaču (zvanje, ime i prezime),
- Uvid u predmete koje predaje,
- Uvid u ispite koje ocenjuje,
- Ocenjivanje studenata.
Sa ove stranice stranici predavač može započeti ocenjivanje studenata koji su polagali ispit nekog njihovog predmeta.

### Student

![Student dijagram](/diagrams/student_diagram.png)

Na stranici student se nalaze:
- Uvid u lične podatke studenta: ime, prezime, indeks, godina, budžet, smer, prosek, osvojeni ESPB.
- Uvid u predmete koje sluša.
- Uvid u ispitne rokove i ispite koje može da prijavi.
Sa ove stranice student može prijaviti željene ispite.

## Dodatno
Opisni dijagrami se nalaze u folderu "diagrams".

# Credits

Front-end: Danijel Anđelković 32/2018, Miloš Antić 34/2018

Back-end:
    Server: Dejan Belić 39/2018, Dušan Stevanović 121/2018
    Baza: Milutin Aleksić 31/2018


