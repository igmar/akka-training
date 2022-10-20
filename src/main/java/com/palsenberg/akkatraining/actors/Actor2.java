package com.palsenberg.akkatraining.actors;

import akka.actor.AbstractLoggingActorWithStashAndTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.palsenberg.akkatraining.pojo.Actor2Protocol;

import java.util.Optional;

public class Actor2 extends AbstractLoggingActorWithStashAndTimers {
    private static final String ACTOR_NAME = "actor2";

    public Actor2() {
        log().info("XXXX {} started", getSelf());
    }

    @Override
    public void preStart() {
        log().info("XXX {} preStart()");

    }

    @Override
    public void postStop() {
        log().info("XXX {} postStop()");
    }

    @Override
    public void preRestart(final Throwable reason, final Optional<Object> message) {
        log().error("XXX preRestart {} -> {}", reason, message);
    }

    @Override
    public void postRestart(final Throwable reason) {
        log().error("XXX postRestart {}", reason);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Actor2Protocol.Actor2Message.class, message -> {
                    final ActorRef sender = getSender();
                    //sender.tell(new Actor2Protocol.Actor2Response(), getSelf());
                })
                .matchAny(msg -> {
                    log().info("Unknown message received of class {} : {}", msg.getClass(), msg);
                    unhandled(msg);
                })
                .build();
    }

    public static Props props() {
        return Props.create(Actor2.class);
    }

    public static String name() {
        return ACTOR_NAME;
    }
}
