package personal.wl.jspos.Front_check.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import personal.wl.jspos.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FrontCheckSettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrontCheckSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrontCheckSettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



    public FrontCheckSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment FrontCheckSettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FrontCheckSettingFragment newInstance() {
        FrontCheckSettingFragment fragment = new FrontCheckSettingFragment();
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
        return inflater.inflate(R.layout.fragment_front_check_setting, container, false);
    }




}
