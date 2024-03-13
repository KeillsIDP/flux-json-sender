package me.keills.fluxjsonsender;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
public class JsonStreamingController {
    private boolean stopped;
    private Flux<String> jsonStream;

    private static final List<String> JSON_CHUNKS = Arrays.asList(
            "{\"field\":\"value1\"}",
            "{\"field\":\"value2\"}",
            "{\"field\":\"value3\"}",
            "{\"field\":\"value4\"}",
            "{\"field\":\"value5\"}",
            "{\"field\":\"value6\"}"
    );

    @GetMapping("/")
    public Flux<String> streamJson() {
        if (jsonStream == null) {
            stopped = false;
            jsonStream = Flux.fromIterable(JSON_CHUNKS).takeUntil(__->stopped)
                    .delayElements(Duration.ofSeconds(5));
        }
        return jsonStream;
    }

    @GetMapping("/stop")
    public Mono<Void> stopStream() {
        if (jsonStream != null) {
            stopped = true;
            jsonStream = null;
        }
        return Mono.empty();
    }
}
