package com.shiluying.footrecord.ui.add;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.shiluying.footrecord.R;
import com.shiluying.footrecord.activity.MainActivity;
import com.shiluying.footrecord.database.DBHelper;
import com.shiluying.footrecord.database.RecordProvider;
import com.shiluying.footrecord.utils.BaseUtils;

public class AddFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "id";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private AddViewModel mViewModel;
    private View view;
    private OnFragmentInteractionListener mListener;
    public AddFragment() {
        // Required empty public constructor
    }
    public static AddFragment newInstance() {
        return new AddFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_add, container, false);

        if (mParam1 != null) {
            getData();
        }
        Button add_picture = view.findViewById(R.id.add_picture);
        add_picture.setOnClickListener(this);
        Button add_submit = view.findViewById(R.id.add_submit);
        add_submit.setOnClickListener(this);
        return view;
    }
    public void getData(){
        Uri reocrd = Uri.parse("content://com.shiluying.footrecord.provider/Record");
        MainActivity mainActivity= (MainActivity) getActivity();
        Cursor cursor= mainActivity.getContentResolver().query(reocrd, DBHelper.RECORD_TABLE_COLUMNS,DBHelper.RECORD_ID+"=?",new String[]{mParam1},null);
        while (cursor != null && cursor.moveToNext()) {
            EditText title=view.findViewById(R.id.add_title);
            EditText content=view.findViewById(R.id.add_content);
            EditText site=view.findViewById(R.id.add_site);
            title.setText(cursor.getString(cursor.getColumnIndex(DBHelper.RECORD_TITLE)));
            BaseUtils baseUtils=new BaseUtils();
            SpannableString ss= baseUtils.initContent(getActivity() ,cursor.getString(cursor.getColumnIndex(DBHelper.RECORD_CONTENT)));
            content.setText(ss);
            site.setText(cursor.getString(cursor.getColumnIndex(DBHelper.RECORD_SITE)));
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Toast.makeText(getActivity().getBaseContext(),mParam1,Toast.LENGTH_LONG).show();
        }
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
            case R.id.add_picture:
                choosePic();
                break;
            case R.id.add_submit:
                if (mParam1 != null) {
                    changeData();
                }else{
                    saveData();
                }

                break;
        }
    }
    public void changeData(){
        EditText title=view.findViewById(R.id.add_title);
        EditText content=view.findViewById(R.id.add_content);
        EditText site=view.findViewById(R.id.add_site);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.RECORD_TITLE,title.getText().toString());
        contentValues.put(DBHelper.RECORD_CONTENT,content.getText().toString());
        contentValues.put(DBHelper.RECORD_SITE,site.getText().toString());
        int num= getActivity().getContentResolver().update(
                RecordProvider.CONTENT_URI, contentValues,DBHelper.RECORD_ID+" =?",new String[]{mParam1});
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment)
                .navigate(R.id.nav_list);
    }
    public void saveData(){
        EditText title=view.findViewById(R.id.add_title);
        EditText content=view.findViewById(R.id.add_content);
        EditText site=view.findViewById(R.id.add_site);
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.RECORD_TITLE,title.getText().toString());
        contentValues.put(DBHelper.RECORD_CONTENT,content.getText().toString());
        contentValues.put(DBHelper.RECORD_SITE,site.getText().toString());
        Uri uri= getActivity().getContentResolver().insert(
                RecordProvider.CONTENT_URI, contentValues);
        Log.i("INSERT",uri.toString());
        Toast.makeText(getActivity().getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
        Navigation.findNavController(getActivity(),R.id.nav_host_fragment)
                .navigate(R.id.nav_list);

    }
    public void choosePic(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("请选择图片获取方式");
        builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openPremissionAblum();
            }


        });
//        builder.setNeutralButton("相机", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                openPremissionCamera();
//            }
//        });
        builder.create().show();
    }

    private void openPremissionCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    }


    private void openPremissionAblum() {
        MainActivity activity=(MainActivity)getActivity();
        activity.callGallery();
//        Intent  intent=new  Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        startActivityForResult(intent, 300);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

