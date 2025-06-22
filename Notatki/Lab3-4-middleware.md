## Technologie middleware

Technologie middleware to rodzaj oprogramowania który pełni rolę warstwy pośredniczącej pomiędzy różnymi aplikacjami. Jego głównym zadaniem jest umożliwienie komunikacji, wymiany danych oraz integracji pomiędzy systemami. 

## Rodzaje middleware

- obiektowe (CORBA, ICE, RMI) - umożliwiają komunikację pomiędzy obiektami w różnych językach i platformach
- wiadomościowe (RabbitMQ) - opiera się na przesyłaniu wiadomości
- Remote Procedure Call (gRPC, Thrift) - pozwalają na wywołanie procedur na odległych maszynach

## Rodzaje serializacji danych

- tekstowa (JSON) - czytelna dla ludzi, mniej wydajna
- binarna - wymaga kontraktu na format danych pomiędzy aplikacjami, wydajniejsza

## CORBA

- Umożliwia komunikację między aplikacjami w różnych językach.
- Centralnym elementem jest ORB (Object Request Broker) który pośredniczy w komunikacji. 
- Interfejsy definiowane są w IDL (Interface Definition Language), z którego generowane są stuby dla klienta i skeletony dla serwera.
- Proces komunikacji obejmuje marshalling (serializację) i unmarshalling (deserializację) danych.

## ICE

- Middleware podobny do CORBY, ale nowocześniejszy
- Używa własnego języka do definiowania interfejsów - SLICE
- Różne protokoły transportowe: TCP, UDP, SSL/TCP, WebSocket
- Zapewnia sematykę wywołań co najwyzej raz (ICE nie powtarza żądań, nawet jak się nie wykona)

## Serwanty
Serwant odnosi się do obiektów po stornie serwera, które implementują logikę dla zdalnych wywołań. Serwant jest instancją klasy napisanej przez programistę która realizuje operacje zadeklarowane w interfejsie Slice.

Zarządzanie serwantami: proste mapowanie, default servant, servant locator, servant evictor. 

## Thrift
- Warstwowa architektura umożliwiająca wybór implementacji transportu i protokołu serializacji
- Obsługuje różne rodzaje transportu (TSocket, TFramedTransport, TMemoryTransport)
- Pozwala na uruchomienie wielu usług na jednym serwerze

## gRPC

- oparty na Protocol Buffers (efektywna serializacja binarna)
- komunikacja po HTTP/2, wsparcie dla strumieniowania, kontrola przepływu
- usługowe podejście, nie jest obiektowym middleware
- gRPC-Web umożliwia korzystanie z gRPC w przeglądarkach z użyciem reverse-proxy

## Pojęcia

- stub - kod po stronie klienta ukrywający szczegóły wywołań zdalnych
- skeleton - kod po stronie serwera, przyjmujący wywołania i przekazujący je do właściwej implementacji
- serwant - implementacja obiektu po stronie serwera
- object adapter - mapuje identyfikatory obiektów na serwanty
- Active Servant Map - mapa powiązań obiekt-serwant

## Pytania z kolokwiów

### Co to jest obiekt, serwant, serwer i jakie są między nimi relacje?

Obiekt w middleware to abstrakcja reprezentująca funkcjonalność dostępną zdalnie. 

Serwant to implementacja obiektu po stronie serwera. To konkretny kod który wykonuje operacje zdefiniowane w interfejsie obiektu.

Serwer to proces lub aplikacja hostująca serwantów i udostępniająca ich funkcjonalności klientom przez middleware.

### Co to jest przeźroczystość i jakie są jej rodzaje?

Przeźroczystość to właściwość systemu rozproszonego polegająca na ukrywaniu przed użytkownikami faktu rozproszenia. 

Wyróżniamy:
- przeźroczystość dostępu - ujednolicanie metod dostępu do danych i ukrywanie różnic w reprezentacji
- przeźroczystość położenia - ukrywanie fizycznego położenia zasobów
- przeźroczystość wędrówki - możliwość przenoszenia zasobów bez zmiany sposobu odwoływania się do nich
- przeźroczystość zwielokrotnienia - ukrywanie faktu replikacji zasobów
- przeźroczystość awarii - maskowanie awarii komponentów

### Wyjaśnij skróty Stub, skeleton i DII. 

Stub to obiekt po stronie klienta, który ukrywa szczegóły komunikacji sieciowej i serializacji parametrów prezentując prosty mechanizm wywołania metod. 

Skeleton jest po stronie serwera odpowiedzialny za odbieranie żądań, deserializację oraz przekazywanie do odpowiedniego serwanta. 

DII - dynamic invocation interface to mechanizm umożliwiający dynamiczne wywołanie metod na obiektach bez wcześniejszej znajomości ich interfejsó.

### Co to jest IDL, po co go kompilujemy. 

IDL to język w którym definiuje się interfejsy potrzebne do komunikacji w systemach rozproszonych z użyciem middleware. Kompilacja IDL służy do generowania kodu w konkretnych językach programowania. W wyniku kompilacji powstają stuby po stronie klienta, skeletony po stronie serwera, klasy pomocnicze do serializacji i deserializacji danych i interfejsy w docelowym języku programowania. 

### Co to ORB?

ORB - object request broker - to centralny komponent systemu middleware odpowiedzialny za zarządzanie komunikacją między rozproszonymi obiektami. Obsługuje tworzenie obiektów, wywoływanie metod, serializaje i deserializację danych oraz zapewnia przeźroczystość położenia. 

### Co to ASM?

ASM - active servant map - to tabela lookup utrzymującą mapowanie między obiektami a serwantami. To podstawowa struktura danych w object adapterze która pozwala na efektywne lokalizowanie serwantów dla przychodzących żądań. 

### Co to Servant Locator?

Servant Locator to lokalny obiekt implementowany przez programistę dołączany do object adaptera. Gdy adapter ma servant locator, konsultuje swój ASM w celu zlokalizowania serwanta dla obsługi przychodzącego żądania. Jeżeli serwant nie zostanie znaleziony w ASM, object adapter wywołuje servant locator, aby zapewnił serwanta dla żądania. Dla okreslonej kategorii można zarejestrować tylko jednego servant locatora.

### Co to Servant Evictor?

Servant Evictor to wyspecjalizowana forma servant locatora która implementuje mechanizm cache z algorytmem LRU. Evictor utrzymuje kolejkę serwantów w porządku LRU i automatycznie usuwa najdłużej nieużywane serwanty. 

### Co to komunikacja dwukierunkowa? 

Komunikacja dwukierunkowa w ICE pozwala na przeływ żądań w obu kierunkach przez to samo połączenie umożliwiając serwerowu wysyłanie żądań callback do klienta przez istniejące połączenie.