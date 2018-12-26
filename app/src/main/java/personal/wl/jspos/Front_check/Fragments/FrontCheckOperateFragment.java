package personal.wl.jspos.Front_check.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import personal.wl.jspos.Front_check.Adapter.FrontCheckDetailAdapter;
import personal.wl.jspos.R;
import personal.wl.jspos.pos.FrontCheck;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link FrontCheckOperateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrontCheckOperateFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters
//    private final SwipeMenuRecyclerView frontcheckview;
    private FrontCheckDetailAdapter frontCheckDetailAdapter;
    private List<FrontCheck> frontCheckList = new ArrayList<FrontCheck>();

    private FloatingActionButton fab;
    private FloatingActionButton fab_add;
    private FloatingActionButton fab_delete;
    private FloatingActionButton fab_modify;
    private FloatingActionButton fab_query;
    private List<View> menuViews = new ArrayList<>();
    private boolean isOpened = false;


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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_front_check_operate, container, false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        fab_add = view.findViewById(R.id.fab1);
        fab_delete = view.findViewById(R.id.fab2);
        fab_modify = view.findViewById(R.id.fab3);
        fab_query = view.findViewById(R.id.fab4);
        fab_add.setVisibility(View.INVISIBLE);
        fab_delete.setVisibility(View.INVISIBLE);
        fab_modify.setVisibility(View.INVISIBLE);
        fab_query.setVisibility(View.INVISIBLE);
        fab = view.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpened) {
                    fab_add.setVisibility(View.VISIBLE);
                    fab_delete.setVisibility(View.VISIBLE);
                    fab_modify.setVisibility(View.VISIBLE);
                    fab_query.setVisibility(View.VISIBLE);
                    isOpened = true;
                } else {
                    fab_add.setVisibility(View.INVISIBLE);
                    fab_query.setVisibility(View.INVISIBLE);
                    fab_delete.setVisibility(View.INVISIBLE);
                    fab_modify.setVisibility(View.INVISIBLE);
                    isOpened = false;
                }
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "增加", Toast.LENGTH_LONG).show();
            }
        });

        for (int i = 0; i < 20; i++) {

            FrontCheck frontCheck = new FrontCheck();
            frontCheck.setBraid("01002");
            frontCheck.setProId("123456578" + String.valueOf(i));
            frontCheck.setBarCode("B123456578" + String.valueOf(i));
            frontCheck.setShieldNo("jjj");
            frontCheck.setCheckQty1(2.00 + i);
            frontCheckList.add(frontCheck);
        }


        frontCheckDetailAdapter = new FrontCheckDetailAdapter(getContext(), frontCheckList);
        final SwipeMenuRecyclerView frontcheckview = view.findViewById(R.id.frontchecklist);
        frontcheckview.setLayoutManager(mLayoutManager);
        frontcheckview.setAdapter(frontCheckDetailAdapter);
        initViews(view);
        return view;


    }


    private void initViews(View view) {

    }


}
