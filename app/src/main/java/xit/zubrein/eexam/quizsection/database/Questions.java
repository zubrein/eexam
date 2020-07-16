package xit.zubrein.eexam.quizsection.database;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by zubrein on 7/15/19.
 */

@Entity(tableName = "questions_table")
public class Questions {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "question_id")
    private String question_id;

    @NonNull
    @ColumnInfo(name = "question")
    private String question;

    @Nullable
    @ColumnInfo(name = "answer")
    private String answer;

    @Nullable
    @ColumnInfo(name = "opt1")
    private String optA;

    @Nullable
    @ColumnInfo(name = "opt2")
    private String optB;

    @Nullable
    @ColumnInfo(name = "opt3")
    private String optC;

    @Nullable
    @ColumnInfo(name = "opt4")
    private String optD;


    public Questions(@NonNull String question_id,@NonNull String question, @Nullable String opta, @Nullable String optb, @Nullable String optc, @Nullable String optd, @Nullable String answer) {
        this.question = question;
        this.question_id = question_id;
        this.optA = opta;
        this.optB = optb;
        this.optC = optc;
        this.optD = optd;
        this.answer = answer;
    }

    public Questions()
    {
        id=0;
        question_id="";
        question="";
        optA="";
        optB="";
        optC="";
        optD="";
        answer="";
    }

    public int getId(){return this.id;}

    public void setId(int id){
        this.id = id;
    }

    @NonNull
    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(@NonNull String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion(){return this.question;}

    public void setQuestion(String question){
        this.question = question;
    }

    public String getAnswer(){return this.answer;}

    public void setAnswer(String answer){
        this.answer = answer;
    }

    public String getOptA(){return this.optA;}

    public void setOptA(String opta){
        this.optA = opta;
    }

    public String getOptB(){return this.optB;}

    public void setOptB(String optb){
        this.optB = optb;
    }

    public String getOptC(){return this.optC;}

    public void setOptC(String optc){
        this.optC = optc;
    }

    public String getOptD(){return this.optD;}

    public void setOptD(String optd){
        this.optD = optd;
    }
}
