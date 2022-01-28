package g55803.samegame.utils;

/**
 * Provides an observable interface for the Observable/Observer design pattern.
 *
 * @author Nathan Furnal
 */
public interface Observable {

    /**
     * Subscribes an observer to the current subject.
     *
     * @param observer the observer which will observe the subject.
     */
    void subscribe(Observer observer);

    /**
     * Unsubscribes an observer to the current subject.
     *
     * @param observer the observer to unsubscribe from the subject.
     */
    void unsubscribe(Observer observer);

    /**
     * Notifies all observers of the new state of the current subject.
     *
     * @param observerList the list of observers to notify.
     */
//    void notify(List<Observer> observerList);
}

