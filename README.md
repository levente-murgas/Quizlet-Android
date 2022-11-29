# Házi feladat specifikáció

## Mobil- és webes szoftverek
### 2022.10.13
### Quizlet
### Murgás Levente - M5E2T9
### murgaslevi@gmail.com
### Laborvezető: Reiter Márton

## Bemutatás

Az alkalmazás elsősorban a tanulást hivatott elősegíteni, ezért minden hallgató számára nagyon hasznos lehet. Tanulókártyákat lehet benne létrehozni amiknek egyik oldalára kifejezések, másik oldalára definíciók kerülnek. Ezekből a kártyákból paklikat (study set) lehet létrehozni amik segítségével a felhasználó kedvére csoportosíthatja a kártyákat téma szerint.  

## Főbb funkciók

Az alkalmazásban lehetőség van paklik létrehozására, új kártyák felvételére pakliba, illetve paklik törlésésre és már meglévő kártyák törlésére a pakliból. A felvett kártyákat lista szerűen megnézheti a felhasználó, ezen kívül lehetőség van kikérdezésre is, amikor egy pakli minden kártyáját a felhasználó megnézheti egyesével. Az egyes kártyákat beteheti a "Megtanultam" illetve "Még tanulnom kell" kategóriák egyikébe. Az utóbbi kategóriába tartozó kártyáknak kérheti az újboli kikérdezését a kikérdezés végén egészen addig amíg nem marad több ilyen kártya. A "Megtanultam" kategóriába rakással a kártya kikerül a "Még tanulnom kell" kategóriából, ha eddig abban volt. Az alkalmazásból való kilépéskor a létrehozott paklik perzisztens módon kerülnek eltárolásra.

## Választott technológiák:

- UI
- fragmentek,
- RecyclerView,
- Perzisztens adattárolás.
