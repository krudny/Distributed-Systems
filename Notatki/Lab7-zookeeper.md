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