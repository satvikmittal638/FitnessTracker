package com.example.fitnesstracker;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.fitnesstracker.RecyclerViewAdapters.MusicAdapter;
import com.example.fitnesstracker.model.Music;

import java.util.ArrayList;


public class MusicFrag extends Fragment {
    private View view;
    private MusicAdapter adapter;

    private RecyclerView recyclerView;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public MusicFrag() {
        // Required empty public constructor
    }

    public static MusicFrag newInstance(String param1, String param2) {
        MusicFrag fragment = new MusicFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_music, container, false);

        initializeVariablesIn(view);

        setupRecyclerView();




        return view;
    }


    private ArrayList<Music> getMusicArrayList(){
        Music meditation=new Music("Meditation",R.drawable.meditation,R.raw.meditation);
//        Music exercise=new Music(   "Exercise",R.drawable.exercise);
//        Music jogging=new Music("Jogging",R.drawable.jogging);

        ArrayList<Music> musicArrayList=new ArrayList<>();
        musicArrayList.add(meditation);
//        musicArrayList.add(exercise);
//        musicArrayList.add(jogging);

        return musicArrayList;


    }

    private void initializeVariablesIn(View view){
        recyclerView=view.findViewById(R.id.RecvMusic);
    }

    private void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);//prevents from contracting to make the contents fit into it

        adapter=new MusicAdapter(getMusicArrayList(),view);
        recyclerView.setAdapter(adapter);
    }
}