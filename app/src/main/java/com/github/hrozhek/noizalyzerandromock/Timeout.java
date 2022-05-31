package com.github.hrozhek.noizalyzerandromock;

import java.util.concurrent.TimeUnit;

public class Timeout {

    private final Long time;
    private final TimeUnit timeUnit;

    public Timeout(Long time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
    }
    public Long getTime() {
        return time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }


}
