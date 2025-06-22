## TCP

- połączeniowy - wymaga ustanowienia połączenia (three-way handshake - SYN, SYN-ACK, ACK)
- niezawodny - gwarantuje dostarczenie danych w odpowiedniej kolejności
- strumieniowy - dane są przesyłane jako strumień bajtów
- kontrola przepływu - mechanizmu zapobiegające przeciążeniu odbiorcy
- zastosowania: strony www (protokół HTTP/HTTPS), zdalny dostęp (SSH), przesyłanie plików (FTP), poczta (SMTP)

## UDP

- bezpołączeniowy
- zawodny
- datagramowy - dane przesyłane są w pakietach
- brak kontroli przepływu - mały narzut na dane
- możliwy multicast/broadcast
- zastosowania: transmisje multimedialne, gry online, dns

## Asocjacja

Asocjacja jest to piątka (protokół, adres IP lokalny, port lokalny, adres IP obcy, port obcy)

Port
- dobrze znane [0, 1023]
- efemeryczne - przydzielane dynamicznie na czas połączenia [2^14+2^15, 2^16-1]

Gniazdo (Socket) - punkt końcowy w komunikacji
- protokół (TCP/UDP)
- adres IP lokalny lub obcy
- port lokalny lub obcy

Gniazd warto używać, kiedy chcemy mieć pełną kontrolę nad protokołem (własny format danych, wybór endianness, kodowanie znaków) oraz gdy zależy nam na wydajności i niskich opóźnieniach. 

## Reprezentacja danych

- Big endian - najbardziej znaczący bajt na początku (tego używamy w sieciach)
- Little endian - najmniej znaczący bajt na początku

## Kodowanie znaków

- ASCII - mapowanie liczb na znaki 
- UTF-8 - obsługa znaków międzynarodowych

## Serializacja

Serializacja to proces przekształcania struktur danych do postaci bajtowej (np. JSON, XML, binary)

## Pytania z kolokwiów

### Co w aplikacji determinuje wybór takiego a nie innego interfejsu (kiedy TCP a kiedy UDP).

Protokołu TCP używamy kiedy zależy nam na nawiązaniu połączenia pomiędzy aplikacjami (three-way handshake) i chcemy mieć pewność, że każdy pakiet zostanie dostarczony, także w odpowiedniej kolejności. TCP ma mechanizmy kontroli przepływu, które mogą być pomocne w dostosowywaniu prędkości wysyłania danych do infrastruktury oraz możliwości odbiorcy. 

Z protokołu UDP korzystamy kiedy nie zależy nam na kompletności danych (streaming, gry wideo, wideo on demand), gdzie zgubiony datagram nie ma wpływu na działanie aplikacji. UDP obsługuje transmisję multicast i broadcast, a TCP tylko unicast. 

### Definicje

Asocjacja to piątka (protokół, adres źródłowy, port źródłowy, adres docelowy, port docelowy)

Porty efemeryczna to porty przydzielane tylko na potrzeby chwilowej komunikacji w zakresie [2^14+2^15, 2^16-1]

Port dobrze znane to ustandaryzowane numery portów dla konkretnych usług na swiecie. Przykładem może być HTTP (80), HTTPS (443), SSH (22).

### Gniazda BSD

Gniazdo to trójka (protokół, adres źródłowy, port źródłowy), to punkt końcowy komunikacji pomiędzy aplikacjami.

Główne metody: 
- socket() - tworzy punkt końcowy komunikacji i zwraca desktyptor pliku odnoszący się do tego punktu
- bind() - przypisuje adres lokalny do gniazda
- close() - zamyka istniejące gniazdo i zwalnia deskryptopr
- listen() - ustawia gniazdo w tryb nasłuchiwania
- accept() - akceptuje połączenie z kolejki nasłuchującej
- connect() - ustawnawia połączenie pomiędzy punktami końcowymi na gniazdach
- send() - wysyła dane przez gniazdo
- recv() - odbiera dane przychodzące z kolejki
- getsockname() - pobiera lokalny adres gniazda
- poll() - sprawdza stan gniazda testując czy można do niego pisać i z niego czytać