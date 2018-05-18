package com.example.user.a442zenbo_record;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    // 錄音按鈕
    private ToggleButton record;
    //資料夾確認按鈕
    private Button folder_check;
    //錄音檔確認按鈕
    private Button recfile_check;
    //錄音檔確認按鈕
    private Button play_sound;
    //資料夾輸入欄位
    private EditText folder_input;
    //錄音檔輸入欄位
    private EditText recfile_input;

    File recordFile;
    // 錄音器
    MediaRecorder mediaRecorder = null;
    // 撥放器
    MediaPlayer mediaPlayer = null;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        record = (ToggleButton)findViewById(R.id.toggleButton);
        folder_check = (Button)findViewById(R.id.button);
        recfile_check = (Button)findViewById(R.id.button2);
        play_sound = (Button)findViewById(R.id.button3);
        folder_input = (EditText)findViewById(R.id.editText);
        recfile_input = (EditText)findViewById(R.id.editText2);
        /*錄音鍵預設不可按*/
        record.setEnabled(false);
        /*錄音按鍵*/
        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record();
            }
        });
        /*資料夾確認按鍵*/
        folder_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                folder_construct();
            }
        });
        /*錄音檔案確認按鍵*/
        recfile_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recfile_construct();
            }
        });
        /*撥放自己的錄音檔*/
        play_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sound_play();
            }
        });
        /*撥放範例的錄音檔*/

    }

    /*創建資料夾*/
    public static class FileUtils {

        private static final String TAG = "FileUtils";

        public static final int FLAG_SUCCESS = 1;//创建成功
        public static final int FLAG_EXISTS = 2;//已存在
        public static final int FLAG_FAILED = 3;//创建失败
        /**
         * 创建 文件夹
         * @param dirPath 文件夹路径
         * @return 结果码
         */
        public static int createDir(String dirPath) {

            File dir = new File(dirPath);
            //文件夹是否已经存在
            if (dir.exists()) {
                Log.w(TAG,"The directory [ " + dirPath + " ] has already exists");
                return FLAG_EXISTS;
            }
            if (!dirPath.endsWith(File.separator)) {//不是以 路径分隔符 "/" 结束，则添加路径分隔符 "/"
                dirPath = dirPath + File.separator;
            }
            //创建文件夹
            if (dir.mkdirs()) {
                Log.d(TAG,"create directory [ "+ dirPath + " ] success");
                return FLAG_SUCCESS;
            }

            Log.e(TAG,"create directory [ "+ dirPath + " ] failed");
            return FLAG_FAILED;
        }
    }



    /*確認檔案是否存在*/
    public boolean fileIsExists(String strFile) {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }
    /*建立資料夾*/
    private void folder_construct(){
        //获取 SD 卡路径
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        int result = FileUtils.createDir(path + "/"+folder_input.getText().toString());
        if(result == 1)
            Toast.makeText(this,"資料夾建立成功", Toast.LENGTH_SHORT).show();
        else if(result == 2)
            Toast.makeText(this,"資料夾已存在", Toast.LENGTH_SHORT).show();
        else if(result == 3)
            Toast.makeText(this,"資料夾建立失敗", Toast.LENGTH_SHORT).show();

    }
    /*建立錄音檔案*/
    private void recfile_construct(){
        //輸入錄音檔案名稱後，重新開啟錄音鍵
        record.setEnabled(true);
        if(fileIsExists(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+folder_input.getText().toString()+"/"+"app"+folder_input.getText().toString()+"01_"+recfile_input.getText().toString()+".m4a"))
            Toast.makeText(this,"檔案已存在，錄音將覆蓋原檔案", Toast.LENGTH_SHORT).show();
        else

            return;
    }
    /*錄音*/
    private void record() {
        if (record.isChecked()) {
            // 錄音流程
            try {
                //設定開始錄音時按鈕不能按
                folder_check.setEnabled(false);
                recfile_check.setEnabled(false);
                mediaRecorder = new MediaRecorder();
                //設定音源(一般是麥克風)
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                //設定輸出格式
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                //設定編碼
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                // 設定檔案位置，可以在手機上的檔案管理找到剛剛錄下的聲音
                mediaRecorder.setAudioEncodingBitRate(96000);
                mediaRecorder.setAudioSamplingRate(44100);
                EditText et = (EditText) findViewById(R.id.editText);
                recordFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+folder_input.getText().toString()+"/"+"app"+folder_input.getText().toString()+"01_"+recfile_input.getText().toString()+".m4a");
                mediaRecorder.setOutputFile(recordFile.getAbsolutePath());
                mediaRecorder.prepare();
                mediaRecorder.start();
                Toast.makeText(this,recordFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this,"FileIOException", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            // 停止錄音
            if (mediaRecorder != null) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                //錄音鍵重新設定成不能按
                record.setEnabled(false);
                // 開啟不能按的按鈕
                folder_check.setEnabled(true);
                recfile_check.setEnabled(true);
            }
        }
    }
    private void sound_play(){
        record.setEnabled(false);
        folder_check.setEnabled(false);
        recfile_check.setEnabled(false);

        Uri uri = Uri.fromFile(recordFile.getAbsoluteFile());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
                record.setEnabled(true);
                folder_check.setEnabled(true);
                recfile_check.setEnabled(true);
            }
        });
    }

}