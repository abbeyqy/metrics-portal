/*
 * Copyright 2019 Dropbox Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arpnetworking.rollups;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Gilligan Markham (gmarkham at dropbox dot com)
 */
public class RollupForwarder extends AbstractActor {
    private final ActorRef _rollupManager;

    /**
     * RollupForwarder actor constructor.
     *
     * @param rollupManager actor ref to RollupManager actor
     */
    @Inject
    public RollupForwarder(
            @Named("RollupManager") final ActorRef rollupManager) {
        _rollupManager = rollupManager;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchAny(
                        msg -> _rollupManager.forward(msg, context())

                )
                .build();
    }
}