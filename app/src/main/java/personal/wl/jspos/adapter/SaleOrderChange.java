package personal.wl.jspos.adapter;

import java.util.Observable;


public class SaleOrderChange extends Observable {
    private static SaleOrderChange instance = null;

    public static SaleOrderChange getInstance() {
        if (null == instance) {
            instance = new SaleOrderChange();
        }
        return instance;
    } //通知观察者数据改变
    public void notifyDataChange(Double total) {
        //被观察者怎么通知观察者数据有改变了呢？？这里的两个方法是关键。
        setChanged();
        notifyObservers(total);
    }

}
