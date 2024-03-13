# Задание 
Требуется отправлять с сервера на клиент JSON, с определенным интервалом, по частям. Для этого будет использован SpringWEBFlux.
</br>

JSON представлен листом строк
```java
  private static final List<String> JSON_CHUNKS = Arrays.asList(
            "{\"field\":\"value1\"}",
            "{\"field\":\"value2\"}",
            "{\"field\":\"value3\"}",
            "{\"field\":\"value4\"}",
            "{\"field\":\"value5\"}",
            "{\"field\":\"value6\"}"
    );
```
При запросе на localhost сразу же будет инициирована отправка сообщения
```java
  @GetMapping("/")
    public Flux<String> streamJson() {
        if (jsonStream == null) {
            stopped = false;
            jsonStream = Flux.fromIterable(JSON_CHUNKS).takeUntil(__->stopped)
                    .delayElements(Duration.ofSeconds(5));
        }
        return jsonStream;
    }
```

Из этой строки мы можем увидеть ```java Flux.fromIterable(JSON_CHUNKS).takeUntil(__->stopped)
.delayElements(Duration.ofSeconds(5)) ```, что мы будем отправлять раз в 5 секунд, по одному элементу из листа,
пока флаг ``` stopped ``` имеет значение ``` True ```

Мы также можем остановить текущий ``` Flux ``` на стороне сервера

```java
  @GetMapping("/stop")
      public Mono<Void> stopStream() {
          if (jsonStream != null) {
              stopped = true;
              jsonStream = null;
          }
          return Mono.empty();
      }
```

Пример интерфейса
![Image alt](https://github.com/KeillsIDP/flux-json-sender/blob/main/gitimg/git_1.png?raw=true)
![Image alt](https://github.com/KeillsIDP/flux-json-sender/blob/main/gitimg/git_2.png)

