package com.labbbio.luvas.dao;

import android.content.Context;

import com.google.gson.Gson;
import com.labbbio.luvas.model.ExerciseOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.labbbio.luvas.utils.Constants.OPTIONS_FILE_NAME;


public class ExerciseOptionsDAO {

    private Context context;

    private static ExerciseOptionsDAO uniqueInstance;

    public ExerciseOptionsDAO(Context context) {
        this.context = context;
    }

    public static ExerciseOptionsDAO getInstance(Context context){
        if(uniqueInstance == null){
            uniqueInstance = new ExerciseOptionsDAO(context);
        }
        return uniqueInstance;
    }

    public ExerciseOptions read() {
        try {
            FileInputStream fis = context.openFileInput(OPTIONS_FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            ExerciseOptions exerciseOptions = new Gson().fromJson(jsonObject.toString(), ExerciseOptions.class);
            return exerciseOptions;

        } catch (FileNotFoundException fileNotFound) {
            return null;
        } catch (IOException ioException) {
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean create(ExerciseOptions options){
        String jsonString = new Gson().toJson(options);
        try {
            FileOutputStream fos = context.openFileOutput(OPTIONS_FILE_NAME, Context.MODE_PRIVATE);
            fos.write(jsonString.getBytes());
            fos.close();
            return true;
        } catch (FileNotFoundException fileNotFound) {
            return false;
        } catch (IOException ioException) {
            return false;
        }

    }

    public boolean isFilePresent() {
        String filePath = context.getFilesDir().getAbsolutePath() + "/" + OPTIONS_FILE_NAME;
        File file = new File(filePath);
        return file.exists();
    }
}
