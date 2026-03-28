# Vezérfonal – Projektdokumentáció

---

## 1. Tartalomjegyzék

1. [Tartalomjegyzék](#1-tartalomjegyzék)
2. [Bevezetés, témaválasztás indoklása, kitűzött célok](#2-bevezetés-témaválasztás-indoklása-kitűzött-célok)
3. [Csapatmunka megvalósítása](#3-csapatmunka-megvalósítása)
4. [Felhasználói dokumentáció](#4-felhasználói-dokumentáció)
5. [Fejlesztői dokumentáció](#5-fejlesztői-dokumentáció)
6. [Összegzés](#6-összegzés)
7. [Továbbfejlesztési lehetőségek](#7-továbbfejlesztési-lehetőségek)
8. [Irodalomjegyzék](#8-irodalomjegyzék)

---

## 2. Bevezetés, témaválasztás indoklása, kitűzött célok

### 2.1 A probléma

A modern szervezetekben – legyen szó iskolákról, vállalatokról, civil közösségekről vagy bármilyen más intézményről – az információ hatékony és ellenőrzött eljuttatása a tagokhoz állandó kihívást jelent. A mindennapi tapasztalat azt mutatja, hogy a legtöbb szervezet kommunikációja informális csatornákon zajlik: WhatsApp-csoportokban, Facebook Messengerben, Viberen vagy egyéb általános célú üzenetküldő alkalmazásokban. Ezek a megoldások ugyan könnyen elérhetők, de alapvető funkcionalitásbeli hiányosságokkal küzdenek, amelyek súlyosan rontják az információ átadásának minőségét.

Az általános célú üzenetküldők legnagyobb problémája, hogy az üzenetek elvesznek a csevegések zűrzavarában. Egy fontos közlemény – legyen az egy tanár órarendváltoztatása, egy iskolai rendezvény értesítője vagy egy munkahelyi feladat kiosztása – perceken belül eltűnik a folyamatos üzenetáradat alatt, és a címzettnek magának kell visszagörgetnie, megkeresnie azt. Nincs rá garancia, hogy az információ egyáltalán eljutott-e a megfelelő személyekhez, és ha eljutott is, nem lehet tudni, mikor olvasták el.

A másik alapvető hiányosság az ellenőrizhetőség teljes hiánya. A közleményt közzétevő személy – legyen az egy tanár, egy vezető vagy egy szervező – nem kaphat visszajelzést arról, hogy az adott információt a megcímzett személyek elolvasták-e. Ha például egy iskolai eseményre szólít fel egy körtájékoztató, az esemény koordinátora csak találgathat, hogy a diákok értesültek-e az időpontról és a helyszínről. Ez számos esetben félreértésekhez, elmaradt eseményekhez és felesleges személyes egyeztetésekhez vezet.

Egy harmadik, kevésbé nyilvánvaló, mégis komoly probléma a jogosultságok és szerepkörök kezelésének hiánya. Az általános chat-alkalmazásokban minden résztvevő egyforma jogokkal rendelkezik: mindenki küldhet üzenetet, mindenki módosíthat, és nincs strukturált különbség a vezető és a beosztott, az információt közlő és az azt befogadó személy között. Ez a szimmetria nem tükrözi a valóságos szervezeti hierarchiákat, és ahhoz vezet, hogy a fontos közlemények elvegyülnek a mindennapi csevegések között.

### 2.2 A témaválasztás indoklása

A Vezérfonal projekt ötlete ebből a mindenki által tapasztalt problémából született. A fejlesztők – Gubacsi Áron Máté és Balogh Márk – saját tapasztalataikon keresztül szembesültek azzal, mennyire nehézkes visszakeresni egy fontos információt egy zsúfolt csoportos csevegésben, és mennyire nem lehet tudni, hogy az adott üzenet el is jutott-e a megfelelő személyekhez.

Az eredeti elképzelés szerint a projekt célközönsége az oktatási szektor volt. A tervdokumentum szerint az alkalmazás az iskolai rendszerek kommunikációs problémáit oldotta volna meg, különös figyelmet fordítva a következő kommunikációs irányokra:

- **Iskolavezetőség ↔ tanárok:** belső utasítások, értekezletek, szervezési kérdések
- **Tanárok ↔ diákok:** órarendváltozások, tananyag-kiegészítések, házi feladatok
- **Diákönkormányzat ↔ diákok:** diákélettel kapcsolatos információk
- **Egyéb szerepkörök ↔ résztvevők:** programszervező pedagógusok, szakköri vezetők és eseményeik résztvevői

A fejlesztés során azonban nyilvánvalóvá vált, hogy az azonosított problémák és a kidolgozott megoldások messze nem kizárólag az oktatási szektorra jellemzők. Egy vállalat részlegei között zajló kommunikáció, egy civil szervezet eseményszervezése vagy egy sportklub belső tájékoztatása mind ugyanazokkal a kihívásokkal néz szembe. Ezért a projekt hatóköre fokozatosan bővült: a Vezérfonal végleges formájában **bármely szervezet** számára alkalmas célzott belső kommunikációs platformmá vált, ahol az adminisztrátorok üzeneteket küldhetnek csoportoknak vagy egyéneknek, a címzettek pedig megtekintési visszajelzésekkel és emoji reakciókkal reagálhatnak.

A témaválasztást erősen motiválta az is, hogy a projekt technológiai kihívást is jelentett: egy cross-platform alkalmazás fejlesztése, amely Android, iOS, web és asztali (desktop) platformokon egyaránt fut, miközben a mögöttes üzleti logika és felhasználói felület kódjának minél nagyobb része közös. Ez lehetőséget adott arra, hogy a fejlesztők megismerkedjenek a Kotlin Multiplatform ökoszisztémával, amely az iparban egyre inkább teret nyer.

### 2.3 Kitűzött célok

A projekt elindításakor a következő konkrét célokat tűzték ki:

**Funkcionális célok:**

1. **Cross-platform elérhetőség:** Az alkalmazás Android, iOS, web (böngészőalapú) és desktop (asztali) platformokon egyaránt fusson, közös kódbázisból kiindulva.
2. **Célzott üzenetküldés:** Az adminisztrátorok egyedi felhasználóknak vagy csoportoknak küldjenek üzeneteket, amelyek más felhasználókhoz nem jutnak el.
3. **Olvasási visszajelzés:** A rendszer rögzítse és megjelenítse, hogy az egyes címzettek mikor nyitották meg az üzenetet.
4. **Emlékeztető funkció (nudge):** Ha az üzenet nem olvasott, az adminisztrátor egyetlen gombnyomással emlékeztetőt küldhessen.
5. **Emoji reakciók:** A felhasználók az üzenetekre előre meghatározott emoji-készlettel reagáljanak.
6. **Admin hierarchia és jogosultságkezelés:** A rendszer különböztessen meg szuperadmin, csoportadmin és sima felhasználói szerepkört, és ezekhez eltérő engedélyeket rendeljen.
7. **Valós idejű értesítések:** Új üzenet érkezésekor a felhasználók azonnali push értesítést kapjanak (mobilon), illetve a webes kliensek valós időben frissüljenek.
8. **Biztonságos autentikáció:** JWT-alapú belépés, BCrypt jelszóhashelés, opcionális kétfaktoros azonosítás (2FA) email kódküldéssel.
9. **Szervezeti elkülönítés:** Különböző szervezetek adatai teljesen elkülönüljenek egymástól az adatbázisban.

**Nem funkcionális célok:**

1. A felhasználói felület intuitív, letisztult és modern legyen, Material Design 3 irányelvek alapján.
2. A rendszer biztonságos legyen: a tokeneket az adatbázis tárolja és szerver oldalról visszavonhatóak legyenek.
3. A kódbázis fenntartható legyen: egységes kódolási konvenciókkal, jól elkülönített modulokkal.
4. A szerver skálázható legyen: a multi-tenant adatbázis-séma lehetővé tegye több szervezet egyidejű kiszolgálását.

---

## 3. Csapatmunka megvalósítása

### 3.1 A csapat összetétele

A Vezérfonal projekten két fejlesztő dolgozott: **Gubacsi Áron Máté** és **Balogh Márk**. A csapat mérete tudatos döntés volt: egy kétfős csapatban a kommunikáció közvetlen és gyors, a felelősségi körök egyértelműek, és elkerülhetők a nagyobb csapatokra jellemző koordinációs nehézségek.

### 3.2 Feladatmegosztás

A munkamegosztás a két fejlesztő meglévő tapasztalatai és érdeklődési köre alapján alakult ki természetes módon.

**Gubacsi Áron Máté** felelt a **teljes szerver oldali fejlesztésért**. Ide tartozik a Ktor-alapú backend megírása, az adatbázis-réteg kialakítása (Exposed ORM, PostgreSQL sémastruktúra, multi-tenant architektúra), az autentikációs rendszer (JWT, BCrypt, 2FA), az API végpontok implementálása, a Firebase Cloud Messaging integráció a szerveren, az email küldési infrastruktúra (Resend, Thymeleaf sablonok) és a profil-kép kezelési rendszer. Emellett Áron részt vett a kliensoldal komplexebb részeiben is: a platform-specifikus implementációkban (`expect`/`actual` mechanizmus), a hálózati réteg kialakításában és a Kotlin típusrendszer erős kihasználásában igénylő megoldásokban (például a `GeneralSelectionDialog` generikus, `reified` típusparaméteres komponens).

**Balogh Márk** felelt a **teljes felhasználói felület fejlesztéséért**. Ide tartozik az összes képernyő Compose Multiplatform alapon történő implementálása, a Voyager-alapú navigáció kialakítása, az állapotkezelési réteg (state model és controller osztályok), a témázás (Material 3, sötét/világos mód), az összes UI-komponens (szűrők, kiválasztó dialógusok, kártyák, csúsztatós gombok) megvalósítása, valamint a frontend és a backend hálózati rétegének összekötése.

Ez a megosztás természetesen nem volt merev: a két fejlesztő folyamatosan segítette egymást, megvitatta az architektúrális döntéseket, és szükség esetén benyúlt egymás területébe – különösen az összetettebb funkciók esetén, ahol a szerver és a kliens szoros együttműködése volt szükséges.

### 3.3 Együttműködési eszközök

A csapat a következő eszközöket használta a közös munka megszervezéséhez:

**GitHub** volt a verziókezelés és a kódmegosztás elsődleges eszköze. A fejlesztés fő ágai a `master` (stabil, releaselhető állapot) és a `ui-test` (aktív fejlesztési ág) volt. A nagyobb funkciókat pull request formájában merge-elték be – jól látható példa erre a #11-es pull request, amely az UI tesztelési ág végleges integrációját jelenti. A commit-üzenetek rövidek, a változtatások magukon a diff-eken keresztül követhetők nyomon.

**Discord** videóhívásokat a csapat rendszeres megbeszélésekre használta. Ezeken a találkozókon a közelgő feladatokat osztották ki, az architekturális kérdéseket vitatták meg, és az esetleges elakadásokat oldották fel közösen.

**Személyes találkozók** lehetővé tették a pair programming jellegű munkát az összetettebb részfeladatokon, ahol az azonnali visszacsatolás kulcsfontosságú volt.

**Szöveges üzenetváltás (chat)** folyamatos, aszinkron kommunikációt tett lehetővé a napi apróbb kérdések, kód-review megjegyzések és gyors döntések esetén.

### 3.4 Feladatkövetés és munkamódszer

A csapat a feladatkövetéshez egy egyszerű, de hatékony módszert alkalmazott: a `composeApp` modulban létrehoztak egy `TODO.kt` fájlt, amelybe az aktuálisan elvégzendő feladatokat jegyezték fel. Ez az élő dokumentum végigkísérte a fejlesztést: az új ötletek és hiányosságok folyamatosan kerültek bele, a kész feladatok pedig megjelölésre kerültek.

A munkafolyamat iteratív volt: az egyik fejlesztő kiválasztott egy feladatot, amelynek megvalósítását saját kompetenciája alapján vállalni tudta, elvégezte azt, majd a következő feladatra tért át. Ez az egyszerű, rugalmas módszer jól illeszkedett a kétfős csapat dinamikájához, és minimalizálta az adminisztratív terhelést.

### 3.5 Kihívások a csapatmunkában

A projekt során a csapat több kihívással szembesült. **Balogh Márk** a projekt megkezdésekor ismerkedett meg a Kotlin programozási nyelvvel, a Jetpack Compose UI keretrendszerrel és az ebben a fejlesztési ökoszisztémában bevett megoldásokkal. Ez a tanulási görbe természetszerűleg lassította a munkát az első szakaszban, de a projekt végére Márk magabiztosan kezelte ezeket az eszközöket.

**Gubacsi Áron Máté** rendelkezett korábbi Android és Kotlin tapasztalattal, de a Kotlin Multiplatform és a Compose Multiplatform egy lényegesen összetettebb terület, mint a hagyományos Android fejlesztés: a platform-specifikus különbségeket kezelni, a WebAssembly fordítást megérteni, az iOS natív integrációt beállítani mind olyan kihívások, amelyekkel a projekt előtt nem kellett szembenézni.

Visszatekintve a fejlesztési időszakra, az egyik legfontosabb tanulság az időgazdálkodással kapcsolatos: a határidőközeli nyomás néha oda vezetett, hogy egyes rendszerek félkész állapotban kerültek be a kódbázisba, amelyeket később újra kellett tervezni és implementálni. Ez többletmunkát generált, és néhány alkalommal instabilitást okozott.

---

## 4. Felhasználói dokumentáció

### 4.1 Az alkalmazás áttekintése

A Vezérfonal egy szervezeti kommunikációs platform, amelynek célja, hogy az adminisztrátorok célzottan tudjanak üzeneteket küldeni meghatározott csoportoknak vagy egyéneknek, a célszemélyek pedig emoji reakciókkal és olvasási visszajelzésekkel reagálhassanak. Az alkalmazás elérhető Android, iOS, web (böngészőben) és asztali (desktop) platformokon.

A rendszer három alapvető szerepkört különböztet meg:

- **Sima felhasználó:** üzeneteket kap, reagálhat rájuk, megtekintheti a korábbi üzeneteket, archiválhatja azokat.
- **Csoportadmin:** a sima felhasználói funkciókon túl üzeneteket küldhet az általa adminisztrált csoportoknak, megtekintheti az üzeneteire érkező reakciókat és olvasási státuszokat.
- **Szuperadmin:** teljes hozzáféréssel rendelkezik: felhasználókat kezelhet, csoportokat hozhat létre és törölhet, cimkéket kezelhet, regisztrációs kódokat generálhat és törölhet, és minden más szervezeti szintű műveletet elvégezhet.

### 4.2 Rendszerkövetelmények

**Szerver:**
- Java 21 vagy újabb
- PostgreSQL 14 vagy újabb
- Firebase-projekt (push értesítésekhez)
- Resend fiók (2FA email küldéséhez)
- Szükséges környezeti változók: `DATABASE_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET`, `PFP_DIR`, `FIREBASE_CRED`, `RESEND_API_TOKEN`, `RESEND_FROM_EMAIL`

**Android:**
- Android 13.0 (API level 33) vagy újabb

**iOS:**
- iOS 15.0 vagy újabb
- Xcode szükséges a build elkészítéséhez

**Web:**
- Bármely modern böngésző (Chrome, Firefox, Safari, Edge) – a WebAssembly változathoz WebAssembly támogatás szükséges (2024 óta minden főbb böngészőben alapértelmezetten elérhető)

**Desktop:**
- Java 17 vagy újabb futtatókörnyezet

### 4.3 Az alkalmazás telepítése és indítása

#### 4.3.1 Szerver indítása

A szerver Ktor keretrendszerre épülő Kotlin alkalmazás, amely a következő paranccsal fordítható le és futtatható fejlesztési módban:

```bash
./gradlew :server:run
```

Éles környezethez az önálló futtatható JAR fájl (fat JAR) az alábbi paranccsal hozható létre:

```bash
./gradlew :server:shadowJar
```

Az eredményül kapott `server/build/libs/vezerfonal.jar` fájl a következőképpen futtatható:

```bash
java -jar vezerfonal.jar
```

A szerver indítása előtt gondoskodni kell az összes szükséges környezeti változó beállításáról.

#### 4.3.2 Android

Az Android alkalmazás a következő paranccsal fordítható le debug módban:

```bash
./gradlew :composeApp:assembleDebug
```

Az eredményül kapott APK fájl a `composeApp/build/outputs/apk/debug/` mappában található, és manuálisan telepíthető Android eszközre (oldalsó telepítés). Az éles kiadáshoz a szokásos Android aláírási és Play Store feltöltési folyamat alkalmazandó.

#### 4.3.3 iOS

Az iOS alkalmazás Xcode segítségével buildelhető. A CocoaPods függőségek a következő paranccsal tölthetők le:

```bash
cd iosApp && pod install
```

Ezután a `iosApp/iosApp.xcworkspace` fájl megnyitható Xcode-ban, és az alkalmazás szimulátorra vagy fizikai eszközre futtatható.

#### 4.3.4 Web (WebAssembly – ajánlott)

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

Ez a parancs lefordítja és elindítja a WebAssembly változatot, majd automatikusan megnyitja a böngészőt.

#### 4.3.5 Web (JavaScript – régebbi böngészőkhöz)

```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

#### 4.3.6 Desktop (JVM)

```bash
./gradlew :composeApp:run
```

### 4.4 Az alkalmazás használata

#### 4.4.1 Landing oldal

Az alkalmazás indításakor a landing oldal jelenik meg, ahonnan a felhasználó bejelentkezhet vagy regisztrálhat.

![Landing oldal](/home/garon/Desktop/.screenshots/landing_page.jpg)

#### 4.4.2 Regisztráció

A regisztráció háromfázisú folyamat.

**1. fázis – Regisztrációs kód megadása**

Az alkalmazáshoz csatlakozáshoz minden felhasználónak szüksége van egy érvényes, a szuperadmin által kiosztott regisztrációs kódra. Ez a kód azonosítja a szervezetet, amelyhez a felhasználó csatlakozik. Az első regisztrációs kód a szervezet létrehozásakor generálódik. Ha a megadott kód egy új szervezethez tartozik, a rendszer felkéri a felhasználót a szervezet nevének megadására is.

![Regisztrációs kód megadása](/home/garon/Desktop/.screenshots/reg_code_register.jpg)

**2. fázis – Hitelesítési adatok**

A következő lépésben a felhasználó megadja az email-címét és jelszavát. A jelszónak legalább 8 karakterből kell állnia.

![Hitelesítési adatok megadása](/home/garon/Desktop/.screenshots/credentials_register.jpg)

**3. fázis – Profil létrehozása**

Az utolsó lépésben a felhasználó beállítja a megjelenített nevét és opcionálisan profilképet tölt fel.

![Profil létrehozása](/home/garon/Desktop/.screenshots/profile_create_register.jpg)

#### 4.4.3 Szervezet létrehozása

Ha a megadott regisztrációs kód egy teljesen új szervezethez tartozik (azaz az adott szervezetnév még nem létezik a rendszerben), a rendszer felkéri a felhasználót a szervezet nevének megadására. Az első regisztrált felhasználó automatikusan szuperadmin jogosultságot kap.

![Szervezet létrehozása](/home/garon/Desktop/.screenshots/create_org.jpg)

#### 4.4.4 Bejelentkezés

A regisztrált felhasználó email-cím és jelszó megadásával léphet be.

![Bejelentkezési oldal](/home/garon/Desktop/.screenshots/login_form_page.jpg)

Ha a felhasználón engedélyezve van a kétfaktoros azonosítás (2FA), a sikeres jelszóellenőrzés után a rendszer egy 6 jegyű kódot küld a regisztrált email-címre.

![2FA bejelentkezési képernyő](/home/garon/Desktop/.screenshots/2fa_login.jpg)

![2FA kód az emailben](/home/garon/Desktop/.screenshots/2fa_code_email.jpg)

#### 4.4.5 Főoldal – Üzenet inbox

A sikeres bejelentkezés után a felhasználó a főoldalra kerül, ahol az összes hozzá vagy az általa tagolt csoportokhoz érkező, nem archivált üzenet látható. Az üzenetek időrendi sorrendben jelennek meg, a legfrissebb üzenet kerül a lista tetejére. A sürgős üzenetek vizuálisan kiemelve látszanak.

![Főoldal](/home/garon/Desktop/.screenshots/home_page.jpg)

Az üzenetekre közvetlenül az üzenetkártyáról lehet emoji reakciót küldeni. Az elérhető reakciók az adminisztrátor által az üzenet küldésekor meghatározott készletből kerülnek ki.

Az inbox szűrhető cimkék, üzenet státusz (olvasott/nem olvasott), sürgősség és egyéb szempontok alapján.

#### 4.4.6 Csoportok

A csoportok fülön a felhasználó megtekintheti a szervezeten belüli csoportokat, amelyeknek tagja.

![Csoportok](/home/garon/Desktop/.screenshots/groups.jpg)

A szuperadmin számára a csoportok oldal kiegészül a csoportkezelési funkciókkal.

![Csoportok – szuperadmin nézet](/home/garon/Desktop/.screenshots/groups_page_super_admin.jpg)

#### 4.4.7 Üzenet küldése (adminisztrátoroknak)

A csoportadmin és szuperadmin jogkörrel rendelkező felhasználók üzenetet küldhetnek. Az üzenetküldő képernyőn meg kell adni:

- Az üzenet **tárgyát** és **tartalmát**
- A **címzettet** (egy vagy több felhasználó, egy vagy több csoport)
- Opcionálisan **cimkéket** (tag-eket) a szűrhetőség érdekében
- A **reakciókészletet** (melyek az emoji-k, amelyekkel a címzett reagálhat)
- Hogy **sürgős-e** az üzenet

![Üzenet küldése](/home/garon/Desktop/.screenshots/writee_message_empty.jpg)

#### 4.4.8 Küldött üzenetek megtekintése

Az adminisztrátor a küldött üzenetei listájában visszanézheti, hogy ki és mikor olvasta el az egyes üzeneteket.

![Küldött üzenetek listája](/home/garon/Desktop/.screenshots/sent_messages.jpg)

Az egyes üzenetek részletes státuszinformációi is megtekinthetők: ki olvasta el, ki nem, és ki reagált, és mivel.

![Üzenet státusz – lista nézet](/home/garon/Desktop/.screenshots/check_sent_message_status_list.jpg)

![Üzenet státusz – egyéni nézet](/home/garon/Desktop/.screenshots/check_sent_message_status_individual.jpg)

#### 4.4.9 Archívum

A felhasználó manuálisan archiválhatja azokat az üzeneteket, amelyekkel már végzett. Az archivált üzenetek az archívum fülön tekinthetők meg, és bármikor visszahelyezhetők az inboxba.

![Archívum](/home/garon/Desktop/.screenshots/archive.jpg)

#### 4.4.10 Fiókbeállítások

A beállítások képernyőn a felhasználó módosíthatja az adatait: megjelenített nevét, profilképét, valamint kezelheti a biztonsági beállításait.

![Fiókbeállítások](/home/garon/Desktop/.screenshots/account_settings.jpg)

![Beállítások főképernyő](/home/garon/Desktop/.screenshots/settings_screen.jpg)

#### 4.4.11 Jelszócsere

A jelszó megváltoztatásához a rendszer ellenőrző kódot küld a regisztrált email-címre.

![Jelszócsere](/home/garon/Desktop/.screenshots/change_password.jpg)

#### 4.4.12 Archívum beállítások

A felhasználó konfigurálhatja az automatikus archiválási szabályokat: beállítható, hogy mennyi idő elteltével kerüljenek az üzenetek automatikusan az archívumba.

![Archívum beállítások](/home/garon/Desktop/.screenshots/archive_settings.jpg)

#### 4.4.13 Kétfaktoros azonosítás beállítása

A felhasználó a fiókbeállításokból kapcsolhatja be a kétfaktoros azonosítást. A 2FA bekapcsolt állapotban minden belépésnél email kód megerősítése szükséges.

![2FA bekapcsolva](/home/garon/Desktop/.screenshots/2fa_turned_on.jpg)

#### 4.4.14 Fiók törlési kérelem

Ha a felhasználó törölni szeretné a fiókját, kérelmet nyújthat be a fiókbeállításokból.

![Fiók törlési kérelem](/home/garon/Desktop/.screenshots/delete_account_request.jpg)

#### 4.4.15 Admin eszközök (szuperadmin funkciók)

A szuperadmin hozzáfér az admin eszközök képernyőhöz, ahol a szervezet összes adminisztratív funkcióját kezelheti.

![Admin eszközök](/home/garon/Desktop/.screenshots/admin_tools.jpg)

**Felhasználókezelés:** Az összes regisztrált felhasználó listája megtekinthető, és a felhasználók adatai módosíthatók.

![Felhasználókezelés](/home/garon/Desktop/.screenshots/user_management.jpg)

**Cimkekezelés (tag management):** A szuperadmin cimkéket hozhat létre, módosíthat és törölhet. A cimkék az üzenetek szűrhetőségét segítik.

![Cimkekezelés](/home/garon/Desktop/.screenshots/tag_management.jpg)

---

## 5. Fejlesztői dokumentáció

### 5.1 Fejlesztői környezetek

A projekt fejlesztéséhez a következő eszközöket használták:

**IntelliJ IDEA** (JetBrains) volt a fő fejlesztőkörnyezet. Az IDEA natívan támogatja a Kotlin Multiplatform projekteket, és a Compose Multiplatform UI fejlesztéséhez is kiváló eszközöket biztosít: az élő előnézet (live preview), a kódkiegészítés és a beépített Gradle integráció mind megkönnyítette a munkát.

**DataGrip** (JetBrains) az adatbázis-kezeléshez: a PostgreSQL sémák böngészéséhez, az adatok ellenőrzéséhez és a manuális SQL lekérdezések futtatásához.

Az Android teszteléshez fizikai Android eszközt, az iOS teszteléshez fizikai iOS eszközt, a webes teszteléshez Chrome és Firefox böngészőket, az asztali teszteléshez Linux és Windows operációs rendszereket használtak.

### 5.2 Technológiák

#### 5.2.1 Kotlin Multiplatform (KMP)

A **Kotlin Multiplatform** (verzió: 2.3.0) a projekt egyik sarokkövét adja. A KMP lehetővé teszi, hogy a Kotlin kód egyetlen forrásból forduljon le különböző célplatformokra: Android (JVM), iOS (Kotlin/Native), WebAssembly (Kotlin/Wasm), JavaScript (Kotlin/JS) és asztali alkalmazás (JVM). A közös kód a `commonMain` forráshalmazba kerül, a platformfüggő részeket pedig az `expect`/`actual` mechanizmus kezeli.

Az `expect` kulcsszóval egy absztrakt deklaráció adható meg a közös kódban, amelyre minden platform saját `actual` implementációt nyújt. Ez lehetővé teszi, hogy az alkalmazás logikája platformfüggetlen maradjon, miközben az alacsony szintű, platform-specifikus funkcionalitás (például a Firebase push token lekérése) natívan legyen megvalósítva.

#### 5.2.2 Compose Multiplatform

A **Compose Multiplatform** (verzió: 1.10.3) a JetBrains által fejlesztett UI keretrendszer, amely a Google Jetpack Compose-ra épül, és kiterjeszti azt az összes KMP célplatformra. Segítségével egyetlen Kotlin kódbázisból állítható össze az Android, iOS, web és desktop felhasználói felület. A keretrendszer a Material Design 3 komponenskönyvtárat is tartalmazza, amelyet a Vezérfonal UI-ja teljes egészében felhasznál.

A Compose Multiplatform deklaratív UI-paradigmát alkalmaz: az UI az alkalmazásállapot függvénye, és az állapot megváltozásakor a keretrendszer automatikusan újrarajzolja az érintett komponenseket.

#### 5.2.3 Ktor

A **Ktor** (verzió: 3.3.3) a JetBrains által fejlesztett, Kotlin-natív, aszinkron web keretrendszer, amelyet a projekt szerver és kliens oldalon egyaránt alkalmaz.

- **Ktor Server (Netty engine):** A backend API szervere, amely kezeli a HTTP és SSE végpontokat, a JWT autentikációt, a CORS beállításokat és a fájlkiszolgálást.
- **Ktor Client:** A kliensalkalmazás HTTP kommunikációs rétege. Minden platformon platform-specifikus motort (engine) használ (Android: OkHttp, iOS: Darwin, Desktop: CIO, Web: JS), de a kód platform-agnosztikus marad.

#### 5.2.4 Exposed ORM

A **Exposed** (verzió: 1.0.0-rc-4) a JetBrains Kotlin-natív SQL keretrendszere. A projekt a DSL API-t használja, amely típusbiztos, Kotlin idiomatikus módot biztosít SQL lekérdezések írásához. Az Exposed migration modulja automatikusan létrehozza és frissíti az adatbázissémákat a Kotlin tábladefiníciók alapján.

#### 5.2.5 PostgreSQL

A backend adatbázisa **PostgreSQL** (JDBC driver verzió: 42.7.8). A PostgreSQL kiválasztásának fő indoka a séma-szintű multi-tenant architektúra natív támogatása: minden szervezet adatai külön sémában helyezkednek el ugyanazon az adatbázispéldányon belül. A projekt egyedi PostgreSQL enum típusokat (`interaction_type`, `message_status`) is alkalmaz.

#### 5.2.6 Voyager Navigator

A **Voyager** (verzió: 1.1.0-beta03) egy Compose Multiplatform-kompatibilis navigációs könyvtár. Képernyőalapú (Screen) navigációt biztosít, ahol minden képernyő egy önálló Kotlin osztály, és a navigációs verem (stack) kezelése automatikus.

#### 5.2.7 Firebase Cloud Messaging

A **Firebase Cloud Messaging (FCM)** az Android push értesítések küldéséért felelős. A szerveren a Firebase Admin SDK (verzió: 9.7.1) multicast push küldést végez az összes regisztrált eszköz tokenéhez. A kliens oldalon a `gitlive:firebase-messaging` KMP könyvtár (verzió: 2.4.0) biztosítja az Androidon natív FCM integrációt.

#### 5.2.8 JWT (JSON Web Token)

Az autentikációhoz JSON Web Token-eket alkalmaz a rendszer (java-jwt könyvtár, verzió: 4.5.0). Az access tokenek 1 óra, a refresh tokenek 30 nap érvényességűek. A tokenek aláírása HMAC-256 algoritmussal történik. A tokenek adatbázisban is tárolódnak (SHA-256 hash-elt formában), így szerver oldalról visszavonhatók.

#### 5.2.9 BCrypt

A jelszavak tárolása **BCrypt** hashelési algoritmussal (cost factor: 12) történik (at.favre.lib:bcrypt, verzió: 0.10.2). Ez biztosítja, hogy a jelszavak szivárgás esetén sem legyenek visszafejthetők brute-force módszerrel.

#### 5.2.10 Resend és Thymeleaf

Az email küldés **Resend** (verzió: 3.1.0) API-n keresztül zajlik. Az email sablonok **Thymeleaf** (verzió: 3.1.3) sablonmotorral generálódnak HTML formátumban. Jelenleg a 2FA kód elküldése és a jelszócsere megerősítő kód küldése használja ezt az infrastruktúrát.

#### 5.2.11 Multiplatform Settings és KVault

A kliens oldalon az alkalmazásbeállítások és a JWT tokenek tartós tárolásához platform-specifikus megoldások szükségesek. A **multiplatform-settings** (verzió: 1.3.0) könyvtár egységes API-t biztosít a platformfüggő tárolási megoldások fölé (Android: SharedPreferences, iOS: NSUserDefaults, Desktop: Properties fájl, Web: LocalStorage). Az iOS Keychain eléréséhez a **KVault** (verzió: 1.12.0) könyvtárt alkalmazza a projekt.

#### 5.2.12 Kotlinx Serialization, Coroutines és DateTime

A **Kotlinx Serialization** (verzió: 1.9.0) JSON szerializációt/deszerializációt végez az összes DTO-ra, mind szerver, mind kliens oldalon. A **Kotlinx Coroutines** (verzió: 1.10.2) az aszinkron műveletek keretrendszere (API hívások, adatbázis-műveletek, SSE streaming). A **Kotlinx DateTime** (verzió: 0.7.1) platformfüggetlen dátum- és időkezelést biztosít.

#### 5.2.13 Összefoglaló táblázat

| Technológia | Verzió | Felhasználási terület |
|---|---|---|
| Kotlin | 2.3.0 | Teljes kódbázis |
| Compose Multiplatform | 1.10.3 | UI – minden platform |
| Ktor Server | 3.3.3 | Backend API |
| Ktor Client | 3.3.3 | HTTP kliens – minden platform |
| Exposed ORM | 1.0.0-rc-4 | Adatbázis-hozzáférés |
| PostgreSQL (JDBC) | 42.7.8 | Relációs adatbázis |
| Voyager | 1.1.0-beta03 | Navigáció |
| Firebase Admin SDK | 9.7.1 | Push értesítések (szerver) |
| Firebase Messaging (KMP) | 2.4.0 | Push értesítések (kliens) |
| java-jwt | 4.5.0 | JWT generálás és ellenőrzés |
| BCrypt | 0.10.2 | Jelszóhashelés |
| Resend | 3.1.0 | Email küldés |
| Thymeleaf | 3.1.3 | Email sablonok |
| Multiplatform Settings | 1.3.0 | Beállítások tárolása |
| KVault | 1.12.0 | iOS Keychain hozzáférés |
| Kotlinx Serialization | 1.9.0 | JSON szerializáció |
| Kotlinx Coroutines | 1.10.2 | Aszinkron programozás |
| Kotlinx DateTime | 0.7.1 | Dátum- és időkezelés |

### 5.3 Modulstruktúra

A projekt három fő modulból áll, amelyeket a Gradle build rendszer kezel:

```
vezerfonal/
├── shared/          ← Közös DTO-k, enumok, segédeszközök
├── server/          ← Ktor backend (csak JVM)
└── composeApp/      ← Teljes UI (minden platform)
```

A függőségi irányok egyértelműek és ciklikusmentesek:

```
server ──── depends on ────▶ shared
composeApp ── depends on ──▶ shared
```

A `server` soha nem importál a `composeApp`-ból, és fordítva: a `composeApp` soha nem importál a `server`-ből. A két modul kizárólag a `shared` modulon keresztül oszt meg kódot.

**`shared` modul** tartalmazza az összes Data Transfer Object-et (DTO), a felsorolás típusokat (enum) és a közös segédeszközöket, amelyeket mind a szerver, mind a kliens felhasznál. Minden DTO `@Serializable` annotációval van ellátva, és a Kotlinx Serialization segítségével JSON-ba és vissza szerializálható.

**`server` modul** a Ktor-alapú backend alkalmazást tartalmazza. Csak JVM 21-en fut, és teljes hozzáféréssel rendelkezik a PostgreSQL adatbázishoz, a Firebase Admin SDK-hoz és az email küldési infrastruktúrához.

**`composeApp` modul** tartalmazza a teljes felhasználói felületet. A közös UI kód a `commonMain` forráshalmazban van, a platformfüggő részek (HTTP motor, tokenek tárolása, push értesítés kezelése) az egyes platform-specifikus forráshalmazokban (`androidMain`, `iosMain`, `jvmMain`, `jsMain`, `wasmJsMain`).

### 5.4 Szerver architektúra

#### 5.4.1 Multi-tenant adatbázis-architektúra

A Vezérfonal szervere egy különleges multi-tenant adatbázis-architekturát alkalmaz, amely PostgreSQL **sémaszintű** elkülönítésen alapul. Ez azt jelenti, hogy egyetlen adatbázispéldányon belül minden szervezet adatai külön sémában helyezkednek el:

- `vezerfonal_main` séma: kizárólag a szervezetek metaadatait és a regisztrációs kódokat tárolja
- `vezerfonal_org_<szevezetnev>` sémák: minden szervezethez egy saját séma, amely tartalmazza az összes felhasználót, csoportot, üzenetet, JWT tokent stb.

Ez az architektúra biztosítja, hogy különböző szervezetek adatai nem kerülhetnek összekeveredésre, ugyanakkor egyetlen adatbázispéldányon kezelhető az összes szervezet, ami egyszerűsíti az üzemeltetést.

#### 5.4.2 Repository minta

A szerver adathozzáférési rétege repository mintát alkalmaz. Minden entitástípushoz (felhasználó, üzenet, csoport, tag, JWT stb.) külön repository osztály tartozik. A repository-k közvetlenül az Exposed DSL-t hívják, a tranzakciókezelés az Exposed `suspendTransaction()` függvényén keresztül történik, amely aszinkron, coroutine-kompatibilis végrehajtást biztosít.

#### 5.4.3 Route konvenciók

Az összes védett API végpont a `/api/*` prefix alá van szervezve, és JWT autentikációt igényel. Minden route handler a következő mintát követi:

```kotlin
val principal = call.principal<AuthResponse>()
    ?: return@get call.respond(HttpStatusCode.Unauthorized)
```

A szuperadmin-only műveletek esetén egy további ellenőrzés is szükséges:

```kotlin
if (!principal.user.isSuperAdmin)
    return@post call.respond(HttpStatusCode.Forbidden)
```

A request body deszerializálásához a `tryIncoming()` segédfüggvény áll rendelkezésre, amely deszerializálási hiba esetén automatikusan 400 Bad Request választ ad és `null`-t ad vissza. Az adatbázis-műveletek a `tryInternal()` segédfüggvénybe ágyazva futnak, amely kivétel esetén 500 Internal Server Error választ küld.

### 5.5 Adatbázis modell

#### 5.5.1 Főséma (vezerfonal_main)

**`organisations` tábla** – szervezetek nyilvántartása

| Oszlop | Típus | Leírás |
|---|---|---|
| id | INTEGER (PK) | Belső azonosító |
| display_name | VARCHAR | Szervezet neve |
| external_id | VARCHAR | Külső azonosító (16 karakter) |
| created_at | TIMESTAMPTZ | Létrehozás időpontja |
| updated_at | TIMESTAMPTZ | Utolsó módosítás időpontja |

**`registration_codes` tábla** – regisztrációs kódok

| Oszlop | Típus | Leírás |
|---|---|---|
| id | INTEGER (PK) | Belső azonosító |
| code | VARCHAR | A regisztrációs kód értéke |
| org_id | INTEGER (FK) | A szervezet azonosítója |
| created_at | TIMESTAMPTZ | Létrehozás időpontja |
| updated_at | TIMESTAMPTZ | Utolsó módosítás |

#### 5.5.2 Szervezeti séma (vezerfonal_org_<név>)

**`users` tábla** – felhasználói fiókok

| Oszlop | Típus | Leírás |
|---|---|---|
| id | INTEGER (PK) | Belső azonosító |
| email | VARCHAR | Email-cím (egyedi) |
| password_hash | VARCHAR | BCrypt jelszóhash |
| display_name | VARCHAR | Megjelenített név |
| profile_pic_uri | VARCHAR | Profilkép fájlneve |
| external_id | VARCHAR | Külső azonosító |
| is_super_admin | BOOLEAN | Szuperadmin-e |
| two_factor_enabled | BOOLEAN | 2FA be van-e kapcsolva |
| two_factor_code | VARCHAR | Aktuális 2FA kódhash |
| deletion_requested | BOOLEAN | Törlési kérelem beadva |
| created_at | TIMESTAMPTZ | Regisztráció időpontja |
| updated_at | TIMESTAMPTZ | Utolsó módosítás |

**`message` tábla** – üzenetek

| Oszlop | Típus | Leírás |
|---|---|---|
| id | INTEGER (PK) | Belső azonosító |
| title | VARCHAR | Üzenet tárgya |
| content | TEXT | Üzenet szövege |
| is_urgent | BOOLEAN | Sürgős-e |
| author_user_id | INTEGER (FK) | Küldő felhasználó |
| user_id | INTEGER (FK) | Egyéni címzett (null, ha csoportos) |
| group_id | INTEGER (FK) | Csoport-címzett (null, ha egyéni) |
| available_reactions | JSONB | Elérhető emoji reakciók listája |
| external_id | VARCHAR | Külső azonosító |
| created_at | TIMESTAMPTZ | Küldés időpontja |
| updated_at | TIMESTAMPTZ | Utolsó módosítás |

**`groups` tábla** – csoportok

| Oszlop | Típus | Leírás |
|---|---|---|
| id | INTEGER (PK) | Belső azonosító |
| display_name | VARCHAR | Csoport neve |
| description | TEXT | Leírás |
| group_admin_id | INTEGER (FK) | Csoport adminisztrátora |
| external_id | VARCHAR | Külső azonosító |
| is_internal | BOOLEAN | Rendszerbelső csoport-e |
| created_at | TIMESTAMPTZ | Létrehozás időpontja |
| updated_at | TIMESTAMPTZ | Utolsó módosítás |

**`user_group_connection` tábla** – csoport-tagság kapcsolótábla

| Oszlop | Típus | Leírás |
|---|---|---|
| user_id | INTEGER (FK) | Felhasználó azonosítója |
| group_id | INTEGER (FK) | Csoport azonosítója |

**`message_user_interactions` tábla** – reakciók, státuszok, nudge-ok, archiválások

| Oszlop | Típus | Leírás |
|---|---|---|
| id | INTEGER (PK) | Belső azonosító |
| type | interaction_type | Interakció típusa (status/reaction/nudge/archive) |
| status | message_status | Üzenet státusza (sent/received/read) |
| reaction | VARCHAR | Emoji reakció szövege |
| recipient_user_id | INTEGER (FK) | Érintett felhasználó |
| message_id | INTEGER (FK) | Érintett üzenet |
| created_at | TIMESTAMPTZ | Interakció időpontja |
| updated_at | TIMESTAMPTZ | Utolsó módosítás |

**`jwt` tábla** – kibocsátott JWT tokenek

| Oszlop | Típus | Leírás |
|---|---|---|
| id | VARCHAR (PK) | Token UUID azonosítója |
| token_hash | VARCHAR | SHA-256 hash a teljes token stringjéről |
| is_refresh | BOOLEAN | Refresh token-e |
| user_id | INTEGER (FK) | Tokenhez tartozó felhasználó |
| revoked | BOOLEAN | Visszavonva-e |
| expires_at | TIMESTAMPTZ | Lejárat időpontja |

**`message_tag` és `message_tag_connection` táblák** – cimkék és üzenet-cimke kapcsolat

**`push_tokens` tábla** – Firebase push értesítési tokenek

**`user_notification_settings` tábla** – értesítési beállítások felhasználónként

### 5.6 Kiemelt kódrészletek

#### 5.6.1 Multi-tenant adatbázis inicializálás

Az alábbi kódrészlet a `Database.kt` fájlból mutatja be, hogyan jön létre egy új szervezeti séma az első regisztráció alkalmával. A függvény ellenőrzi, hogy az adott szervezethez már létezik-e aktív adatbázis-kapcsolat (az `OrgDBs` map-ben), és ha igen, visszaadja azt. Ha nem, akkor létrehozza a sémát, inicializálja az egyedi PostgreSQL enum típusokat, majd az Exposed migration segítségével létrehozza az összes szükséges táblát.

```kotlin
suspend fun ensureOrgDB(name: String): Database? {
    val escapedName = name.filter { it.isLetter() }
    val schemaName = "vezerfonal_org_$escapedName"

    if (OrgDBs.containsKey(escapedName)) return OrgDBs[escapedName]

    try {
        val db = connect(schemaName)

        suspendTransaction(db) {
            exec("CREATE SCHEMA IF NOT EXISTS $schemaName;")

            // Custom enum típus: interaction_type (ha még nem létezik)
            exec(
                """SELECT 1 FROM pg_type t
                   JOIN pg_namespace n ON n.oid = t.typnamespace
                   WHERE t.typname = 'interaction_type'
                   AND n.nspname = current_schema()"""
            ) {
                if (!it.next())
                    exec("create type interaction_type as enum " +
                         "('status', 'reaction', 'nudge', 'archive')")
            }

            // Tábla-migráció futtatása
            val statements = MigrationUtils
                .statementsRequiredForDatabaseMigration(*orgTables)
            statements.forEach(::exec)
        }

        OrgDBs[escapedName] = db
        return db
    } catch (e: Exception) {
        System.err.println("Unable to connect to org DB: ${e.message}")
        return null
    }
}
```

Ez a megközelítés elegáns megoldást nyújt a multi-tenant architektúrára: nincs szükség sem több adatbázispéldányra, sem komplex partícionálásra, hiszen a PostgreSQL séma-mechanizmusa biztosítja a teljes adatszeparációt.

#### 5.6.2 JWT generálás adatbázis-alapú tokenkövetéssel

A `JWTConfig.kt` fájlból az alábbi kódrészlet mutatja a token generálási folyamatot. A tokenek nem csupán aláírásra kerülnek, hanem az adatbázisban is nyilvántartásra kerülnek – SHA-256 hash-elt formában. Ez lehetővé teszi a tokenek szerver oldalról történő visszavonását (például kijelentkezéskor), ami a hagyományos, kizárólag aláíráson alapuló JWT implementációkban nem lehetséges.

```kotlin
suspend fun generateToken(
    userExtId: String,
    db: Database,
    mainDB: Database,
    isRefresh: Boolean = false
): String {
    val tokenId = UUID.randomUUID().toString()
    val expiresAt = when (isRefresh) {
        false -> Date(now + ACCESS_TOKEN_VALIDITY_IN_MS)   // 1 óra
        true  -> Date(now + REFRESH_TOKEN_VALIDITY_IN_MS)  // 30 nap
    }

    val jwt = JWT.create()
        .withClaim("userExtId", userExtId)
        .withClaim("tokenId", tokenId)
        .withClaim("orgExtId", orgExtId)
        .withExpiresAt(expiresAt)
        .sign(Algorithm.HMAC256(SECRET))

    // Token SHA-256 hash-elt formában kerül az adatbázisba
    val success = with(JWTRepository(db)) {
        val inserted = insertJWT(JWTModel(
            id = tokenId,
            tokenHash = hashLongString(jwt),
            isRefresh = isRefresh,
            user = user,
            revoked = false,
            expiresAt = expiresAt.toKotlinInstant()
        ))
        if (inserted) true else invalidateAllTokensByUserId(user.id!!)
    }

    return if (success) jwt else error("Unable to insert token.")
}
```

Az `AuthResponse` principal tartalmazza a belépett felhasználó adatait, a szervezetspecifikus adatbázis-referenciát és a szervezet metaadatait – így minden route handler egyetlen objektumból el tudja érni az összes szükséges kontextust:

```kotlin
data class AuthResponse(
    val user: User,
    val db: Database,
    val org: Organisation,
    val rememberMe: Boolean = true
)
```

#### 5.6.3 Valós idejű üzenetközvetítés – MessageHub

A `MessageHub.kt` az egyik legérdekesebb szerver oldali komponens. Felelőssége kettős: egyrészt valós idejű üzenet-streamingre előfizetett kliensek értesítése Server-Sent Events (SSE) csatornákon keresztül, másrészt párhuzamos Firebase push értesítések küldése.

```kotlin
object MessageHub {
    private val userChannels =
        ConcurrentHashMap<Int, MutableList<Channel<MessageData>>>()

    fun subscribe(userId: Int): Channel<MessageData> =
        Channel<MessageData>(Channel.UNLIMITED).let { channel ->
            userChannels.compute(userId) { _, list ->
                (list ?: mutableListOf()).apply { add(channel) }
            }
            channel
        }

    private val sendSemaphore = Semaphore(32)

    suspend fun broadcast(message: Message, db: Database) = coroutineScope {
        val recipients = message.user?.let { listOf(it) }
            ?: message.group!!.members.map { it.user }

        recipients.map { user ->
            async {
                sendSemaphore.withPermit {
                    // SSE csatorna értesítése
                    val msg = message.fillMissingInformation(
                        db, user.id!!, user.isAnyAdmin == true
                    )
                    userChannels[user.id]?.forEach { it.trySend(msg) }

                    // Firebase push értesítés küldése
                    sendNotification(
                        trepo = PushTokenRepository(db),
                        userId = user.id,
                        data = NotificationData(
                            title = message.title,
                            notifType = NotificationType.Message,
                            data = mapOf("sender" to message.author.displayName)
                        )
                    )
                }
            }
        }.awaitAll()
    }
}
```

A `Semaphore(32)` korlátot állít fel arra, hogy egyszerre legfeljebb 32 párhuzamos értesítési műveletet lehessen futtatni, megakadályozva a szerver túlterhelését nagy csoportos küldések esetén. A `coroutineScope` és `async`/`awaitAll` kombináció biztosítja, hogy az összes értesítés párhuzamosan, de kontrolláltan fusson, és a `broadcast` csak akkor térjen vissza, ha az összes küldés befejeződött.

#### 5.6.4 Platform-specifikus implementáció – expect/actual

A `GetPushToken` az `expect`/`actual` mechanizmus szemléletes példája. A közös kódban csak egy deklaráció szerepel:

```kotlin
// commonMain/GetPushToken.kt
expect suspend fun getPushToken(): String?
```

Minden platform saját implementációt ad erre:

```kotlin
// androidMain/GetPushToken.android.kt
actual suspend fun getPushToken(): String? =
    Firebase.messaging.getToken()

// iosMain/GetPushToken.ios.kt
actual suspend fun getPushToken(): String? = null

// jvmMain/GetPushToken.jvm.kt
actual suspend fun getPushToken(): String? = null

// jsMain/GetPushToken.js.kt
actual suspend fun getPushToken(): String? = null

// wasmJsMain/GetPushToken.wasmJs.kt
actual suspend fun getPushToken(): String? = null
```

Az Androidon a Firebase messaging könyvtár valódi FCM tokent ad vissza. Minden más platformon `null` az eredmény, ami azt jelenti, hogy a push token regisztrációja azokon a platformokon nem kerül végrehajtásra. Ez a megoldás megakadályozza, hogy a Firebase dependency – amely csak Android esetén érhető el – egyéb platformokon fordítási hibát okozzon.

#### 5.6.5 Generikus kiválasztó komponens – reified típusparaméter

A `GeneralSelectionDialog` a Kotlin típusrendszerének erős kihasználására épülő megoldás. Egy generikus, `reified` típusparaméteres `inline` Composable függvény, amely egyetlen implementációból kezeli a felhasználó-, csoport- és tag-kiválasztó dialógusokat:

```kotlin
@Composable
internal inline fun <reified T : NamedDTO> GeneralSelectionDialog(
    snapshot: SelectionStateModel<T>,
    title: String,
    noinline onCancelClick: Function,
    onApplyClick: CallbackFunction<List<T>>,
    noinline prefixContent: (@Composable (T) -> Unit)? = null,
) {
    @Suppress("UNCHECKED_CAST")
    val state: SelectionStateController<T> = remember {
        when (snapshot) {
            is UserSelectionStateModel  -> UserSelectionStateController(snapshot)
            is GroupSelectionStateModel -> GroupSelectionStateController(snapshot)
            is TagSelectionStateModel   -> TagSelectionStateController(snapshot)
            else -> error("Invalid snapshot")
        } as SelectionStateController<T>
    }
    // ... UI megvalósítás
}
```

A `reified` kulcsszó lehetővé teszi, hogy a típusinformáció futásidőben is elérhető legyen az `inline` függvényen belül. A `when (snapshot)` ág ismert `SelectionStateModel<T>` altípusokra illeszkedik, és a megfelelő controller osztályt hozza létre. Ez a megközelítés kiküszöböli a háromfajta kiválasztó dialógus kódduplikációját anélkül, hogy reflexiót vagy kevésbé típusbiztos megoldásokat kellene alkalmazni.

### 5.7 A kliens architektúra

Az alkalmazás navigációját a **Voyager** könyvtár kezeli. Minden képernyő egy önálló Kotlin osztály, amely implementálja a `Screen` interfészt. A navigáció a `Navigator` composable-en keresztül, `navigator.push()` és `navigator.pop()` hívásokkal történik.

A `App.kt` gyökér composable három `CompositionLocal`-t állít fel, amelyek az egész alkalmazáson belül elérhetők:

- `LocalHttpClient`: a Ktor HTTP kliens példánya
- `LocalTokenStorage`: a JWT tokenek tárolásáért felelős, platform-specifikus tároló
- `LocalDarkModeState`: a sötét/világos módot kezelő állapot

A hálózati hívások közvetlen `suspend` függvényekként vannak megvalósítva a `network/api/` könyvtárban, repository vagy ViewModel réteg nélkül. A képernyők közvetlenül `rememberCoroutineScope().launch { }` blokkban hívják meg ezeket.

### 5.8 Tesztelés

#### 5.8.1 Automatizált tesztek

A projektben egy automatizált integrációs teszt található (`server/src/test/kotlin/ApplicationTest.kt`), amely az alapvető szerver-indítást és az API gyökérvégpontját ellenőrzi. Az integrációs tesztek Ktor beépített `testApplication` segítségével futnak, valódi adatbáziskapcsolat nélkül.

#### 5.8.2 Manuális tesztelés

A fejlesztők kiterjedt manuális tesztelést végeztek az összes célplatformon:

- **Desktop:** Linux (Ubuntu) és Windows 10/11 operációs rendszeren futtatva, JVM alapú asztali verzió
- **Web (WASM):** Google Chrome és Mozilla Firefox böngészőkben, a WebAssembly fordítású változattal
- **Web (JS):** Google Chrome böngészőben, a JavaScript fordítású visszaállási változattal
- **Android:** Fizikai Android eszközön, az alkalmazás telepítésével és valós használattal
- **iOS:** Fizikai iOS eszközön, Xcode segítségével telepítve és tesztelve

#### 5.8.3 Azonosított és javított hibák

A fejlesztési folyamat során – különösen 2026 februárjában – számos hiba azonosítása és javítása történt. Az alábbiakban a legjelentősebbek kerülnek bemutatásra.

---

**1. Push értesítés architektúra teljes átdolgozása**

A push értesítési rendszer első implementációja nem volt kellően robusztus: a coroutine scope kezelése hibás volt, a `NotificationData` osztály hiányzott, és az értesítési adatok átadása szétszórt paraméterezéssel történt.

*Javítás:* A `FirebasePushService` teljes újraírása, egységes `NotificationData` adatosztály bevezetése, a coroutine scope megfelelő kezelése, és az `onDestroy()` eseményen alapuló erőforrás-felszabadítás megvalósítása. A hibakezelés `runCatching`-re cserélve.

---

**2. Reakció-értesítés szövegkiválasztási logika inverziója**

Az emoji reakciókhoz tartozó push értesítés szövegének kiválasztásában egy logikai inverziós hiba volt: az `if (!data["extra"].isNullOrEmpty())` feltétel tévesen értékelte a feltételt, és a két esethez tartozó szöveget felcserélve adta vissza.

*Javítás:* A feltétel `if (data["extra"].isNullOrEmpty())`-re módosítva.

---

**3. Firebase push token csak Android platformon**

Egy platformkompatibilitási hiba volt abban, hogy a `firebase-messaging` KMP könyvtár importja a `commonMain` forráshalmazban szerepelt, holott a könyvtár csak Android platformon érhető el. Ez fordítási hibát okozott iOS, Desktop és Web platformokon.

*Javítás:* A könyvtár dependenciája áthelyezve a `commonMain`-ből az `androidMain`-be. Minden platformra önálló `expect`/`actual` implementáció készült: Androidon valódi FCM token lekérése, minden más platformon `null` visszaadása.

---

**4. Null push token regisztrációja**

Azokon a platformokon, ahol a `getPushToken()` `null`-t ad vissza (minden Android), a token regisztrációs API hívás mégis megtörtént, felesleges szerverhívásokat és potenciális hibákat okozva.

*Javítás:* A token regisztrációs hívás elé null-ellenőrzés került: `if (token != null) registerPushToken(token)`.

---

**5. CORS konfiguráció hiánya**

A szerver induláskor nem volt CORS konfiguráció beállítva. Emiatt a webes kliensek (böngészőben futó JS/WASM alkalmazások) nem tudtak kommunikálni a szerverrel, mivel a böngésző biztonsági okokból blokkolta a cross-origin kéréseket.

*Javítás:* A `ktor-server-cors` dependency hozzáadása és a `CORSConfig.kt` fájl létrehozása az engedélyezett originek, HTTP metódusok és fejlécek konfigurálásával.

---

**6. Belső csoportok megjelenése a csoportlistában**

A rendszer belső célokra is használ csoportokat (pl. az alapértelmezett `default` csoport). Ezek a belső csoportok tévesen megjelentek a felhasználók számára látható csoportlistában.

*Javítás:* A csoport lekérdezési logikában szűrő hozzáadása: `.filterNot { it.isInternal || it.displayName == "default" }`.

---

**7. Back navigáció gombkötés web platformon**

A webes változatban a `Backspace` billentyű vissza-navigációs eseményt váltott ki, ami ütközött a szövegbeviteli mezőkkel: gépelés közben véletlenszerűen lehetett visszanavigálni.

*Javítás:* A `Backspace` billentyű eltávolítása a navigációs billentyűkötések közül. A visszanavigáció az `Alt + Bal nyíl` kombinációra lett hagyva.

---

**8. Firebase konfigurációs fájl véletlenszerű commitolása**

A `composeApp/google-services.json` fájl – amely a Firebase projekt hitelesítési adatait tartalmazza – véletlenszerűen bekerült a repository-ba, érzékeny adatokat téve nyilvánossá.

*Javítás:* A fájl eltávolítása a repository-ból (`git rm`), és a `.gitignore` fájl frissítése, hogy a jövőben ez ne forduljon elő.

---

**9. Profilkép betöltési rendszer race condition-ok**

A profilkép lekérő és megjelenítő rendszer első változata komplex aszinkron/await minták alkalmazásával dolgozott, amelyek több platformon race condition-okat okoztak: a kép időnként nem töltődött be, vagy többször küldött felesleges hálózati kérést.

*Javítás:* A profilkép-betöltési logika egyszerűsítése és platform-specifikus, robusztus implementációk megírása. A `Util` segédfüggvények kiemelése, a kódkomplexitás csökkentése.

---

## 6. Összegzés

A Vezérfonal projekt fejlesztése sikeresen zárult: a kitűzött célok teljesültek, és az alkalmazás minden tervezett platformon (Android, iOS, Web WASM, Web JS, Desktop) működőképes állapotban van.

### 6.1 A célok teljesítése

Az eredeti projektterv célkitűzései nagyrészt megvalósultak:

- **Cross-platform elérhetőség:** ✓ Az alkalmazás Android, iOS, WebAssembly, JavaScript és Desktop (JVM) platformokon egyaránt fut, közös Compose Multiplatform kódbázisból kiindulva.
- **Célzott üzenetküldés:** ✓ Az adminisztrátorok üzeneteket küldhetnek egyedi felhasználóknak és csoportoknak, a többi felhasználó nem látja az üzenetet.
- **Olvasási visszajelzés:** ✓ A rendszer nyomon követi és megjeleníti, hogy az egyes felhasználók mikor olvasták el az üzeneteket (sent → received → read státuszfolyam).
- **Emlékeztető (nudge) funkció:** ✓ Az adminisztrátorok emlékeztetőt küldhetnek a nem olvasott üzenetekhez.
- **Emoji reakciók:** ✓ A felhasználók az admin által meghatározott emoji-készlettel reagálhatnak.
- **Admin hierarchia:** ✓ A rendszer megkülönbözteti a szuperadmin, csoportadmin és sima felhasználói szerepköröket, és ezekhez eltérő funkciókat rendel.
- **Valós idejű értesítések:** ✓ Server-Sent Events alapú valós idejű szinkronizáció, Firebase push értesítések Android platformon.
- **Biztonságos autentikáció:** ✓ JWT-alapú belépés, BCrypt jelszóhashelés, opcionális kétfaktoros azonosítás (2FA) email kódküldéssel.
- **Szervezeti adatelkülönítés:** ✓ PostgreSQL séma-szintű multi-tenant architektúra.

### 6.2 Tapasztalatok és tanulságok

A projekt számos értékes tapasztalattal gazdagította a fejlesztőket.

**Technológiai tapasztalatok:** A Kotlin Multiplatform és a Compose Multiplatform valóban lehetővé teszi az egy kódbázisból épített, több platformra szóló alkalmazás fejlesztését. Ugyanakkor ez az ígéret technológiai kompromisszumokkal jár: a WebAssembly fordítás és az iOS natív integráció számos platformspecifikus részletet igényel, amelyekre fel kell készülni. Az `expect`/`actual` mechanizmus elegáns megoldást nyújt ezekre a különbségekre, de a platform-specifikus edge case-ek azonosítása és kezelése jelentős fejlesztési időt vesz igénybe.

**Szervezési tapasztalatok:** Visszatekintve a fejlesztési folyamatra, az egyik legfontosabb tanulság az időgazdálkodással kapcsolatos. A határidők közelében tapasztalt nyomás több alkalommal is oda vezetett, hogy egyes rendszerek félkész állapotban kerültek be a kódbázisba. Ezeket utólag újra kellett tervezni és implementálni, ami többletmunkát okozott. Jobb lett volna több időt szánni az előzetes tervezésre és kutatásra, és explicit feladatbontással, reálisabb becslésekkel dolgozni.

**Csapatmunka tapasztalatai:** A kétfős csapat mérete jól illeszkedett a projekt komplexitásához: a kommunikáció gyors és közvetlen volt, a felelősségi körök egyértelműek. Balogh Márk sikeresen elsajátította a Kotlin és Compose Multiplatform technológiákat a projekt során, ami egyszerre volt kihívás és eredmény.

---

## 7. Továbbfejlesztési lehetőségek

### 7.1 Éles üzemi elérhetőség

A projekt jelenleg éles üzemeltetés felé tart: a `vezerfonal.org` domain regisztrálva van, és a szerver telepítése folyamatban van. Az éles indítás után a cél az, hogy valódi szervezetek számára is elérhetővé váljon az alkalmazás.

### 7.2 Piaci visszajelzés és iparági validáció

A közeli jövőbeli tervek között szerepel az, hogy a fejlesztők különböző cégeket és szervezeteket keresnek fel az alkalmazással, és visszajelzéseket gyűjtenek arról, hogy milyen funkciók lennének értékesek, milyen igényeket nem fed le a jelenlegi rendszer, és milyen irányba érdemes fejleszteni. Ez az iparági validáció elengedhetetlen ahhoz, hogy a fejlesztési erőforrások a valódi felhasználói igényekre összpontosuljanak.

### 7.3 Technikai továbbfejlesztési irányok

A kódbázis elemzése és a fejlesztési folyamat tanulságai alapján számos konkrét technikai továbbfejlesztési lehetőség azonosítható:

**Apple és Google OAuth bejelentkezés:** A kódbázisban már megtalálható az OAuth konfiguráció (`OAuthConfig.kt`, `UserOAuthProvider` tábla) és az OAuth provider modell, de a tényleges bejelentkezési folyamat még nincs teljesen implementálva. Az Apple és Google OAuth integráció befejezése jelentősen egyszerűsítené a regisztrációt és a bejelentkezést.

**Web push értesítések:** Jelenleg a push értesítési rendszer kizárólag Android platformon működik Firebase Cloud Messaging segítségével. A Web Push API (browsers) és az iOS APNs integráció megvalósításával a valós idejű értesítések kiterjednének az összes platformra.

**Fájlmellékletek üzenetekhez:** A jelenlegi implementáció kizárólag szöveges üzeneteket támogat. Képek, dokumentumok és egyéb fájltípusok mellékelhetőségének hozzáadása jelentősen növelné az alkalmazás hasznosságát, különösen oktatási és vállalati kontextusban.

**Csoporthierarchia:** Jelenleg a csoportok egyszintűek. Alcsoportok (alcsoportok) bevezetése lehetővé tenné a nagyobb szervezetek hierarchikus struktúrájának modellezését.

**Audit napló adminoknak:** Egy szervezeti szintű napló (audit log), amely rögzíti a fontosabb adminisztratív eseményeket (felhasználó létrehozása, törlése, csoport módosítása), növelné az átláthatóságot és a visszakövethetőséget.

**Teljes szöveges keresés az üzenetekben:** Az üzenetek jelenleg cimkék és státusz alapján szűrhetők. Tartalom szerinti szöveges keresés bevezetése – különösen nagyobb mennyiségű üzenet esetén – fontos kényelmi funkció lenne.

**Értesítési beállítások finomhangolása:** A jelenlegi rendszerben az értesítési beállítások kezelése az operációs rendszer szintjén történik. Alkalmazásszintű értesítési beállítások (pl. mely cimkéjű üzenetekhez kérjen értesítést a felhasználó) bevezetése testre szabhatóbbá tenné a rendszert.

---

## 8. Irodalomjegyzék

A projekt fejlesztése során az alábbi forrásokat vettük igénybe:

**Hivatalos dokumentációk:**

1. Kotlin programozási nyelv és Kotlin Multiplatform – JetBrains hivatalos dokumentáció. Elérhető: https://kotlinlang.org

2. Ktor szerver és kliens keretrendszer – JetBrains hivatalos dokumentáció. Elérhető: https://ktor.io

3. Android fejlesztői útmutatók és codelabs – Google Android dokumentáció. Elérhető: https://developer.android.com

**Könyvtárak és keretrendszerek forráskódja és dokumentációja (GitHub):**

4. Exposed ORM – JetBrains. Elérhető: https://github.com/JetBrains/Exposed

5. Compose Multiplatform – JetBrains. Elérhető: https://github.com/JetBrains/compose-multiplatform

6. Voyager Navigator – Adriel Café. Elérhető: https://github.com/adrielcafe/voyager

7. Firebase Admin Java SDK – Google. Elérhető: https://github.com/firebase/firebase-admin-java

8. gitlive Firebase KMP – GitLive. Elérhető: https://github.com/GitLiveApp/firebase-kotlin-sdk

9. KVault – Liftric. Elérhető: https://github.com/Liftric/KVault

10. Multiplatform Settings – Russell Wolf. Elérhető: https://github.com/russhwolf/multiplatform-settings

11. BCrypt – Favre. Elérhető: https://github.com/patrickfav/bcrypt

12. Resend Java SDK – Resend. Elérhető: https://github.com/resend/resend-java
