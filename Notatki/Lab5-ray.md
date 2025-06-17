## Model aktorowy

Aktorzy to podstawowe jednostki przetwarzania współbieżnego posiadające własną kolejkę wiadomości, zachowanie oraz izolowany stan. 

Komunikują się między sobą asynchronicznie poprzez wysyłanie wiadomości, które mogą tworzyć nowych aktorów albo modyfikować ich zachowanie. 

Operacje na skrzynce odbiorczej są atomowe, co eliminuje race condition. 

Model aktorowy sprawdza się, gdy problem można podzielić na niezależne lub jasnopowiązane zadania możliwe do rozbicia na wątki. 

Zalety: 
- dekompozycja stanu na autonomiczne, asynchroniczne komponenty. 
- korzystanie z korzyści programowania obiektowego 

Wady: 
- tworzenie aktorów może być obciążające obliczeniowo
- nieprawidłowa archiwizacja może obniżyć wydajność w dużych systemach

## Ray framework

Ray to narzędzie do rozproszonych obliczeń z prostym API. 

## Taski
- wywołanie zdalnej, bezstanowej funkcji na workerze
- zwraca referencję do wyniku (future)
- korzysta się z dekoratora `@ray.remote`
- odczytanie wyniku jest blokujące `ray.get()` na obiekcie future
- automatyczny load balancing pomiędzy workerami
- służą do równoległej transformacji danych

## Aktor
- reprezentuje obliczenia stanowe
- metody aktora są wywoływane sekwencyjnie i zdalnie
- pozwalają na budowę usług które musza przechowywać dane lub zarządzać kontekstem
- zadania mogą być przekazywane innym aktorom lub taskom

```python
@ray.remote
class Counter:
    def __init__(self):
        self.value = 0
    
    def increment(self, inc=1):
        self.value += inc
        return self.value

# Tworzenie aktora
counter = Counter.remote()

# Równoległe wywołania metod
for _ in range(10):
    counter.increment.remote(1)

print(ray.get(counter.increment.remote(0)))
```

`Uwaga:` Powyższy kod wykona się na 1 wątku!

Ray obsługuje heterogeniczność i elastyczność dzięki
- `ray.wait([futures], num_returns=x)` - czekanie na dostępne wyniki, możliwość czekania na x pierwszych wyników
- określeniau wymagań zasobowych dla zadań (`@ray.remote(num_cpus=2, num_gpus=1)`)
- zagnieżdzaniu zdalnych funkcji dla skalowalności
- skorzystanie z dynamicznego schedulera

Model obliczeniowy Ray'a jest oparty na dynamicznym grafie zależności obliczeń, który ewoluuje podczas działania systemu. Węzły grafu to taski / aktorzy oraz obiekty danych, a kraedzie reprezentują zależności danych. System automatycznie uruchamia taski, gdy tylko ich dane wejściowe są dostępne. 

## Architektura

Warstwa aplikacji: 
- driver (program użytkownika)
- worker (bezstanowy)
- aktor (stanowy)

Warstwa systemu: 
- Global Control Store (przechowuje metadane i graf obliczeń)
- Bottom Up Distributed Scheduler (zarządza zadaniami)
- In Memory Distributed Object Store (przechowuje dane)

Ray umożliwia równoległe przetwarzanie zadań przekazując referencje do obiektów zamiast ich wartości, co minimalizuje kopiowanie danych między węzłami. 
 
W przypadku błędów albo utraty danych system odtwarza je powtarzając odpowiednie obliczenia na podstawie grafu zależności w GCS. 

## Wzorce projektowe

- Parallel tasks: bezstanowe funkcje rozproszone jako zadania. 
- Object as Futures: asynchroniczne wykoniwanie zadań i przekazywanie referencji. 
- Actors: stanowe serwisy komunikujące się przez wiadomości. 