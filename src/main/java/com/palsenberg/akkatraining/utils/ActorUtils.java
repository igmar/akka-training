package com.palsenberg.akkatraining.utils;

import akka.actor.ActorPath;
import akka.actor.ActorRef;
import org.apache.commons.lang3.StringUtils;

public final class ActorUtils {
    private ActorUtils() {
    }

    public static String asStringWithAddress(final ActorRef ref) {
        if (ref == null) {
            return "";
        }
        final ActorPath path = ref.path();

        return path.toStringWithAddress(path.address());
    }

    public static String actorName(ActorRef ref) {
        return ref == null ? "" : ref.path().name();
    }

    public static String user(final String name) {
        return String.format("/user/%s", StringUtils.strip(name, "/"));
    }
}
