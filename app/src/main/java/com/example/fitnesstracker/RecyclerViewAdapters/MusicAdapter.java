package com.example.fitnesstracker.RecyclerViewAdapters;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnesstracker.R;
import com.example.fitnesstracker.model.Music;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.jvm.internal.markers.KMutableSet;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicVH> {
    private ArrayList<Music> musicArrayList;
    private MediaPlayer player;

    private ImageView btn_rewind,btn_play,btn_forward;
    private SeekBar seekbMusic;

    private boolean musicIsPlaying=false;
    private int musicLeftAt;

    public MusicAdapter(ArrayList<Music> musicArrayList,View view) {
        this.musicArrayList = musicArrayList;

        btn_rewind=view.findViewById(R.id.btn_rewind);
        btn_forward=view.findViewById(R.id.btn_forward);
        btn_play=view.findViewById(R.id.btn_play);
        seekbMusic=view.findViewById(R.id.seekbMusic);
    }

    @NonNull
    @Override
    public MusicVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_music_row,parent,false);
        return new MusicVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicVH holder, int position) {
        Music music=musicArrayList.get(position);
        holder.musicName.setText(music.getName());
        holder.musicImage.setImageResource(music.getImageId());
        Log.d("Stay Fit","Added a cardview");

        int musicId=music.getMusicId();

        holder.cardViewMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player=MediaPlayer.create(holder.musicName.getContext(),musicId);
                player.start();
                musicIsPlaying=true;

                seekbMusic.setMax(player.getDuration());

                    seekbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser)
                                player.seekTo(progress);
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                            //do nothing
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            //do nothing
                        }
                    });


                btn_play.setImageResource(R.drawable.icon_pause);

                    btn_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(musicIsPlaying){
                            player.pause();
                            musicLeftAt=player.getCurrentPosition();
                            btn_play.setImageResource(R.drawable.icon_play_arrow);
                            musicIsPlaying=false;
                            }
                            else{
                               player.seekTo(musicLeftAt);
                               player.start();
                               btn_play.setImageResource(R.drawable.icon_pause);
                               musicIsPlaying=true;
                            }
                        }
                    });


                btn_forward.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        player.seekTo(player.getCurrentPosition()+10000);//in ms
                    }
                });

                btn_rewind.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        player.seekTo(player.getCurrentPosition()-10000);//in ms
                    }
                });


                TimerTask timerTask=new TimerTask() {
                    @Override
                    public void run() {
                        seekbMusic.setProgress(player.getCurrentPosition());
                    }
                };
                new Timer().scheduleAtFixedRate(timerTask,0,150);




            }
        });




    }


    @Override
    public int getItemCount() {
        return musicArrayList.size();
    }

    public class MusicVH extends RecyclerView.ViewHolder{
    TextView musicName;
    CircleImageView musicImage;
    CardView cardViewMusic;
        public MusicVH(@NonNull View itemView) {
            super(itemView);
            musicName=itemView.findViewById(R.id.musicName);
            musicImage=itemView.findViewById(R.id.musicImage);
            cardViewMusic=itemView.findViewById(R.id.cardViewMusic);
        }
    }
}
