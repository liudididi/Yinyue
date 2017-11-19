package com.liu.asus.yinyue;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.bt_kaishi)
    ImageView btKaishi;
    @BindView(R.id.bt_zuo)
    ImageView btZuo;
    @BindView(R.id.bt_you)
    ImageView btYou;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.progressBar)
    SeekBar progressBar;
    @BindView(R.id.tv_dq)
    TextView tvDq;
    @BindView(R.id.tv_zong)
    TextView tvZong;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.bt_moshi)
    ImageView btMoshi;
    @BindView(R.id.img_tu)
    ImageView imgTu;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Runnable task;
    private List<Bean> list;
    private int dq;//当前播放的下标
    private SharedPreferences sp;
    private String uri = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1510306428416&di=eb90b619fb9d95e016db1e831b71c38b&imgtype=0&src=http%3A%2F%2Fs1.trueart.com%2F20170725%2F215830153_640.gif";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定控件
        ButterKnife.bind(this);
        //创建音乐集合
        list = new ArrayList<>();
        sp = getSharedPreferences("yinyue", MODE_PRIVATE);
        //从sp中获取下标
        dq = sp.getInt("dq", 0);
        //加载list数据
        initdata();
        //加载图片
        ImageLoader.getInstance().displayImage(uri,imgTu);
        progressBar = findViewById(R.id.progressBar);
        //实例化对象
        mediaPlayer = new MediaPlayer();
        initMediaplayer();
        //拖动条的改变监听
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //如果手动拖到，跳到当前位置
                if (b == true) {
                    //跳到指定位置
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btKaishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //当音乐不不播放时
                if (!mediaPlayer.isPlaying()) {
                    //音乐播放
                    mediaPlayer.start();

                    Glide.with(MainActivity.this).load(uri).into(imgTu);
                    //换图
                    btKaishi.setImageResource(R.drawable.zanting);
                    //设置拖动条最大值
                    progressBar.setMax(mediaPlayer.getDuration());
                    //启动线程
                    handler.postDelayed(task, 200);
                   /* Timer t=new Timer();
                    TimerTask task=new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    int totalTime = Math.round(mediaPlayer.getCurrentPosition() / 1000);
                                    String str = String.format("%02d:%02d", totalTime / 60,
                                            totalTime % 60);
                                    tvDq.setText(str+"");
                                    progressBar.setProgress(mediaPlayer.getCurrentPosition());
                                }
                            });

                        }
                    };
                    t.schedule(task, 0,200);
*/
                } else {
                    //正在播放时
                    ImageLoader.getInstance().displayImage(uri,imgTu);
                    btKaishi.setImageResource(R.drawable.bofang);
                    //暂停方法
                    mediaPlayer.pause();
                }
            }
        });
        task = new Runnable() {
            @Override
            public void run() {
                //设置拖动条的当前值
                progressBar.setProgress(mediaPlayer.getCurrentPosition());
                //获取当前时间
                int totalTime = Math.round(mediaPlayer.getCurrentPosition() / 1000);
                //保留两位小数
                String str = String.format("%02d:%02d", totalTime / 60,
                        totalTime % 60);
                //赋值
                tvDq.setText(str + "");
                //调用自身
                handler.postDelayed(this, 200);
            }
        };
        btZuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //当前下标减减
                dq = dq - 1;
                if (dq < 0) {
                    dq = list.size() - 1;
                }
                //当切换音乐的时候 切记重置
                mediaPlayer.reset();
                //初始化
                initMediaplayer();
                mediaPlayer.start();
                btKaishi.setImageResource(R.drawable.zanting);
                int totalTime = Math.round(mediaPlayer.getDuration() / 1000);
                String str = String.format("%02d:%02d", totalTime / 60,
                        totalTime % 60);
                tvZong.setText(str);
                progressBar.setMax(mediaPlayer.getDuration());
                handler.postDelayed(task, 200);
            }
        });
        btYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int moshi = sp.getInt("moshi", 2);
                switch (moshi) {
                    case 3://随机播放
                        //随机生成1到最大集合的随机数 赋给当前下标
                        dq = new Random().nextInt(list.size());
                        mediaPlayer.reset();
                        initMediaplayer();
                        mediaPlayer.start();
                        btKaishi.setImageResource(R.drawable.zanting);
                        int totalTim = Math.round(mediaPlayer.getDuration() / 1000);
                        String st = String.format("%02d:%02d", totalTim / 60,
                                totalTim % 60);
                        tvZong.setText(st);
                        progressBar.setMax(mediaPlayer.getDuration());
                        handler.postDelayed(task, 200);
                        break;

                    default:
                        dq = dq + 1;
                        if (dq > list.size() - 1) {
                            dq = 0;
                        }
                        mediaPlayer.reset();
                        initMediaplayer();
                        mediaPlayer.start();
                        btKaishi.setImageResource(R.drawable.zanting);
                        int totalTime = Math.round(mediaPlayer.getDuration() / 1000);
                        String str = String.format("%02d:%02d", totalTime / 60,
                                totalTime % 60);
                        tvZong.setText(str);
                        progressBar.setMax(mediaPlayer.getDuration());
                        handler.postDelayed(task, 200);
                        break;

                }

            }
        });
    }
    public void scanFileList(File parentFile) {
        File[] listFile = parentFile.listFiles();
        int length = listFile.length;

        if (listFile != null) {
            for (int i = 0; i < length; i++) {
                File file = listFile[i];
                if (file.isDirectory()) {
                    scanFileList(file);
                } else {
                    //file://music/p/xx.mp3
                    if (file.getName().endsWith(".mp3")||file.getName().endsWith(".m4a")) {
                        String fileName = file.getName();
                        list.add(new Bean(fileName.substring(0, fileName.length()-".mp3".length()),file.getAbsolutePath()));
                    }
                }
            }
        }
    }

    private void initdata() {
         scanFileList(Environment.getExternalStorageDirectory());

/*
        list.add(new Bean("安河桥北", "mnt/sdcard/ahq.m4a"));
        list.add(new Bean("彩虹", "/mnt/sdcard/ch.mp3"));
        list.add(new Bean("蓝莲花", "/mnt/sdcard/llh.mp3"));
        list.add(new Bean("蓝莲花错误", "/mnt/sdcard/llha.mp3"));*/
    }

    private void initMediaplayer() {
        try {
            int moshi = sp.getInt("moshi", 2);
            switch (moshi) {
                case 1://单曲循环
                    btMoshi.setImageResource(R.drawable.dqxh);

                    break;
                case 2://顺序循环
                    btMoshi.setImageResource(R.drawable.shunxu);
                    break;
                case 3://随机播放
                    btMoshi.setImageResource(R.drawable.suiji);
                    break;
            }
            mediaPlayer.setDataSource(list.get(dq).path);
            sp.edit().putInt("dq", dq).commit();
            mediaPlayer.prepare();
            int totalTime = Math.round(mediaPlayer.getDuration() / 1000);
            tvName.setText(list.get(dq).name);
            String str = String.format("%02d:%02d", totalTime / 60,
                    totalTime % 60);
            tvZong.setText(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                int moshi = sp.getInt("moshi", 2);
                switch (moshi) {
                    case 1://单曲循环
                        sp.edit().putInt("dq", dq).commit();
                        // 设置为单曲循环
                        mediaPlayer.setLooping(true);
                        mediaPlayer.reset();
                        initMediaplayer();
                        mediaPlayer.start();
                        break;
                    case 2://顺序播放
                        sp.edit().putInt("dq", dq).commit();
                        mediaPlayer.setLooping(false);
                        dq = dq + 1;
                        if (dq > list.size() - 1) {
                            dq = 0;
                        }
                        mediaPlayer.reset();
                        initMediaplayer();
                        mediaPlayer.start();
                        btKaishi.setImageResource(R.drawable.zanting);
                        int totalTime = Math.round(mediaPlayer.getDuration() / 1000);
                        String str = String.format("%02d:%02d", totalTime / 60,
                                totalTime % 60);
                        tvZong.setText(str);
                        progressBar.setMax(mediaPlayer.getDuration());
                        handler.postDelayed(task, 200);
                        break;
                    case 3://随机播放
                        sp.edit().putInt("dq", dq).commit();
                        mediaPlayer.setLooping(false);
                        dq = new Random().nextInt(list.size());
                        mediaPlayer.reset();
                        initMediaplayer();
                        mediaPlayer.start();
                        btKaishi.setImageResource(R.drawable.zanting);
                        int totalTim = Math.round(mediaPlayer.getDuration() / 1000);
                        String st = String.format("%02d:%02d", totalTim / 60,
                                totalTim % 60);
                        tvZong.setText(st);
                        progressBar.setMax(mediaPlayer.getDuration());
                        handler.postDelayed(task, 200);
                        break;
                }

            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                int moshi = sp.getInt("moshi", 2);
                switch (moshi) {
                    case 3://随机播放
                        dq = new Random().nextInt(list.size());
                        mediaPlayer.reset();
                        initMediaplayer();
                        mediaPlayer.start();
                        btKaishi.setImageResource(R.drawable.zanting);
                        int totalTim = Math.round(mediaPlayer.getDuration() / 1000);
                        String st = String.format("%02d:%02d", totalTim / 60,
                                totalTim % 60);
                        tvZong.setText(st);
                        progressBar.setMax(mediaPlayer.getDuration());
                        handler.postDelayed(task, 200);
                        break;
                    default:
                        dq = dq + 1;
                        if (dq > list.size() - 1) {
                            dq = 0;
                        }
                        mediaPlayer.reset();
                        initMediaplayer();
                        mediaPlayer.start();
                        btKaishi.setImageResource(R.drawable.zanting);
                        int totalTime = Math.round(mediaPlayer.getDuration() / 1000);
                        String str = String.format("%02d:%02d", totalTime / 60,
                                totalTime % 60);
                        tvZong.setText(str);
                        progressBar.setMax(mediaPlayer.getDuration());
                        handler.postDelayed(task, 200);
                        break;

                }

                return true;
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

    }

    /**
     * 当按手机上的返回按键的时候，会自动调用系统的onKeyDown方法，
     * 而onKeyDown方法右会自动调用onDestroy()方法
     * 销毁该Activity,此时如果onDestroy()方法不重写，那么正在播放
     * 的音乐不会停止，所以这时候要重写onDestroy()方法，
     * 在该方法中，加入mediaplayer.stop()方法，表示按返回键的时候，
     * 会调用mediaPlayer对象的stop方法，
     * 从而停止音乐的播放
     */

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            sp.edit().putInt("dq", dq).commit();
            mediaPlayer.stop();
            mediaPlayer.release();
            progressBar.setProgress(0);
            btKaishi.setImageResource(R.drawable.bofang);
            mediaPlayer = null;
        }
        super.onDestroy();
    }

    @OnClick(R.id.bt_moshi)
    public void onViewClicked() {
        int moshi = sp.getInt("moshi", 2);
        switch (moshi) {
            case 1://顺序播放
                Toast.makeText(this, "顺序播放", Toast.LENGTH_SHORT).show();
                btMoshi.setImageResource(R.drawable.shunxu);
                sp.edit().putInt("moshi", 2).commit();
                break;
            case 2://随机播放
                btMoshi.setImageResource(R.drawable.suiji);
                Toast.makeText(this, "随机播放", Toast.LENGTH_SHORT).show();
                sp.edit().putInt("moshi", 3).commit();
                break;
            case 3://单曲循环
                Toast.makeText(this, "单曲循环", Toast.LENGTH_SHORT).show();
                btMoshi.setImageResource(R.drawable.dqxh);
                sp.edit().putInt("moshi", 1).commit();
                break;
        }
    }
}
