package com.shiluying.footrecord.ui.list;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shiluying.footrecord.R;
import com.shiluying.footrecord.activity.MainActivity;
import com.shiluying.footrecord.database.DBHelper;
import com.shiluying.footrecord.database.RecordProvider;
import com.shiluying.footrecord.utils.*;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private View view;
    private String id=null;
    private OnFragmentInteractionListener mListener;

    public DetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }
    public void getData(){
        Uri reocrd = Uri.parse("content://com.shiluying.footrecord.provider/Record");
        MainActivity mainActivity= (MainActivity) getActivity();
        Cursor cursor= mainActivity.getContentResolver().query(reocrd, DBHelper.RECORD_TABLE_COLUMNS,DBHelper.RECORD_ID+"=?",new String[]{mParam1},null);
        while (cursor != null && cursor.moveToNext()) {
            TextView title=view.findViewById(R.id.show_title);
            TextView content=view.findViewById(R.id.show_content);
            TextView site=view.findViewById(R.id.show_site);
            title.setText(cursor.getString(cursor.getColumnIndex(DBHelper.RECORD_TITLE)));
            BaseUtils baseUtils=new BaseUtils();
            SpannableString ss= baseUtils.initContent(getActivity() ,cursor.getString(cursor.getColumnIndex(DBHelper.RECORD_CONTENT)));
            content.setText(ss);
            site.setText(cursor.getString(cursor.getColumnIndex(DBHelper.RECORD_SITE)));
            id=cursor.getString(cursor.getColumnIndex(DBHelper.RECORD_ID));
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_detail, container, false);
        Button detail_delete = view.findViewById(R.id.detail_delete);
        detail_delete.setOnClickListener(this);
        Button detail_change = view.findViewById(R.id.detail_change);
        detail_change.setOnClickListener(this);
        getData();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_delete:
                deleteDiary();
                break;
            case R.id.detail_change:
                changeDiary();
                break;
        }
    }

    private void deleteDiary() {
        MainActivity mainActivity= (MainActivity) getActivity();
        int num= mainActivity.getContentResolver().delete(RecordProvider.CONTENT_URI,DBHelper.RECORD_ID+"=?",new String[]{id});
        Navigation.findNavController(mainActivity,R.id.nav_host_fragment)
                .navigate(R.id.nav_list);
    }
    private void changeDiary(){
        MainActivity mainActivity= (MainActivity) getActivity();
        Bundle arguments = new Bundle();
        arguments.putString("id",id);
        Navigation.findNavController(mainActivity,R.id.nav_host_fragment)
                .navigate(R.id.nav_add,arguments);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
