## Zadanie 3

Zaimplementować przesłanie wartości liczbowej pomiędzy serwerami napisanymi w Javie i Pythonie. 

- Symulujemy komunikację z platformą o
innej kolejności bajtów: klient Python ma
wysłać następujący ciąg bajtów:

```python
msg_bytes = (300).to_bytes(4, byteorder='little')
```

-  Server Javy ma wypisać otrzymaną liczbę
oraz odesłać liczbę zwiększoną o jeden