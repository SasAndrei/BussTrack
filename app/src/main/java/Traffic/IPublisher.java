package Traffic;

import Accounts.ISubscriber;

public interface IPublisher {
    public void subscribe(ISubscriber subscriber, Station station);
    public void unSubscribe(ISubscriber subscriber);
    public void notifySubscribers(Station station);
}
