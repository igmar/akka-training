package com.palsenberg.akkatraining;

import akka.actor.ActorSystem;
import akka.management.javadsl.AkkaManagement;
import com.palsenberg.akkatraining.actors.Actor1;
import com.palsenberg.akkatraining.actors.Actor2;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.compat.java8.FutureConverters;

public class Main {

    private static final String AKKA_TRAINING_APP = "akka-training";
    private final Config config;
    private Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        new Main(args);
    }

    private Main(final String[] args) {
        this.config = ConfigFactory.load();
        final ClassLoader classLoader = this.getClass().getClassLoader();
        final ActorSystem system = ActorSystem.create(AKKA_TRAINING_APP, config, classLoader);

        AkkaManagement.get(system).start();

        log().info("Adding shutdown hook.");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log().info("Running shutdown hook, terminating application");
            AkkaManagement.get(system).stop();
            FutureConverters.toJava(system.terminate()).thenAccept(res -> {
                System.out.println("Actor system terminated, exciting");
            });
        }));

        system.actorOf(Actor1.props(), Actor1.name());
        system.actorOf(Actor2.props(), Actor2.name());

        log().info("Waiting for Actorsystem termination");
        system.getWhenTerminated()
                .thenAccept(terminated -> {
                    log().info("System terminated: {}", terminated);
                    System.exit(0);
                });
    }

    private Logger log() {
        return logger;
    }
}
