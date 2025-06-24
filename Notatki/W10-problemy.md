## Synchronizacja czasu

### Zegary fizyczne

Niedokładność zegarów wymaga synchronizacji. Protokół NTP (network time protocol) służy do synchronizacji z czasem UTC w hierarchicznej strukturze. Algorytmy korekcji (Cristiana, Berkeleya) uwzględniają opóźnienia sieciowe. 

### Zegary logiczne

Zegar Lamporta porządkuje zdarzenia relacją happened before. Każde zdarzenie zwiększa licznik, a przy wysyłaniu wiadomości dołączany jest aktualny stan zegaru, przez co odbiorca może skorygować swój zegar. 

### Zegary wektorowe

Każdy proces ma wektor liczników dla wszystkich węzłów które mogą być aktualizowane przez porównanie wektorów przy odbiorze wiadomości.

## Globalny stan systemu 

### Snapshot

Algorytm wykorzystuje markery do zbierania stanów lokalny i wiadomości w drodze. Proces inicjujący wysyła markery, a każdy proces zapisuje swój stan po otrzymaniu markera i przekazuje go dalej. Stan jest spójny, jeżeli nie narusza relacji przyczynowo-skutkowych. 

### Wykrywanie zakończenia obliczeń

Używa markerów Done i Continue. Procesy propagują status obliczeń w hierarchii, gdzie odpowiedź Done od wszystkich oznacza koniec obliczeń. 

## Elekcja lidera

- bully - procesy o wyższych ID blokują kandydatury niższych. Zwycięzca o najwyższym ID ogłasza się liderem. Złożoność n^2.
- pierścieniowy - wiadomość election krąży w pierścieniu, proces o najwyższym ID zatrzymuje ją i wysyła announcement

## Synchronizacja dostępu do zasobów

- centralizowany - jeden koordynator zarządza kolejką żądań, z wad to jest to pojedynczy punkt awarii
- ricarta i agrawali - wymaga zgody wszystkich procesów na wejście do sekcji krytycznej. Używa znaczników czasowych do ustalenia priorytetów
- token ring - token krązy w pierścieniu, proces z tokenem może wejść do sekcji krytycznej. 