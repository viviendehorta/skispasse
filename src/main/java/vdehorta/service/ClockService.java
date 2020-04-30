package vdehorta.service;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;

@Component
public class ClockService {

    private Clock clock;

    public ClockService() {
        this.clock = Clock.systemUTC();
    }

    public Instant now() {
        return clock.instant();
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
