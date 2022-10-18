package com.palsenberg.akkatraining.pojo;

import akka.actor.ActorRef;
import lombok.Value;

public interface Actor2Protocol {
    @Value
    public class Actor2Message {
        //ActorRef replyTo;
    }

    @Value
    public class Actor2Response {
    }
}
