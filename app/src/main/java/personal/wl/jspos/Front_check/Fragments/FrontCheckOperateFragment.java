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
 * {@link FrontCheckOperateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FrontCheckOperateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrontCheckOperateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters



    public FrontCheckOperateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment FrontCheckOperateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FrontCheckOperateFragment newInstance() {
        FrontCheckOperateFragment fragment = new FrontCheckOperateFragment();
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

        View view =inflater.inflate(R.layout.fragment_front_check_operate, container, false);

        initViews(view);
        return view;
    }

    private void initViews(View view) {
    }




}
