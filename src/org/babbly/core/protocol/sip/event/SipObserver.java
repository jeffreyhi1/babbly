package org.babbly.core.protocol.sip.event;

public interface SipObserver<U> {
    public void update(U arg);
}

