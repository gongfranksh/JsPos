package personal.wl.jspos.Front_check.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import personal.wl.jspos.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link FrontCheckDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrontCheckDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment FrontCheckDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FrontCheckDetailFragment newInstance() {
        FrontCheckDetailFragment fragment = new FrontCheckDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_front_check_detail, container, false);
    }
}





