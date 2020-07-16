package xit.zubrein.eexam.challengefriend.model;

public class CreateChallengeSubjectModel {

    String subject_id;
    int value;

    public CreateChallengeSubjectModel(String subject_id, int value) {
        this.subject_id = subject_id;
        this.value = value;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public int getValue() {
        return value;
    }
}
