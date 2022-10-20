package com.palsenberg.akkatraining;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.cluster.singleton.ClusterSingletonManager;
import akka.cluster.singleton.ClusterSingletonManagerSettings;
import akka.cluster.singleton.ClusterSingletonProxy;
import akka.cluster.singleton.ClusterSingletonProxySettings;
import akka.management.javadsl.AkkaManagement;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import com.palsenberg.akkatraining.actors.Singleton1;
import com.palsenberg.akkatraining.utils.ActorUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.compat.java8.FutureConverters;

public class Main {

    private static final String AKKA_TRAINING_APP = "akka-training";
    private final Config config;
    private final ClassLoader classLoader;
    private final ActorSystem system;
    private Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(final String[] args) {
        new Main(args);
    }

    private Main(final String[] args) {
        this.config = ConfigFactory.load();
        this.classLoader = this.getClass().getClassLoader();
        this.system = ActorSystem.create(AKKA_TRAINING_APP, config, classLoader);

        AkkaManagement.get(system).start();
        ClusterBootstrap.get(system).start();

        log().info("Adding shutdown hook.");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log().info("Running shutdown hook, terminating application");
            AkkaManagement.get(system).stop();
            FutureConverters.toJava(system.terminate()).thenAccept(res -> {
                System.out.println("Actor system terminated, exciting");
            });
        }));

        /*
         * Initialize managers / proxies
         */
        createClusterSingleton1();

        final ActorRef singletonProxy = createClusterSingletonProxy();

        log().info("Waiting for Actorsystem termination");
        system.getWhenTerminated()
                .thenAccept(terminated -> {
                    log().info("System terminated: {}", terminated);
                    System.exit(0);
                });
    }

    private ActorRef createClusterSingletonProxy() {
        ClusterSingletonProxySettings proxySettings =
                ClusterSingletonProxySettings.create(system);

        return system.actorOf(ClusterSingletonProxy.props(ActorUtils.user(Singleton1.name()), proxySettings), Singleton1.name() + "-proxy");
    }

    private void createClusterSingleton1() {
        final ClusterSingletonManagerSettings settings =
                ClusterSingletonManagerSettings.create(system);

        system.actorOf(
                ClusterSingletonManager.props(
                        Singleton1.props(),
                        PoisonPill.getInstance(),
                        settings),
                Singleton1.name());
    }



    private Logger log() {
        return logger;
    }
}
