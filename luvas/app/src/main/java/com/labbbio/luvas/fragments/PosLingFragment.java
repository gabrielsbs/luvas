package com.labbbio.luvas.fragments;

import android.content.ContentValues;
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
    private ArrayList<ExerciseItem> exerciseItems = new ArrayList<>();

    private int lastExercise;
    private int number =1;

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

        setDatabase();
        getLastExercise();

        setRecyclerView(view);
        createExerciseList();
        addExercisesToDB();

        return view;


    }


    public void createExerciseList(){
        numbersExercise();
        lowCaseExercises();
        wordExercises1();
        upperCaseExercises();
        wordExercises2();
        accentuationExercises();
        pontuationExercises();
        simbolsExercises();
        delimitersExercises();
        mathExercises();
        phrasesExercises();
        mAdapter.notifyDataSetChanged();

    }

    public void addExercisesToDB(){
        for(ExerciseItem item: exerciseItems){
            int qNumber = item.getExerciseNumber();
            String question = item.getQuestion();
            String answer = item.getAnswer();
            ContentValues cv = new ContentValues();
            cv.put(ExerciseItem.ExerciseEntry.COLUMN_NUMBER,qNumber);
            cv.put(ExerciseItem.ExerciseEntry.COLUMN_QUESTION,question);
            cv.put(ExerciseItem.ExerciseEntry.COLUMN_ANSWER,answer);
            database.insert(ExerciseItem.ExerciseEntry.POSLING_TABLE_NAME,null,cv);
        }
    }

    public void setDatabase(){
        database = ((MainActivity)this.getActivity()).getDatabase();
    }

    public void setRecyclerView(View view){

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
                if(position<=lastExercise)
                    callExerciseFragment(position+1);
                else
                    lockedQuestion();

            }
        });
    }


    public void lockedQuestion(){
        int question = lastExercise +1;
        Toast.makeText(this.getContext(), "Ainda não, faça a questão "+question+" primeiro", Toast.LENGTH_SHORT).show();
    }

    public int getLastExercise(){
        String[] column = new String[]{ExerciseItem.LastExerciseEntry.COLUMN_LAST_POSLING};
        Cursor cursor = database.query(ExerciseItem.LastExerciseEntry.TABLE_NAME,column,null,null,null,null,null);
        int ex = cursor.getColumnIndex(ExerciseItem.LastExerciseEntry.COLUMN_LAST_POSLING);
        if(cursor.moveToNext()){
            lastExercise = cursor.getInt(ex);
            Log.d(TAG,"LastExercise = "+lastExercise);
        }

        return lastExercise;
    }

    public void updateLastExercise(){
        lastExercise = lastExercise+1;
        String command = "UPDATE " +
                ExerciseItem.LastExerciseEntry.TABLE_NAME +
                " SET " + ExerciseItem.LastExerciseEntry.COLUMN_LAST_POSLING +
                " = "+ lastExercise;

        database.execSQL(command);
    }

    public void callExerciseFragment(int questionNumber){
        Log.d(TAG,"LastExercise = "+lastExercise);
        ((MainActivity) this.getActivity()).exerciseFragmentStart(ExerciseItem.ExerciseEntry.POSLING_TABLE_NAME, questionNumber);
    }

    public void numbersExercise(){

        String question = "Digite a sequenência 123456789";
        String answer = "123456789";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 2
        question = "Digite o número 7";
        answer = "7";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 3
        question = "Digite o número 5";
        answer = "5";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 4
        question = "Digite o número 0";
        answer = "0";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 5
        question = "Digite o número 3";
        answer = "3";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 6
        question = "Digite o número 9";
        answer = "9";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 7
        question = "Digite o número 2";
        answer = "2";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 8
        question = "Digite o número 4";
        answer = "4";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 9
        question = "Digite o número 6";
        answer = "6";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 10
        question = "Digite o número 38";
        answer = "38";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 11
        question = "Digite o número 42";
        answer = "42";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 12
        question = "Digite o número 19";
        answer = "19";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 13
        question = "Digite o número 432";
        answer = "432";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 14
        question = "Digite o número 569";
        answer = "569";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        //Questão 15
        question = "Digite o número 9258";
        answer = "9258";
        exerciseItems.add(new ExerciseItem(number,question,answer));
    }

    public void lowCaseExercises(){
        String answer;
        String question;
        for(int i = 1; i<=26; i++){
            answer = Character.toString((char) (i+96)); // letra a corresponde a 97 na tabela ascii, 96+1 = 97 = a, depois 96+2 = 98 = b, assim em diante
            question = "Digite a letra \""+answer+"\"";
            exerciseItems.add(new ExerciseItem(number+i,question,answer));
        }
        number = number +27;
        answer = "ç" ;
        question = "Digite a letra ç";
        exerciseItems.add(new ExerciseItem(number,question,answer));

        number = number +1;
        answer = Character.toString((char) (8)); // espaço em ascii
        question = "Digite a letra espaço";
        exerciseItems.add(new ExerciseItem(number,question,answer));
    }

    public void wordExercises1(){
        String answer;
        String question;
        number = number +1;
        question = "Digite a palavra lata";
        answer = "lata";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra bola";
        answer = "bola";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra suco";
        answer = "suco";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra menino";
        answer = "menino";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra folha";
        answer = "folha";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra chuva";
        answer = "chuva";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra caminho";
        answer = "caminho";
        exerciseItems.add(new ExerciseItem(number,question,answer));
    }

    public void upperCaseExercises(){
        String answer;
        String question;
        for(int i = 1; i<=26; i++){
            answer = Character.toString((char) (i+64)); // letra A corresponde a 65 na tabela ascii, 64+1 = 65 = A, depois 64+2 = 66 = B, assim em diante
            question = "Digite a letra \""+answer+"\"";
            exerciseItems.add(new ExerciseItem(number+i,question,answer));
        }

        number = number+27;

    }

    public void wordExercises2(){
        String answer;
        String question;

        question = "Digite a palavra Taciana";
        answer = "Taciana";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra Maria Carolina";
        answer = "Maria Carolina";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra Belo Horizonte";
        answer = "Belo Horizonte";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra UFMG";
        answer = "UFMG";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra CEP";
        answer = "CEP";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra CPF";
        answer = "CPF";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;
    }

    public void accentuationExercises(){
        String answer;
        String question;

        question = "Digite o acento agudo ´";
        answer = "´";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a crase `";
        answer = "`";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o acento circunflexo ^";
        answer = "^";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o til ~";
        answer = "~";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite as aspas "+Character.toString((char) (8));
        answer = Character.toString((char) (34));
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra lápis";
        answer = "lápis";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra lâmpada";
        answer = "lâmpada";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra mão";
        answer = "mão";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra ão";
        answer = "ão";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra ães";
        answer = "ães";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra ãos";
        answer = "ãos";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra õe";
        answer = "õe";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra ões";
        answer = "ões";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra pão";
        answer = "pão";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra mãos";
        answer = "mãos";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a palavra limões";
        answer = "limões";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

    }

    public void pontuationExercises(){
        String answer;
        String question;

        question = "Digite o ponto final (.)";
        answer = ".";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a vírgula (,)";
        answer = "`";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o ponto e vírgula (;)";
        answer = ";";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite dois pontos (:)";
        answer = ":";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite ponto de interrogaçãp (?)";
        answer = ":";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite ponto de exclamação (!)";
        answer = "!";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite a sequência, incluindo as vírgulas: 1,2,3";
        answer = "1,2,3";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite:"+ " ponto.";
        answer = "ponto.";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: o que?";
        answer = "o que?";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: Nossa!";
        answer = "Nossa!";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

    }

    public void simbolsExercises(){
        String answer;
        String question;

        question = "Digite o símbolo de arroba (@)";
        answer = "@";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o símbolo de hashtag (#)";
        answer = "#";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o símbolo de underline (_)";
        answer = "_";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o símbolo de cifrão ($)";
        answer = "$";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o símbolo de porcentagem (%)";
        answer = "%";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o símbolo &";
        answer = "&";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o símbolo de asterisco (*)";
        answer = "*";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o símbolo de grau (º)";
        answer = "º";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: ana_12@gmail.com";
        answer = "ana_12@gmail.com";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: #foratemer";
        answer = "#foratemer";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: R$100,00";
        answer = "R$100,00";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: 50%";
        answer = "50%";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;
    }

    public void delimitersExercises(){
        String answer;
        String question;

        question = "Digite aspas (\")";
        answer = "\"";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite parenteses esquerdo \"(\"";
        answer = "(";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite colchete esquerdo ([)  ";
        answer = "[";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite chave esquerda ({)";
        answer = "[";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite barra (/)";
        answer = "/";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite barra invertida (\\)";
        answer = "\\";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite  chave direita (})";
        answer = "}";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite colchete direito (])";
        answer = "]";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite parentese direito \" )\" ";
        answer = ")";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite aspas esquerda (\")";
        answer = "\"";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite aspas simples (\')";
        answer = "\'";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite aspas (\")";
        answer = "\"";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: minúsculas (ativador 12)";
        answer = "minúsculas (ativador 12)";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: \"verdade\"";
        answer = "\"verdade\"";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;
    }

    public void mathExercises(){
        String answer;
        String question;

        question = "Digite o sinal de soma (+)";
        answer = "+";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o sinal de subtração (-)";
        answer = "-";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o sinal de  multiplicação (*)  ";
        answer = "*";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o sinal de divisão (/)";
        answer = "/";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o sinal de igual (=)";
        answer = "=";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o sinal de diferente (\\)";
        answer = "\\";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite  o sinal de menor que (<)";
        answer = "<";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite o sinal de maior que (>)";
        answer = "]";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: 4+5=9 ";
        answer = "4+5=9";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: 16/4=4";
        answer = "16/4=4";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: 13<14";
        answer = "13<14";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;
    }

    public void phrasesExercises(){

        String answer;
        String question;

        question = "Digite: Escolha ser feliz!";
        answer = "Escolha ser feliz!";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: Aquele dia vai ficar na lembrança.";
        answer = "Aquele dia vai ficar na lembrança.";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

        question = "Digite: Essa vida é um mistério. Não sabemos de onde viemos, nem para onde vamos. De onde vem nossos pensamentos?";
        answer = "Essa vida é um mistério. Não sabemos de onde viemos, nem para onde vamos. De onde vem nossos pensamentos?";
        exerciseItems.add(new ExerciseItem(number,question,answer));
        number = number+1;

    }
}
