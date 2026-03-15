Sovellus on tehty boulderointi kertojen tallentamiseen ja
tarkasteluun. Se koostuu seuraavista pääosista.


# Route (Record):
- grade (String)
- wallType (WallType enum)
- attempts (int)
- isCompleted (boolean)
- UUID (String)

Route on Record tyyppinen ja see kasaa yhteen kaikki yksittäiseen
reittiin kuuluvat tiedot.

### WallType (Enum)
- SLAB
- VERTICAL
- OVERHANG
- ROOF

WallType on enum luokka joka määrittää mitä tyyppiä seinä voi olla.

---


# Session (Record)
- listOfRoutes (Route)
- date (String)
- UUID (String)

Session on myös Record tyyppinen ja se sisältää listan kaikista
Routeista (reiteistä), jotka käyttäjä kiipesi annetulla kerralla.

---

# Archive (Class)
- listOfSessions (Session)
- month (int)
- year (int)

Archive on luokka joka tallentaa kuukauden ajan kaikki Session luokat
omaan listaansa. Uuden kuukauden alussa tehdään uusi tyhjä Archive ja 
vanha arkistoidaan historiaan näkyville, josta voi tarkistella omaa progressia.
 
