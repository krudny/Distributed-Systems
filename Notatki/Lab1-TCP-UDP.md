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