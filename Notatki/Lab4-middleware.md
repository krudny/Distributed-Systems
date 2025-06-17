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

