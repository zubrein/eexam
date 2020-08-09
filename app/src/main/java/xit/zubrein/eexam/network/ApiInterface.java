package xit.zubrein.eexam.network;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import xit.zubrein.eexam.category.ModelAvailability;
import xit.zubrein.eexam.challengefriend.model.AcceptedChallengeModel;
import xit.zubrein.eexam.charging.ModelCharging;
import xit.zubrein.eexam.quizsection.model.AnswerModel;
import xit.zubrein.eexam.quizsection.model.QuestionsModel;
import xit.zubrein.eexam.category.CategoryModel;
import xit.zubrein.eexam.challengefriend.model.ChallengeFriendAnswerModel;
import xit.zubrein.eexam.challengefriend.model.CreateChallengeModel;
import xit.zubrein.eexam.challengefriend.model.MyChallengeModel;
import xit.zubrein.eexam.challengefriend.model.RecieveCodeModel;
import xit.zubrein.eexam.signinsignup.model.ModelEmailLogin;
import xit.zubrein.eexam.signinsignup.model.ModelRegister;
import xit.zubrein.eexam.signinsignup.model.ModelSigninSignup;
import xit.zubrein.eexam.signinsignup.model.ModelUser;
import xit.zubrein.eexam.statistics.models.ModelStat;

/**
 * Created by zubrein on 7/15/19.
 */

public interface ApiInterface {


    @FormUrlEncoded
    @POST("send_otp")
    Call<ModelSigninSignup> send_otp(@Field("msisdn") String msisdn);

    @FormUrlEncoded
    @POST("confirm_otp")
    Call<ModelSigninSignup> confirm_otp(@Field("msisdn") String msisdn,@Field("otp") String otp);

    @FormUrlEncoded
    @POST("manual_otp_check")
    Call<ModelSigninSignup> manual_otp_check(@Header("Authorization") String Authorization,@Field("msisdn") String msisdn,@Field("otp") String otp);



    @FormUrlEncoded
    @POST("get_details_by_user_id")
    Call<ModelUser> get_details_by_user_id(@Header("Authorization") String Authorization, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("edit_profile")
    Call<ModelRegister> edit_name(@Header("Authorization") String Authorization,@Field("name") String name,@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("edit_profile")
    Call<ModelRegister> edit_email(@Header("Authorization") String Authorization,@Field("email") String email,@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("edit_profile")
    Call<ModelRegister> edit_city(@Header("Authorization") String Authorization,@Field("city") String city,@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("edit_profile")
    Call<ModelRegister> edit_msisdn(@Header("Authorization") String Authorization,@Field("msisdn") String msisdn,@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("edit_profile")
    Call<ModelRegister> edit_current_study(@Header("Authorization") String Authorization,@Field("current_study") String current_study,@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("register")
    Call<ModelRegister> register(@Field("msisdn") String msisdn, @Field("name") String name, @Field("email") String email,
                                 @Field("city") String city, @Field("current_study") String current_study
            );

    @FormUrlEncoded
    @POST("feedback")
    Call<ModelRegister> send_feedback(@Header("Authorization") String Authorization, @Field("user_id") String user_id,@Field("text") String text
            );



    @FormUrlEncoded
    @POST("register")
    Call<ModelRegister> register_email(@Field("email") String email, @Field("name") String name,
                                 @Field("city") String city, @Field("current_study") String current_study
            );

    @FormUrlEncoded
    @POST("get_user_id_by_email")
    Call<ModelEmailLogin> get_user_id_by_email(@Field("email") String email
    );

    @POST("subjects")
    Call<CategoryModel> get_sebjects(@Header("Authorization") String Authorization);

    @POST("challenge_friend_subjects")
    Call<CategoryModel> get_sebjects_create_challenge(@Header("Authorization") String Authorization);

    @FormUrlEncoded
    @POST("question_for_regular_exam")
    Call<QuestionsModel> getQuestions(@Header("Authorization") String token, @Field("user_id") String user_id,
                                      @Field("subject_id") String subject_id
    );

    @FormUrlEncoded
    @POST("check_availability")
    Call<ModelAvailability> check_availability(@Header("Authorization") String token, @Field("user_id") String user_id);

    @POST("submit_answer")
    Call<AnswerModel> answer_submit(@Header("Authorization") String token, @Body AnswerModel model);

    @POST("get_code")
    Call<RecieveCodeModel> get_code(@Header("Authorization") String token, @Body CreateChallengeModel model);

    @FormUrlEncoded
    @POST("delete_challenge")
    Call<RecieveCodeModel> delete_challenge(@Header("Authorization") String token, @Field("code") String code);

    @FormUrlEncoded
    @POST("get_challenge_question")
    Call<QuestionsModel> getQuestionsChallengefriend(@Header("Authorization") String token, @Field("user_id") String user_id,
                                      @Field("code") String code
    );

    @POST("submit_answer_challenge_question")
    Call<ChallengeFriendAnswerModel> answer_submit_challengeFriend (@Header("Authorization") String token, @Body ChallengeFriendAnswerModel model);

    @FormUrlEncoded
    @POST("get_my_challenge")
    Call<MyChallengeModel> get_my_challenge(@Header("Authorization") String token, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("get_accepted_challenge")
    Call<AcceptedChallengeModel> get_accepted_challenge(@Header("Authorization") String token, @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST("check_subscription")
    Call<ModelCharging> check_subscription(@Header("Authorization") String token, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("subscription_paid")
    Call<ModelCharging> do_subscription(@Header("Authorization") String token, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("cass_charge_regular")
    Call<ModelCharging> charge(@Header("Authorization") String token, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("caas_charge_mobile_number")
    Call<ModelCharging> one_time_payment(@Header("Authorization") String Authorization, @Field("msisdn") String msisdn, @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("get_all_stat")
    Call<List<ModelStat>> get_all_stat(@Header("Authorization") String token, @Field("user_id") String user_id, @Field("subject_id") String subject_id);






}
