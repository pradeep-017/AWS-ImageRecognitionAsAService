package com.aws.ccproject.constants;

import java.util.concurrent.atomic.AtomicInteger;

public final class IntIdGenerator {
	private static final AtomicInteger sequence = new AtomicInteger(1);

    private IntIdGenerator() {}

    public static int generate(){
        return sequence.getAndIncrement();
    }
}
