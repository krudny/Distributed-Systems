## CAP

Twierdzenie CAP, sformułowane przez Erica Brewera w 2000 roku stanowi fundamentalną zasadę projektowania systemów rozproszonych. Mówi ono, żę w przypadku awarii, system może jednocześnie zapewniść tylko dwie z trzech kluczowych właściwości. 

## spójność (Consistency)

Wszystkie węzły systemu widzą te same dane w tym samym momencie. Każdy odczyt zwraca najnowszą wersję danych lub błąd. 

Przykład: Saldo w banku zawsze będzie takie samo, niezależnie skąd je odczytujemy.

## dostępność (Availability)

System zawsze odpowiada na żądania, nawet jeśli część węzłów jest niedostępna. Odpowiedź może zawierać jednak nieaktualne dane.

Przykład: Można odświeżać portale społecznościowe i dostawać stare dane, ale portal jest dostępny. 

## tolerancję na podziały (Partition Tolerance)

System kontynuuje działanie pomimo utraty komunikacji między węzłami. Jest to wymóg obowiązkowy w rzeczywistych systemach rozproszonych, gdzie awarie sieci są nieuniknione. 

## Kompromisy w praktyce

![alt text](image.png)

Twierdzenie CAP pozostaje kluczowym narzędziem przy wyborze architektury systemu, wymuszając świadome kompromisy między niezawodnością a wydajnością. Jego zrozumienie jest szczególnie istotne w erze chmur obliczeniowych i rozproszonych systemów big data.