## Zookeeper

Apache ZooKeeper to rozproszony system koordynacji zapewniający niezawodne zarządzanie dużymi klastrami aplikacji. Oferuje mechanizmu synchronizacji, zarządzania konfiguracją i wyboru lidera, kluczowe dla utrzymania spójności w systemach rozproszonych. 

## Architektura

Hierarhiczna struktura danych oparta na węzłach (znode'ach), gdzie kazdy węzeł może miec potomków

## Typy znode'ów

- Persystentne - istnieją dopóki ręcznie sie ich nie usunie
- Efemeryczne - automatycznie usuwanie po zakończeniu sesji klienta, nie mogą mieć potomków
- Sekwencyjne - z automatycznie generowanym numerem przyrostowym w nazwie

## Mechanizmy koordynacji

- Watchers - jednorazowe powiadomienia o zmianach w strukturze znode'ów. Wymagają ponownej rejestracji po aktywacji

## Elekcja lidera

- Serwery tworzą efemeryczne znode'y sekwencyjne w ścieżce `/svc/election-path`
- Węzeł z najniższym numerem sekwencyjnym staje sie liderem
- Pozostałe węzły obserwują poprzednika w sekwencji, przejmując role lidera przy jego awarii

## Różnica pomiędzy liderem i uczestnikiem

Tryb lidera (Leader mode):
- Węzeł, który zostanie wybrany jako lider, jest odpowiedzialny za
przyjmowanie żądań zapisu od klientów
- Klient wysyła żądanie zapisu.
- Lider przetwarza żądanie zapisu, generuje unikalny numer sekwencyjny dla
danego zapisu i rozgłasza żądanie do pozostałych węzłów w klastrze.

Tryb uczestnika (Follower mode):
- Uczestnicy to pozostałe węzły w klastrze, które podążają za liderem i
odbierają od niego rozgłaszane żądania zapisu.
- Uczestnik musi potwierdzić, że odebrał i zapisuje dane w odpowiedniej
kolejności.

## ZAB - ZooKeeper Atomic Broadcast

ZAB gwarantuje atomowość i sekwencyjność operacji zapisu poprzez ścisłą kontrole kolejności zmian. 

1. Propagacja żądania - klient wysyła zapis do dowolnego węzła, który przekazuje go liderowi.
2. Zapis u lidera - Lider loguje transakcję i rozsyła do followerów, którzy logują ją w swoich logach i odsyłają ACK do lidera. 
3. Potwierdzenie kworum - Wymagana akceptacja od większości węzłów.
4. Finalizacja - Lider zatwierdza operację i powiadamia klienta. 

Wszystkie aktualizacje są widoczne w tej samej kolejności dla wszystkich klientów i potwierdzone zmiany przetrwają restart systemu. 

## Zastosowania

- Zarządzanie stanem usług w systemach big data typu Kafka i Hadoop. 
- Koordynacja usług w architekturze mikroserwisów. 
- Implementacja rozproszonych blokad.
- Przechowywanie konfiguracji klastra dostępnej dla wszystkich komponentów. 

## Jakie są gwarancje konsystentności Zookeepera?

- sekwencyjność - aktualizacje są wykonywane w odpowiedniej kolejności 
- atomowość - aktualizacje są wykonywane w całości, albo wycofywane
- single system image - klient widzi taki sam obraz systemu, niezależnie od tego do którego serwisu jest podłączony
- niezawodność - aktualizacje są stałe, dopóki ktoś ich nie zmieni
- timeliness  - widok systemu dla klienta jest zawsze aktualny po danym przedziale czasu
- zookeeper nie gwarantuje, że każdy klient będzie miał taki sam obraz systemu w tym samym czasie. 

## Pytania z kolokwiów

### Opisz znode
Znode służą jako rejestry danych w hierarchicznej strukturze przypominającej system plików, umożliwiając przechowywanie i zarządzanie danymi w systemach rozproszonych. 

Znode to każdy węzeł w drzewie Zookeeper, który stanowi główną abstrakcję programistyczną w tym systemie. Każdy znode jest identyfikowany przez unikalną ścieżkę, gdzie komponenty oddzielone są znakiem "/".

Wyróżniamy 3 typy znodeów: 

- persystentne - zostają dopóki się ich manualnie nie usunie
- efemeryczne - są usuwane po zakończeniu sesji, nie mogą mieć potomków
- sekwencyjne - dostają losowy identyfikator przyrostowy w nazwie

Każdy znode utrzymuje strukturę stat, która zawiera numery wersji dla zmian danych i ACL oraz znaczniki czasowe. Numer wersji zwiększa się za każdym razem gdy dane w znode ulegną zmianie, co pozwala ZooKeeperowi na walidację cache i koordynację aktualizacji.

Dane przechowywane w każdym znode są odczytywane i zapisywane atomowo, każdy odczyt odczytuje wszystkie dane, a zapis nadpisuje wszystkie dane. Każdy węzeł posiada ACL, który ogranicza dostęp do operacji.

Klienci mogą używać obserwatorów (watchers) na znode'ach, i kiedy nastąpi zmiana w znode to dostaną o tym powiadomienie. Jest to jednorazowe i po otrzymaniu powiadomienia trzeba na nowo zarejestrować obserwatora. 

Każdy znode może przechowywać maksymalnie 1MB danych. 

### Podaj przykładowy scenariusz wykorzystania ZooKeeper w systemie.

Przykładowy scenariusz wykorzystania ZooKeeper to system zarządzania konfiguracją w klastrze Apache Kafka, gdzie ZooKeeper pełni rolę centralnego koordynatora dla rozproszonych brokerów. 

W tym scenariuszu ZooKeeper zarządza metadanymi Kafka, przeprowadza wybory lidera brokerów i śledzi offsety konsumentów. Gdy nowy broker dołącza do klastra automatycznie rejestruje się w ZooKeeper jako węzeł efemeryczny. Jeżeli broker przestaje działać to zostaje automatycznie usunięty, a inne znode'y dostają powiadomienie o zmianie. 

Dzięki takiemu rozwiązaniu konfiguracja klastra jest przechowywana centralnie i propagowana do wszystkich brokerów przy zmianach

### Podaj wymagania aplikacji do wykorzystania ZooKeepera.

Architektura musi być zaprojektowana do pracy z ZooKeeperem składającym się z nieparzystej liczby serwerów aby zapewnić podejmowanie decyzji w oparciu o kworum. 

Aplikacja musi być przygotowana na rozłączanie sesji klienta, gdy serwer ZooKeeper nie może połączyć się z kworum dłużej niż skonfigurowany timeout. Musi implementować mechanizmu ponownego łączenia i odtwarzania stanu. 

Aplikacja powinna wykorzystywać wszystkie trzy typy znodeów. 

Aplikacja musi implementować mechanizm obserwatorów do reagowania na zmiany w czasie rzeczywistym. Po wyzwoleniu obserwatora aplikacja powinna go ponownie ustawić, żeby kontynuować monitorowanie systemu. 

Aplikacja musi być przygotowana na automatyczną reelekcje lidera w przypadku awarii. 

Aplikacja musi respektować limit 1MB danych na Znode. 

Aplikacja powinna implementować odpowiednie listy kontroli dostępu ACL dla zabezpieczenia danych wrażliwych. 

Aplikacja powinna działać asynchronicznie