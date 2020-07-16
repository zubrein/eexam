package xit.zubrein.eexam.challengefriend.model;

import java.util.List;

public class CreateChallengeModel {

    String user_id;
    List<CreateChallengeSubjectModel> question_category;

    public CreateChallengeModel(String user_id, List<CreateChallengeSubjectModel> question_category) {
        this.user_id = user_id;
        this.question_category = question_category;
    }
}


