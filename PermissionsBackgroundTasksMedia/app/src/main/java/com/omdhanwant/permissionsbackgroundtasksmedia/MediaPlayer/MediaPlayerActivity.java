package com.omdhanwant.permissionsbackgroundtasksmedia.MediaPlayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.omdhanwant.permissionsbackgroundtasksmedia.R;

import java.io.IOException;
import java.util.ArrayList;

public class MediaPlayerActivity extends AppCompatActivity {

    SeekBar mSeekBar;
    Button mStart;
    Button mStop;
    Button mPause;
//    ListView songListView;
    RecyclerView songListView;
    MediaPlayer mMediaPlayer;
    ArrayList<Song> songList;

    MediaRecyclerViewAdapter mSongAdapter;

    int currentSeekValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player);

        mSeekBar = (SeekBar) findViewById(R.id.mseekBar);
        mStart = (Button) findViewById(R.id.mstart);
        mStop = (Button) findViewById(R.id.mstop);
        mPause = (Button) findViewById(R.id.mpause);
        songListView = (RecyclerView) findViewById(R.id.mlist);

        // start a seek bar thread
        SeekBarThread seekBarThread= new SeekBarThread();
        seekBarThread.start();

        // set operations on actions of seek bar
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentSeekValue=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMediaPlayer.seekTo(currentSeekValue);
            }
        });


        // setup bttons logic

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMediaPlayer != null)
                    mMediaPlayer.start();
            }
        });
        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMediaPlayer != null)
                    mMediaPlayer.stop();
            }
        });
        mPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mMediaPlayer != null)
                    mMediaPlayer.pause();
            }
        });


        LoadSongs();


        // on list click

        mSongAdapter.setClickListener(new MediaRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Song song = songList.get(position);

                if(mMediaPlayer!=null && mMediaPlayer.isPlaying()) {
                    mMediaPlayer.release();
                }
                mMediaPlayer = new MediaPlayer();

                try {
                    mMediaPlayer.setDataSource(song.Path);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                    mSeekBar.setMax(mMediaPlayer.getDuration());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // load list

    void LoadSongs(){
        songList = getAllSongs();
        songListView.setLayoutManager(new LinearLayoutManager(this));
        mSongAdapter = new MediaRecyclerViewAdapter(this, songList);
        songListView.setAdapter(mSongAdapter);
    }


    //
    ArrayList<Song> getAllSongs() {
        ArrayList<Song> songs = new ArrayList<>();

        for (int i = 0 ; i < 20 ; i++) {
            songs.add(new Song("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3","Sound Helix Song 1","Sound Helix","\tT. Schürger"));
        }

        return songs;
    }


    // seekbar thread
    class SeekBarThread extends Thread {

        @Override
        public void run() {

            while(true){
                try {
                    Thread.sleep(1000);

                }  catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //seek bar   seekBar1.setProgress(mp .getCurrentPosition());
                        if (mMediaPlayer !=null)
                            mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                    }
                });



            }
        }
    }

}
