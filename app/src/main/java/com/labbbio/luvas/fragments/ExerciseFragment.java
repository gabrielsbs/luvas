    package com.labbbio.luvas.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureStore;
import android.gesture.Prediction;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;
import com.labbbio.luvas.model.Exercise;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class ExerciseFragment extends Fragment implements GestureOverlayView.OnGesturePerformedListener {

    private String TAG = "ExerciseFragment";

    private SQLiteDatabase database;
    private TextView questionView;
    private EditText answerView;
    private ImageButton send;
    private GestureLibrary gLibrary;
    private GestureLibrary gLibraryTripleStrokes;
    private GestureOverlayView mView;

    private StringBuilder messages;
    private String ExerciseType;
    private String answer;
    private String answerTried;
    private int questionNumber;
    private String questionType;
    private ArrayList<Exercise> exerciseItems, temp;

    private boolean saveMode = false;

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private static final int VOICE_OPTION = 5;
    private static final int GESTURE_OPTION = 6;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);

        questionView = view.findViewById(R.id.textview_question);
        answerView = view.findViewById(R.id.answer);
        send = view.findViewById(R.id.button_send);

        File file = new File(this.getContext().getFilesDir() + "/" + "gesture.txt");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File file2 = new File(this.getContext().getFilesDir() + "/" + "gesture2.txt");
        if(!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        gLibrary = GestureLibraries.fromFile( this.getContext().getFilesDir() + "/" + "gesture.txt");
        gLibrary.load();

        gLibraryTripleStrokes = GestureLibraries.fromFile( this.getContext().getFilesDir() + "/" + "gesture2.txt");
        gLibraryTripleStrokes.load();
        messages = new StringBuilder();

        ExerciseType = getArguments().getString("ExerciseType");
        questionNumber = getArguments().getInt("questionNumber");

        if(temp != null){
            exerciseItems = temp;
        }else{
            exerciseItems = ((MainActivity) this.getActivity()).getPosExerciseItemsItems();
        }

        int option = ((MainActivity) this.getActivity()).getAnswerOption();

        this.getActivity().setTitle("Exercício " + questionNumber);

        getQuestionText();

        if (questionType.equals("Emission")) {

            if (answerView.requestFocus()) {
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(
                        InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY
                );
            }
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Button pressed");
                    String answerTried = answerView.getText().toString();
                    answerView.getText().clear();
                    if (answerTried.equals(answer)) {
                        correctAnswer();
                    } else {
                        wrongAnswer();
                    }

                }
            });

        } else if (questionType.equals("Reception")) {

            ((MainActivity) this.getActivity()).sendMessage(answer);
            if(option == VOICE_OPTION )
                startVoiceRecognitionActivity();
            else if(option == GESTURE_OPTION){
                mView =  view.findViewById(R.id.gestures);
                mView.addOnGesturePerformedListener(this);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Button pressed");
                        String answerTried = answerView.getText().toString();
                        answerView.getText().clear();
                        if (answerTried.equals(answer)) {
                            correctAnswer();
                        } else {
                            wrongAnswer();
                        }

                    }
                });
            }
        }

        IntentFilter intentF = new IntentFilter("incomingMessage");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(incomingMessageReceiver, intentF);
        return view;
    }

    private BroadcastReceiver incomingMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Incoming", "Broadcast " + "OK");
            String text = intent.getStringExtra("message");
            String s = "\\n";
            if (text.equals(s)) {
                Log.d(TAG, "Enter Received");
                String answerTried = answerView.getText().toString();
                answerView.getText().clear();
                if (answerTried.equals(answer)) {
                    correctAnswer();
                } else {
                    wrongAnswer();
                }
            } else {
                text = text.substring(0, 1);
                messages.append(text);
                answerView.setText(messages.toString());
            }

        }
    };


    private void correctAnswer() {
        ((MainActivity) this.getActivity()).sendMessage("Correct");
        goToNextQuestion();
    }

    private void wrongAnswer() {
        ((MainActivity) this.getActivity()).sendMessage("Wrong");
        ((MainActivity) this.getActivity()).refreshExerciseFragment();
        Toast.makeText(this.getContext(), "Reposta Errada", Toast.LENGTH_SHORT).show();
    }

    private void goToNextQuestion() {
        if(questionNumber > PosLingFragment.getInstance().getLastExercise()){
          //  PosLingFragment.getInstance().updateLastExercise();
        }
        ((MainActivity) this.getActivity()).exerciseFragmentStart(ExerciseType, questionNumber + 1);
    }

    private void getQuestionText() {
        Exercise question = exerciseItems.get(questionNumber - 1);
        String text = question.getQuestion();
        questionType = question.getQuestionType();
        Log.d(TAG, text);
        questionView.setText(text);
        answer = question.getAnswer();

    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            answerTried = matches.get(0);
            answerView.setText(answerTried);
            if(answerTried.equals(answer))
                goToNextQuestion();
            else
                wrongAnswer();
        }
    }

    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        if (saveMode) {
            String gestureName = answerView.getText().toString();
            if (gesture.getStrokesCount() > 2) {
                gLibraryTripleStrokes.addGesture(gestureName, gesture);
                if (!gLibraryTripleStrokes.save()) {
                    Log.e(TAG, "gesture not saved!");
                }else {
                    Log.d(TAG, "save triple stroke gesture: " + gestureName);
                    Toast.makeText(this.getContext(), "save gesture: " + gestureName, Toast.LENGTH_LONG);
                }
            } else {
                gLibrary.addGesture(gestureName, gesture);
                if (!gLibrary.save()) {
                    Log.e(TAG, "gesture not saved!");
                }else {
                    Log.d(TAG, "save gesture: " + gestureName);
                    Log.d(TAG, "with number of strokes of: " + gesture.getStrokesCount());
                    Toast.makeText(this.getContext(), "save gesture: " + gestureName, Toast.LENGTH_LONG);
                }
            }

        } else {
            ArrayList<Prediction> predictions =  gesture.getStrokesCount() > 2 ? gLibraryTripleStrokes.recognize(gesture) : gLibrary.recognize(gesture) ;
            if (predictions.size() > 0) {
                Prediction prediction = predictions.get(0);
                String letter = prediction.name;


                if (prediction.score > 1.0) {
                    answerView.setText(answerView.getText().toString() + letter);
                }
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "Destroy");
        temp = exerciseItems;
        this.getActivity().setTitle("Mensageiro Luvas");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(incomingMessageReceiver);
    }
}
