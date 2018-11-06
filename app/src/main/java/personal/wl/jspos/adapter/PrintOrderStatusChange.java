package personal.wl.jspos.adapter;

import java.util.Observable;

public class PrintOrderStatusChange  extends Observable {
    private static PrintOrderStatusChange instance = null;

    public static PrintOrderStatusChange getInstance() {
        if (null == instance) {
            instance = new PrintOrderStatusChange();
        }
        return instance;
    } //通知观察者数据改变
    public void notifyDataChange(Boolean isPrinted) {
        //被观察者怎么通知观察者数据有改变了呢？？这里的两个方法是关键。
        setChanged();
        notifyObservers(isPrinted);
    }
}
