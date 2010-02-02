package org.babbly.core.protocol.sip.event;

import java.util.ArrayList;
import java.util.List;

public class SipObservable<U> {

	private List<SipObserver<U>> observers =
		new ArrayList<SipObserver<U>>();

	public void addObserver(SipObserver<U> obs) {
		if (obs == null) {
			throw new IllegalArgumentException();
		}
		if (observers.contains(obs)) {
			return;
		}
		observers.add(obs);            
	}

	/**
	 * Deletes an observer from the set of observers of this object. 
	 * Passing <CODE>null</CODE> to this method will have no effect.
	 * @param   o   the observer to be deleted.
	 */
	public synchronized void deleteObserver(SipObserver<U> o) {
		observers.remove(o);
	}

	public synchronized void deleteObservers() {
		observers.clear();
	}

	public void notifyObservers(U data) {
		for (SipObserver<U> obs : observers) {
			obs.update(data);
		}
	}
}

