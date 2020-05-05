package vdehorta.service;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class ClockService {

    private Clock clock;

    public ClockService() {
        this.clock = Clock.systemUTC();
    }

    public LocalDateTime now() {
        return LocalDateTime.now(clock);
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
