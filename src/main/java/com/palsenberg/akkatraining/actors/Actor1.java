package com.palsenberg.akkatraining.actors;

import akka.actor.AbstractLoggingActorWithStashAndTimers;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.palsenberg.akkatraining.pojo.Actor2Protocol;
import com.palsenberg.akkatraining.utils.ActorUtils;
import lombok.Value;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class Actor1 extends AbstractLoggingActorWithStashAndTimers {
    private static final String ACTOR1_NAME = "actor1";

    private static final String INIT_TIMER = "init-timer";

    public Actor1() {
        log().info("YYY {} started", getSelf());
    }

    @Override
    public void preStart() {
        log().info("YYY {} preStart()");
        getTimers().startSingleTimer(INIT_TIMER, new InitMessage(), Duration.ofMillis(500));
    }

    @Override
    public void postStop() {
        log().info("YYY {} postStop()");
    }

    @Override
    public void preRestart(final Throwable reason, final Optional<Object> message) {
        log().error("YYY preRestart {} -> {}", reason, message);
    }

    @Override
    public void postRestart(final Throwable reason) {
        log().error("YYY postRestart {}", reason);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(InitMessage.class, message -> {
                    final ActorSelection ref = getContext().actorSelection(ActorUtils.user(Actor2.name()));
                    //ref.tell(new Actor2Protocol.Actor2Message(), getSelf());

                    final ActorRef actor2 = ref.resolveOne(Duration.ofMillis(500)).toCompletableFuture().get();
                    Patterns.ask(actor2, new Actor2Protocol.Actor2Message(), Duration.ofMillis(500)).thenCompose(msg -> {
                        log().info("XXXXX RECEIVED {}", msg);
                       return null;
                    }).exceptionally(throwable -> {
                        log().error("Exception : {}", throwable);
                        return null;
                    });
                })
                .matchAny(msg -> {
                    log().info("Unknown message received class {} : {}", msg.getClass(), msg);
                    unhandled(msg);
                })
                .build();
    }

    public static Props props() {
        return Props.create(Actor1.class);
    }

    public static String name() {
        return ACTOR1_NAME;
    }

    @Value
    private static class InitMessage {
    }
}
