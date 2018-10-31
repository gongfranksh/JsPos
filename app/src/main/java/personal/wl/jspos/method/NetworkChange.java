package personal.wl.jspos.method;

import java.util.Observable;

public class NetworkChange extends Observable {
    private static NetworkChange instance = null;

    public static NetworkChange getInstance() {
        if (null == instance) {
            instance = new NetworkChange();
        }
        return instance;
    } //通知观察者数据改变

    public void notifyDataChange(APPNetwork net) {

        setChanged();
        notifyObservers(net);
    }
}
