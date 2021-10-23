package com.a9934527599.myplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<MediaFiles> mVideoFiles=new ArrayList<>();
    PlayerView playerView;
    SimpleExoPlayer player;
    ImageView videoBack, lock,unlock,scaling;
    RelativeLayout root;
    int position;
    String videoTitle;
    TextView title;
    private  ControlsMode controlsMode;
    public enum ControlsMode{
        LOCK,FULLSCREEN;
    }
    ConcatenatingMediaSource concatenatingMediaSource;
    ImageView nextButton,prevButton;
    private View decrorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_video_player);
        getSupportActionBar().hide();
        decrorView= getWindow().getDecorView();
        decrorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility==0)
                    decrorView.setSystemUiVisibility(setInvisible());
            }
        });

        playerView= findViewById(R.id.exoPlayer_view);
        position=getIntent().getIntExtra("position",1);
        videoTitle=getIntent().getStringExtra("Video_title");
        mVideoFiles= getIntent().getExtras().getParcelableArrayList("videoArrayList");

        videoBack= findViewById(R.id.video_back);
        lock= findViewById(R.id.exo_lock);
        unlock= findViewById(R.id.unlock);
        scaling = findViewById(R.id.exo_sca);
        root=findViewById(R.id.root_layout);

        nextButton=findViewById(R.id.exo_next);
        prevButton=findViewById(R.id.exo_previous);
        nextButton.setOnClickListener(this::onClick);
        prevButton.setOnClickListener(this::onClick);
        title= findViewById(R.id.video_title);
        title.setText(videoTitle);

        videoBack.setOnClickListener(this);
        unlock.setOnClickListener(this);
        lock.setOnClickListener(this);

        scaling.setOnClickListener(firstListener);

        try {
            playVideo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playVideo() throws Exception {
        String path= mVideoFiles.get(position).getPath();
        Uri uri = Uri.parse(path);
        player = new SimpleExoPlayer.Builder(this).build();
        DefaultDataSourceFactory dataSourceFactory= new DefaultDataSourceFactory(
                this, Util.getUserAgent(this,"app"));
        concatenatingMediaSource= new ConcatenatingMediaSource();

        for (int i=0;i<mVideoFiles.size();i++){
            new File(String.valueOf(mVideoFiles.get(i)));
            MediaSource mediaSource= new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(String.valueOf(uri)));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }

        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        player.prepare(concatenatingMediaSource);
        player.seekTo(position, C.TIME_UNSET);
        playError();

    }

    private void playError() {
        player.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Toast.makeText(VideoPlayerActivity.this,"Video playing Error",Toast.LENGTH_SHORT).show();
            }
        });
        player.setPlayWhenReady(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(player.isPlaying()){
            player.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }
    private void setFullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // this.getWindow().setAttributes(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exo_next:
                try {
                    player.stop();
                    position++;
                    playVideo();
                }catch (Exception e){
                    Toast.makeText(this,"no Next Video",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                case R.id.exo_previous:
                    try {
                        player.stop();
                        position--;
                        playVideo();
                    }catch (Exception e){
                        Toast.makeText(this,"no Previus Video",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                break;

            case R.id.video_back:
                if (player!=null){
                    player.release();
                }
                finish();
                break;

            case R.id.unlock:
                controlsMode = controlsMode.LOCK;
                root.setVisibility(View.INVISIBLE);
                lock.setVisibility(View.VISIBLE);
               // Toast.makeText(this, "locked", Toast.LENGTH_SHORT).show();
                break;

            case R.id.exo_lock:
                controlsMode = controlsMode.FULLSCREEN;
                root.setVisibility(View.VISIBLE);
                lock.setVisibility(View.INVISIBLE);
               // Toast.makeText(this, "UNlocked", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decrorView.setSystemUiVisibility(setInvisible());
        }
    }
    public int setInvisible(){
                         return (
                                 View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                |View.SYSTEM_UI_FLAG_IMMERSIVE
                                |View.SYSTEM_UI_FLAG_FULLSCREEN
                                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                |View.SYSTEM_UI_FLAG_FULLSCREEN
                                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
    View.OnClickListener firstListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
            scaling.setImageResource(R.drawable.exo_controls_fullscreen_enter);
            Toast.makeText(VideoPlayerActivity.this, "Full Screen", Toast.LENGTH_SHORT).show();

            scaling.setOnClickListener(secondListener);
        }
    };
    View.OnClickListener secondListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
            scaling.setImageResource(R.drawable.exo_ic_fullscreen_exit);
            Toast.makeText(VideoPlayerActivity.this, "Stretch", Toast.LENGTH_SHORT).show();
            scaling.setOnClickListener(thirdListener);
        }
    };
    View.OnClickListener thirdListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            player.setVideoScalingMode(C.VIDEO_SCALING_MODE_DEFAULT);
            scaling.setImageResource(R.drawable.ic_fit);
            Toast.makeText(VideoPlayerActivity.this, "fit", Toast.LENGTH_SHORT).show();
            scaling.setOnClickListener(firstListener);
        }
    };
}