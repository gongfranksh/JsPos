package personal.wl.jspos.db;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.widget.TextView;

public class InverseMethodTools {
    @BindingAdapter("android:text")
    public static void setText(TextView view, double value) {
//        if (view.getText() != null
//                && ( !view.getText().toString().isEmpty() )
//                && Double.parseDouble(view.getText().toString()) != value) {
//            view.setText(Double.toString(value));
//        }
        if (view.getText() != null
                ) {
            view.setText(Double.toString(value));
        }

    }

    @InverseBindingAdapter(attribute = "android:text")
    public static Double getText(TextView view) {
        if (!view.getText().toString().isEmpty()) {
            return Double.parseDouble(view.getText().toString());
        } else return 1.00;
    }




}
