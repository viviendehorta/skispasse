package com.vdehorta.skispasse.time;

import java.time.*;

public enum DateTimeProvider {

    INSTANCE;

    private final Clock defaultClock = Clock.system(TimeUtils.FRANCE_ZONE);

    private Clock clock = defaultClock;

    public LocalDateTime nowDateTime() {
        return LocalDateTime.now(clock);
    }

    public LocalDate nowDate() {
        return LocalDate.now(clock);
    }

    public Instant nowInstant() {
        return Instant.now(clock);
    }

    /**
     * FOR TESTING PURPOSE ONLY !
     * Set clock as a fixed clock on the specified datetime.
     *
     * @param localDateTime
     */
    public void setTime(LocalDateTime localDateTime) {
        ZoneOffset offset = localDateTime.atZone(TimeUtils.FRANCE_ZONE).getOffset();
        this.clock = Clock.fixed(localDateTime.toInstant(offset), TimeUtils.FRANCE_ZONE);
    }

    /**
     * FOR TESTING PURPOSE ONLY !
     * Reset the provider time clock to its initial value.
     */
    public void resetTime() {
        this.clock = this.defaultClock;
    }
}
