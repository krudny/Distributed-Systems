## RAFT

Algorytm RAFT to popularny, zrozumiały i szeroko stosowany algorytm konsensusu wykorzystywany w systemach rozproszonych do uzgadniania wspólnego stanu między wieloma węzłami. Jego głównym celem jest zapewnienie spójności i niezawodności danych nawet w przypadku awarii części systemu.

## Podstawowe założenia RAFT

- spójność systemu - wszystkie węzły systemu mają identyczny log operacji, co gwarantuje, że ich stany są zgodne
- tolerancja awarii - RAFT toleruje opóźnienia, utraty wiadomości i awarię węzła, ale nie toleruje złośliwych węzłów
- łatwość implementacji - algorytm został zaprojektowany tak, aby był prostszy w wdrożeniu niż inne

## Role węzłów

- lider - odpowiada za przyjmowanie komend od klientów, replikację logu i zarządzanie klastrem
- follower - odpowiada tylko na komendy RPC, czeka na instrukcje od lidera
- candidate - staje się kandydatem gdy podejrzewa awarię lidera i rozpoczyna proces wyboru nowego lidera

## Wybór lidera

1. węzły startują jako followerzy
2. Po upływie losowego czasu bez kontaktu z liderem, follower staje się kandydatem i rozpoczyna głosowanie
3. Wygrywa kandydat z większą ilością głosów. Warunkiem jest posiadanie najbardziej kompletnego logu. 

## Replikacja logu

1. Lider przyjmuje komendy od klientów, zapisuje w swoim logu i replikuje do followerów
2. Wpis uznaje się za uzgodniony gdy zostanie zapisany w większości logów
3. Lider powiadamia followerów o zatwierdzeniu wpisów w kolejnych komunikatach.

## Struktura logu

Każdy wpis zawiera 
- indeks wpisu
- numer termu (kadencji lidera)
- operację do wykonania

## Zmiana lidera

Nowy lide po przejęciu funkcji musi uzgodnić logi z pozostałymi węzłami usuwając lub dopisując wpisy tak aby były zgodne. 

## Bezpieczeństwo

Lider nigdy nie nadpisuje swojego logu, natomiast narzuca swój log followerom. Tylko wpisy znajdujące się w logu lidera mogą być zatwierdzony. Każdy wpis uzgodniony indukcyjnie gwarantuje, że poprzednie wpisy też zostały uzgodnione. 

## Schemat działania

1. Klient wysyła komendę do lidera
2. Lider zapisuje komendę w swoim logu i wysyła ją do followerów
3. Po otrzymaniu potwierdzenia od większości followerów, lider zatwierdza wpis
4. Operacja jest wykonywana
5. Lider powiadamia followerów o uzgodnieniu, co pozwala im wykonać operację. 
6. W razie awarii lidera, wybierany jest nowy lider z najbardziej kompletnym logiem. 
7. Nowy lider synchronizuje logi węzłów.