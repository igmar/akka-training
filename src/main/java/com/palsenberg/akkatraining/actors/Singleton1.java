package com.palsenberg.akkatraining.actors;

import akka.actor.AbstractLoggingActorWithStashAndTimers;
import akka.actor.Props;

import java.util.Optional;

public class Singleton1 extends AbstractLoggingActorWithStashAndTimers {
    private static final String ACTOR_NAME = "singleton1";

    public Singleton1() {
        log().info("ZZZ {} started", getSelf());
    }

    @Override
    public void preStart() {
        log().info("ZZZ {} preStart()");
    }

    @Override
    public void postStop() {
        log().info("ZZZ {} postStop()");
    }

    @Override
    public void preRestart(final Throwable reason, final Optional<Object> message) {
        log().error("ZZZ preRestart {} -> {}", reason, message);
    }

    @Override
    public void postRestart(final Throwable reason) {
        log().error("YYY postRestart {}", reason);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(msg -> {
                    log().info("Unknown message received class {} : {}", msg.getClass(), msg);
                    unhandled(msg);
                })
                .build();
    }

    public static Props props() {
        return Props.create(Singleton1.class);
    }

    public static String name() {
        return ACTOR_NAME;
    }
}
