package xit.zubrein.eexam.category;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.charging.ChargingRepository;
import xit.zubrein.eexam.charging.ModelCharging;
import xit.zubrein.eexam.charging.PaymentActivity;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.quizsection.QuizActivity;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.quizsection.model.QuestionsModel;
import xit.zubrein.eexam.utils.CustomToast;
import xit.zubrein.eexam.utils.LoadingBar;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<CategoryModel.subjects> subjectList;
    Context c;
    String user_id = "", token = "";
    SharedPreferences sharedPreferences;
    LoadingBar loadingBar;
    Activity activity;
    ChargingRepository chargingRepository;
    private static final String TAG = "CategoryAdapter";

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSubject;
        public LinearLayout parent;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSubject = itemView.findViewById(R.id.txtSubject);
            parent = itemView.findViewById(R.id.parent);
        }
    }

    public CategoryAdapter(List<CategoryModel.subjects> examplelist, Activity activity, Context c) {
        this.subjectList = examplelist;
        this.activity = activity;
        this.c = c;
        sharedPreferences = c.getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        token = sharedPreferences.getString("token", "");
        loadingBar = new LoadingBar();
        chargingRepository = new ChargingRepository(activity);


    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        CategoryViewHolder evh = new CategoryViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        final CategoryModel.subjects currentitem = subjectList.get(position);
        holder.txtSubject.setText(currentitem.getSubject_name());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAvailibity(currentitem.getId());
                Log.d(TAG, "checkAvailibity: "+user_id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjectList.size();

    }

    public void checkAvailibity(final String subject) {

        loadingBar.showDialog(activity);
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<QuestionsModel> call = service.getQuestions(token, user_id, subject);
        call.enqueue(new Callback<QuestionsModel>() {
            @Override
            public void onResponse(Call<QuestionsModel> call, Response<QuestionsModel> response) {
                if (response.isSuccessful()) {
                    loadingBar.cancelDialog();
                    QuestionsModel questionsModel = response.body();
                    if (questionsModel.isAvailability()) {
                        ViewDialog viewDialog = new ViewDialog();
                        viewDialog.showDialog(activity, "Your next exam is totally free. Do you want to proceed ?", "available", subject,questionsModel.getStatus_code());
                    } else {
                        ViewDialog viewDialog = new ViewDialog();
                        viewDialog.showDialog(activity, "You have reached your free limit. Next exam will cost 2tk + vat + sd. Do you want to proceed ?", "not_available", subject,questionsModel.getStatus_code());
                    }

                } else {
                    Toast.makeText(c, "no response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuestionsModel> call, Throwable t) {

            }
        });
    }

    public class ViewDialog {
        public void showDialog(Activity activity, String messages, final String type, final String subject, final int code) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView header = dialog.findViewById(R.id.dialog_message_header);
            header.setText("");
            TextView message = dialog.findViewById(R.id.dialog_message);
            message.setText(messages);
            FrameLayout mDialogNo = dialog.findViewById(R.id.frmNo);
            mDialogNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            FrameLayout mDialogOk = dialog.findViewById(R.id.frmOk);
            mDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                    if (type.equals("available")) {
                        Intent intent = new Intent(c, QuizActivity.class);
                        intent.putExtra("subject_id", subject);
                        intent.putExtra("quiz_type", "regular");
                        c.startActivity(intent);
                    } else if (type.equals("not_available")) {
                        if (code == 513){
                            c.startActivity(new Intent(c, PaymentActivity.class));
                        }else{
                            check_sub(subject);
                        }

                    } else if (type.equals("subscribe")) {
                        do_charge(subject);
                    }

                }
            });

            dialog.show();
        }
    }

    public void check_sub(final String subject) {
        loadingBar = new LoadingBar();
        loadingBar.showDialog(activity);

        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelCharging> call = service.check_subscription(token, user_id);
        call.enqueue(new Callback<ModelCharging>() {
            @Override
            public void onResponse(Call<ModelCharging> call, Response<ModelCharging> response) {
                if (response.isSuccessful()) {
                    ModelCharging model = response.body();
                    String status = model.getSubscription_status();
                    if (status.equals("REGISTERED")) {
                        loadingBar.cancelDialog();
                        do_charge(subject);
                    } else {
                        loadingBar.cancelDialog();
                        ViewDialog viewDialog = new ViewDialog();
                        viewDialog.showDialog(activity, "You aren't subscribed. Do you want to subscribe ? (Subscription is totally free)", "subscribe", subject,200);
                    }

                }

            }

            @Override
            public void onFailure(Call<ModelCharging> call, Throwable t) {
            }
        });


    }

    public void do_charge(final String subject) {
        loadingBar = new LoadingBar();
        loadingBar.showDialog(activity);
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelCharging> call = service.charge(token, user_id);
        call.enqueue(new Callback<ModelCharging>() {
            @Override
            public void onResponse(Call<ModelCharging> call, Response<ModelCharging> response) {
                if (response.isSuccessful()) {
                    ModelCharging model = response.body();
                    String data = model.getStatus_code();

                    if (data.equals("200")) {
                        loadingBar.cancelDialog();
                        Intent intent = new Intent(c, QuizActivity.class);
                        intent.putExtra("subject_id", subject);
                        intent.putExtra("quiz_type", "regular");
                        c.startActivity(intent);
                    } else {
                        loadingBar.cancelDialog();
                        CustomToast.showCustomToast(activity, "You don't have sufficient balance");
                    }

                }

            }

            @Override
            public void onFailure(Call<ModelCharging> call, Throwable t) {
            }
        });


    }

}
