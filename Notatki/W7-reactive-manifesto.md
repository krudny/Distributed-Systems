## Reactive manifesto

Reactive manifesto to manifekt który definiuje architekturę systemów reaktywnych ktore skutecznie działają w środowiskach rozproszonych i obciążonych. 
Składa się z 4 filarów. 

## Responsywność 

System reaguje w przewidywalnym czasie, nawet przy dużym obciążeniu. 

## Odporność

Awaria komponentu nie wpływa na cały system. 

Techniki
- izolacja błędów
- samoleczenie (automatyczny restart części systemu)
- circuit breaker

## Elastyczność

System dynamicznie skaluje zasoby w reakcji na obciążenie. 

Strategie: 
- scale-out (dodawanie instacji, lub ulepszanie obecnych)
- serverless 

## Komunikacja przez wiadomości

Komponenty komunikują się asynchronicznie za pomocą wiadomości z kontrolą przepływu (back-pressure). 

Back-pressure - odbiorca kontroluje tempo przepływu, tak żeby serwer nie produkował danych których odbiorca nie jest w stanie przetworzyć. 

## Java Reactive Streams API

Komponenty
- Publisher - źródło danych
- Subscriber - odbiorca z możliwością żądania danych
- Subscribtion - mechanizm negocjacji przepustowości 
- Processor - połączenie publishera i subscribera

## Event-Driven vs Message-Driven

Architektura event-driven polega na komunikacji przez emitowanie zdarzeń które reprezentują zmianę stanu lub zaistnienie jakiegoś faktu, zazwyczaj nie wymagają żadnej reakcji odbiorcy. Zdarzenia są publikowane do wszystkich zainteresowanych odbiorców, bez określenia kto je odbierze. Przykładem może być broker w `Kafka`, powiadomienia o nowych zamówieniach, zmiana statusu użytkownika. 

Architektura message-driven polega na komunikacji przez wysyłanie wiadomości do konkretnych odbiorców. Odbiorca przetwarza wiadomość i często potwierdza odbór, lub wykonuję jakąś akcje. Przykładem może być użycie kolejek w `RabbitMQ`, przetwarzanie płatności, systemy kolejkowania zadań, systemy wymagające niezawodności, potwierdzeń i kontroli przepływu. 