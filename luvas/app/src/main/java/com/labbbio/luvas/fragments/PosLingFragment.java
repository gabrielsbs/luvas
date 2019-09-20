/**
 * PosLingFragment: Fragment that contains the list of the pos-linguistic exercises.
 * The exercises must be completed in order. The app blocks if the user tries to do otherwise.
 */

package com.labbbio.luvas.fragments;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;
import com.labbbio.luvas.exercisedb.ExerciseAdapter;
import com.labbbio.luvas.exercisedb.ExerciseItem;

import java.util.ArrayList;

public class PosLingFragment extends Fragment {

    private SQLiteDatabase database;
    private RecyclerView mRecyclerView;
    private ExerciseAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExerciseItem> exerciseItems;

    private int lastExercise;
    private int number = 1;

    String TAG = "POSLINGFRAGMENT";

    private static PosLingFragment instance = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    public static PosLingFragment getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pos_ling, container, false);
        exerciseItems = ((MainActivity) this.getActivity()).getPosExerciseItemsItems();

        setDatabase();
        lastExercise();

        setRecyclerView(view);
        if(exerciseItems.size() == 0)
            createExerciseList();
        return view;
    }

    public void createExerciseList() {
        numbersExercise();
        lowCaseExercises();
        wordExercises1();
        upperCaseExercises();
        wordExercises2();
        accentuationExercises();
        punctuationExercises();
        simbolsExercises();
        delimitersExercises();
        mathExercises();
        phrasesExercises();
        textExercises();
        dialogExercises();
        personalMessagesExercises();
    }

    public void setDatabase() {
        database = ((MainActivity) this.getActivity()).getDatabase();
    }

    public void setRecyclerView(View view) {

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new ExerciseAdapter(exerciseItems);
        mAdapter.setLastExercise(lastExercise);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExerciseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position <= lastExercise)
                    callExerciseFragment(position + 1);
                else
                    lockedQuestion();
            }
        });
    }

    public void lockedQuestion() {
        int question = lastExercise + 1;
        Toast.makeText(this.getContext(), "Ainda não, faça a questão " + question + " primeiro", Toast.LENGTH_SHORT).show();
    }

    // Get the last exercise from the database
    public int lastExercise() {
        String[] column = new String[]{ExerciseItem.LastExerciseEntry.COLUMN_LAST_POSLING};
        Cursor cursor = database.query(ExerciseItem.LastExerciseEntry.TABLE_NAME, column, null, null, null, null, null);
        int ex = cursor.getColumnIndex(ExerciseItem.LastExerciseEntry.COLUMN_LAST_POSLING);
        if (cursor.moveToNext()) {
            lastExercise = cursor.getInt(ex);
            Log.d(TAG, "LastExercise = " + lastExercise);
        }

        return lastExercise;
    }

    public int getLastExercise(){
        return lastExercise;
    }

    // Uptade the database
    public void updateLastExercise() {
        lastExercise = lastExercise + 1;
        String command = "UPDATE " +
                ExerciseItem.LastExerciseEntry.TABLE_NAME +
                " SET " + ExerciseItem.LastExerciseEntry.COLUMN_LAST_POSLING +
                " = " + lastExercise;

        database.execSQL(command);
    }

    //Call the MainActivity function that start the ExerciseFragment
    public void callExerciseFragment(int questionNumber) {
        Log.d(TAG, "LastExercise = " + lastExercise);
        ((MainActivity) this.getActivity()).exerciseFragmentStart("PosLing" ,questionNumber);
    }

    // In the following function, the exercises are incluided in the list
    public void numbersExercise() {
        String question;
        String answer;
        String questionType;

        /*Emissão de números de 1 digito em ordem*/

        for(int i = 0; i<10; i++){
            question = "Digite o número: "+Integer.toString(i);
            answer = Integer.toString(i);
            questionType = "Emission";
            exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
            number = number + 1;
        }
        /*Recepção de número de 1 digito em ordem*/
        for(int i = 0; i<10; i++){
            question = "Digite o número: "+Integer.toString(i);
            answer = Integer.toString(i);
            questionType = "Reception";
            exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
            number = number + 1;
        }

        question = "Digite o número 7";
        answer = "7";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o número 5";
        answer = "5";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o número 0";
        answer = "0";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o número 3";
        answer = "3";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o número 9";
        answer = "9";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o número 2";
        answer = "2";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o número 4";
        answer = "4";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o número 6";
        answer = "6";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        /* Recepção de números de 1 digito*/
        question = "Qual é o número";
        answer = "2";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é o número";
        answer = "9";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é o número";
        answer = "0";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é o número";
        answer = "6";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é o número";
        answer = "7";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é o número";
        answer = "4";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        /* Emissão de número de 2 digitos*/
        question = "Digite o número 38";
        answer = "38";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o número 42";
        answer = "42";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o número 19";
        answer = "19";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        /*Recepção de números com 2 digitos*/
        question = "Qual é o número";
        answer = "38";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é o número";
        answer = "72";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é o número";
        answer = "50";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        /*Emissão de número com >3 digitos*/
        question = "Digite o número 432";
        answer = "432";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o número 569";
        answer = "569";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o número 9258";
        answer = "9258";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));

        /*Recepção de número com >3 digitos*/

        question = "Qual é o número";
        answer = "145";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é o número";
        answer = "648";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é o número";
        answer = "3982";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;
    }

    public void lowCaseExercises() {
        String answer;
        String question;

        /*Emissão de letras minúsculas*/
        String questionType = "Emission";
        for (int i = 1; i <= 26; i++) {
            answer = Character.toString((char) (i + 96)); // letra a corresponde a 97 na tabela ascii, 96+1 = 97 = a, depois 96+2 = 98 = b, assim em diante
            question = "Digite a letra \"" + answer + "\"";
            exerciseItems.add(new ExerciseItem(number + i, question, answer, questionType));
        }

        number = number + 27;
        answer = "ç";
        question = "Digite a letra ç";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));

        number = number + 1;
        answer = Character.toString((char) (8)); // espaço em ascii
        question = "Digite \nespaço";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));

        /*Recepção de letras minúsculas*/
        questionType = "Reception";
        for (int i = 1; i <= 26; i++) {
            answer = Character.toString((char) (i + 96)); // letra a corresponde a 97 na tabela ascii, 96+1 = 97 = a, depois 96+2 = 98 = b, assim em diante
            question = "Digite a letra \"" + answer + "\"";
            exerciseItems.add(new ExerciseItem(number + i, question, answer, questionType));
        }
        number = number + 27;
    }

    public void wordExercises1() {
        String answer;
        String question;
        String questionType;

        /*Exercícios de emissão*/
        question = "Digite a palavra lata";
        answer = "lata";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a palavra bola";
        answer = "bola";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a palavra suco";
        answer = "suco";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a palavra menino";
        answer = "menino";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        /*Exercícios de recepção silabas simples*/
        question = "Qual é a palavra?(mala)";
        answer = "mala";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é a palavra?(pato)";
        answer = "pato";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é a palavra?(moto)";
        answer = "moto";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é a palavra?(caneta)";
        answer = "caneta";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        /*Exercícios de emissão silabas com mais de 2 letras*/
        question = "Digite a palavra folha";
        answer = "folha";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a palavra chuva";
        answer = "chuva";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a palavra caminho";
        answer = "caminho";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));

        /*Exercícios de recepção silabas com mais de duas letras*/
        question = "Qual é a palavra?(porta)";
        answer = "porta";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é a palavra?(colher)";
        answer = "colher";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é a palavra?(palhaço)";
        answer = "palhaço";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));


    }

    public void upperCaseExercises() {
        String answer;
        String question;
        String questionType = "Emission";
        for (int i = 1; i <= 26; i++) {
            answer = Character.toString((char) (i + 64)); // letra A corresponde a 65 na tabela ascii, 64+1 = 65 = A, depois 64+2 = 66 = B, assim em diante
            question = "Digite a letra \"" + answer + "\"";
            exerciseItems.add(new ExerciseItem(number + i, question, answer,questionType));
        }
        number = number + 26;

    }

    public void wordExercises2() {
        String answer;
        String question;
        String questionType;

        /*Exercicios de emissão*/
        question = "Digite a palavra Taciana";
        answer = "Taciana";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \npalavra \nMaria \nCarolina";
        answer = "Maria Carolina";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \npalavra \nBelo \nHorizonte";
        answer = "Belo Horizonte";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \npalavra \nUFMG";
        answer = "UFMG";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \npalavra CEP";
        answer = "CEP";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \npalavra \nCPF";
        answer = "CPF";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;
    }

    public void accentuationExercises() {
        String answer;
        String question;
        String questionType;

        question = "Digite o \nacento \nagudo ´";
        answer = "´";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a crase `";
        answer = "`";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nacento \ncircunflexo ^";
        answer = "^";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o til ~";
        answer = "~";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite as aspas " + Character.toString((char) (8));
        answer = Character.toString((char) (34));
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \npalavra \nlápis";
        answer = "lápis";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \npalavra \nlâmpada";
        answer = "lâmpada";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \npalavra mão";
        answer = "mão";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        /*Recepção de palavras acentuadas*/

        question = "Qual é a palavra?(café)";
        answer = "café";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é a palavra?(maçã)";
        answer = "maçã";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é a palavra?(fogão)";
        answer = "fogão";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));


        /*Emissão de sílabas com til*/
        question = "Digite a \nsilaba ão";
        answer = "ão";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \nsilaba ães";
        answer = "ães";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;


        question = "Qual é a palavra?(café)";
        answer = "café";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é a palavra?(maçã)";
        answer = "maçã";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é a palavra?(fogão)";
        answer = "fogão";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \npalavra pão";
        answer = "pão";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \npalavra mãos";
        answer = "mãos";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \npalavra \nlimões";
        answer = "limões";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        /*Recepção de palavras terminadas com ão e õe*/

        question = "Qual é a palavra?(sabão)";
        answer = "sabão";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é a palavra?(botões)";
        answer = "botões";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

    }

    public void punctuationExercises() {
        String answer;
        String question;
        String questionType;

        question = "Digite o \nponto \nfinal (.)";
        answer = ".";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \nvírgula (,)";
        answer = ",";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nponto e \nvírgula (;)";
        answer = ";";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite dois \npontos (:)";
        answer = ":";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite \nponto de \ninterrogação (?)";
        answer = ":";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite \nponto de \nexclamação (!)";
        answer = "!";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite a \nsequência, \nincluindo as \nvírgulas: 1,2,3";
        answer = "1,2,3";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite:\n" + " ponto.";
        answer = "ponto.";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite: \nO que?";
        answer = "O que?";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite: \nNossa!";
        answer = "Nossa!";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        /*Recepção*/

        question = "Qual é a palavra?(Sexta, sábado)";
        answer = "Sexta, sábado";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é a palavra?(Tchau.)";
        answer = "Tchau.";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Qual é a palavra?(Tudo bem?)";
        answer = "Tudo bem?";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

    }

    public void simbolsExercises() {
        String answer;
        String question;
        String questionType;

        question = "Digite o \nsímbolo de \narroba (@)";
        answer = "@";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nsímbolo de \nhashtag (#)";
        answer = "#";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nsímbolo de \nunderline (_)";
        answer = "_";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nsímbolo de \ncifrão ($)";
        answer = "$";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nsímbolo de \nporcentagem (%)";
        answer = "%";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nsímbolo &";
        answer = "&";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nsímbolo de \nasterisco (*)";
        answer = "*";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nsímbolo de \ngrau (º)";
        answer = "º";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite: \nana_12@gmail.com";
        answer = "ana_12@gmail.com";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite: \n#foratemer";
        answer = "#foratemer";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite: \nR$100,00";
        answer = "R$100,00";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite: 50%";
        answer = "50%";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;
    }

    public void delimitersExercises() {
        String answer;
        String question;
        String questionType;

        question = "Digite aspas \n(\")";
        answer = "\"";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite \nparentese \nesquerdo \"(\"";
        answer = "(";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite \ncolchete \nesquerdo ([)  ";
        answer = "[";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite chave \nesquerda ({)";
        answer = "[";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite \nbarra (/)";
        answer = "/";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite barra \ninvertida (\\)";
        answer = "\\";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite chave \ndireita (})";
        answer = "}";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite \ncolchete \ndireito (])";
        answer = "]";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite \nparentese \ndireito \" )\" ";
        answer = ")";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite \naspas \nesquerda (\")";
        answer = "\"";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite aspas \nsimples (\')";
        answer = "\'";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite aspas (\")";
        answer = "\"";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite: \nminúsculas \n(ativador 12)";
        answer = "minúsculas (ativador 12)";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite: \n\"verdade\"";
        answer = "\"verdade\"";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;
    }

    public void mathExercises() {
        String answer;
        String question;
        String questionType;

        question = "Digite o \nsinal de \nsoma (+)";
        answer = "+";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nsinal de \nsubtração (-)";
        answer = "-";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nsinal de  \nmultiplicação (*)  ";
        answer = "*";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nsinal de \ndivisão (/)";
        answer = "/";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nsinal de \nigual (=)";
        answer = "=";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;


        question = "Digite o \nsinal de \nmenor que (<)";
        answer = "<";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite o \nsinal de \nmaior que (>)";
        answer = "]";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite: \n4+5=9 ";
        answer = "4+5=9";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite: \n16/4=4";
        answer = "16/4=4";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite: \n13<14";
        answer = "13<14";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;
    }

    public void phrasesExercises() {

        String answer;
        String question;
        String questionType;

        question = "Digite: \nEscolha ser \nfeliz!";
        answer = "Escolha ser feliz!";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite: \nAquele dia \nvai ficar \nna lembrança.";
        answer = "Aquele dia vai ficar na lembrança.";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        /*Recepção*/
        question = "Qual é a frase?(¿Você vem, no próximo dia de teste?)";
        answer = "¿Você vem, no próximo dia de teste?";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

    }

    public void dialogExercises(){
        String answer;
        String question;
        String questionType;

        question = "Responda a pergunta que irá receber.(¿Oi, tudo bem?)";
        answer = "¿Oi, tudo bem?";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Responda a pergunta que irá receber.(¿Qual é o seu endereço?)";
        answer = "¿Qual é o seu endereço?";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Responda a pergunta que irá receber.(¿Me passa o seu whatsapp?)";
        answer = "¿Me passa o seu whatsapp?";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Responda a pergunta que irá receber.(Até a próxima. Estude bastante e juízo, hein?)";
        answer = "Até a próxima. Estude bastante e juízo, hein?";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;


    }

    public void textExercises(){

        String answer;
        String question;
        String questionType;

        question = "Digite: Essa vida \né um mistério. \nNão sabemos de \nonde viemos, \nnem para onde \nvamos. De onde \nvem nossos \npensamentos?";
        answer = "Essa vida é um mistério. Não sabemos de onde viemos, nem para onde vamos. De onde vem nossos pensamentos?";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "O que está recebendo?";
        answer = "Você sabe com quem está lidando? Com uma pessoa forte, que usa os obstáculos como esada para subir na vida";
        questionType = "Reception";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;


    }

    public void personalMessagesExercises(){
        String answer;
        String question;
        String questionType;

        question = "Digite seu nome";
        answer = "nome";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite seu CPF";
        answer = "CPF";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite seu CPF";
        answer = "CPF";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite seu CPF";
        answer = "CPF";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite seu CPF";
        answer = "CPF";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite seu CPF";
        answer = "CPF";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite seu CPF";
        answer = "CPF";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite seu CPF";
        answer = "CPF";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite seu CPF";
        answer = "CPF";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;

        question = "Digite seu CPF";
        answer = "CPF";
        questionType = "Emission";
        exerciseItems.add(new ExerciseItem(number, question, answer, questionType));
        number = number + 1;
    }

}
