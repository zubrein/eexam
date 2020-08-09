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
import android.os.Handler;
import java.util.logging.LogRecord;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xit.zubrein.eexam.charging.ChargingRepository;
import xit.zubrein.eexam.charging.ModelCharging;
import xit.zubrein.eexam.charging.PaymentActivity;
import xit.zubrein.eexam.network.ApiClient;
import xit.zubrein.eexam.network.ApiInterface;
import xit.zubrein.eexam.profile.ProfileActivity;
import xit.zubrein.eexam.quizsection.QuizActivity;
import xit.zubrein.eexam.R;
import xit.zubrein.eexam.quizsection.model.QuestionsModel;
import xit.zubrein.eexam.utils.CustomToast;
import xit.zubrein.eexam.utils.LoadingBar;
import xit.zubrein.eexam.utils.LoadingBarCharge;
import xit.zubrein.eexam.utils.LoadingBarForFreeExam;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<CategoryModel.subjects> subjectList;
    Context c;
    String user_id = "", token = "",otb_message="",subscribe_message="";
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
        otb_message = sharedPreferences.getString("otb_message", "");
        subscribe_message = sharedPreferences.getString("subscribe_message", "");
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
                checkAvailabity(currentitem.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public void checkAvailabity(final String subject) {
        loadingBar.showDialog(activity);
        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelAvailability> call = service.check_availability(token, user_id);
        call.enqueue(new Callback<ModelAvailability>() {
            @Override
            public void onResponse(Call<ModelAvailability> call, Response<ModelAvailability> response) {
                if (response.isSuccessful()) {
                    loadingBar.cancelDialog();
                    ModelAvailability modelAvailability = response.body();
                    if (modelAvailability.getAvailability().equals("true")) {
                        final LoadingBarForFreeExam loadingBarForFreeExam = new LoadingBarForFreeExam();
                        loadingBarForFreeExam.showDialog(activity);
                        Handler handler = new Handler() ;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingBarForFreeExam.cancelDialog();
                                Intent intent = new Intent(c, QuizActivity.class);
                                intent.putExtra("subject_id", subject);
                                intent.putExtra("quiz_type", "regular");
                                c.startActivity(intent);
                            }
                        }, 3000);
                    } else if (modelAvailability.getAvailability().equals("false")) {
                        if (modelAvailability.getMsisdn().equals("false")) {
                            DialogNoMsisdn dialogNoMsisdn = new DialogNoMsisdn();
                            dialogNoMsisdn.showDialog(activity, subject);
                        } else if (modelAvailability.getMsisdn().equals("true")) {
                            if (modelAvailability.getSub_status_paid().equals("true")) {
                                DialogPaid dialogPaid = new DialogPaid();
                                dialogPaid.showDialog(activity, subject, "subscribed");
                            } else if (modelAvailability.getSub_status_paid().equals("false")) {
                                DialogPaid dialogPaid = new DialogPaid();
                                dialogPaid.showDialog(activity, subject, "not_subscribed");
                            }else if (modelAvailability.getSub_status_paid().equals("pending_charge")) {
                                CustomToast.showCustomToast(activity,"You have previous dues. Please recharge first.");
                            }


                        }

                    }


                } else {
                    loadingBar.cancelDialog();
                    Toast.makeText(c, "no response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ModelAvailability> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(c, "No Internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class DialogPaid {
        public void showDialog(Activity activity, final String subject, String type) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.subscription_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView subscribe = dialog.findViewById(R.id.subscribe);
            TextView auto_renewel_text = dialog.findViewById(R.id.auto_renewel_text);
            TextView otb = dialog.findViewById(R.id.otb);
            TextView cancel = dialog.findViewById(R.id.cancel);
            TextView header = dialog.findViewById(R.id.dialog_message_header);
            header.setText("");
            TextView message = dialog.findViewById(R.id.dialog_message);
            if (type.equals("subscribed")) {
                message.setText(otb_message);
                subscribe.setVisibility(View.INVISIBLE);
                auto_renewel_text.setVisibility(View.INVISIBLE);

            } else if (type.equals("not_subscribed")) {
                message.setText(subscribe_message);
            }

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    do_subscription(subject);
                }
            });

            otb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                   do_charge(subject);
                }
            });

            dialog.show();
        }
    }

    public class DialogNoMsisdn {
        public void showDialog(Activity activity, final String subject) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.subscription_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView header = dialog.findViewById(R.id.dialog_message_header);
            header.setText("");
            TextView message = dialog.findViewById(R.id.dialog_message);
            message.setText("Your have reached your free limit . Please add your ROBI/AIRTEL number for subscribe or buy 5 exams by taping OTB(One Time Buy)");
            TextView subscribe = dialog.findViewById(R.id.subscribe);
            TextView otb = dialog.findViewById(R.id.otb);
            TextView cancel = dialog.findViewById(R.id.cancel);
            otb.setText("OTB");
            subscribe.setText("Add Number");

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            subscribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c.startActivity(new Intent(c, ProfileActivity.class));
                }
            });
            otb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(c, PaymentActivity.class);
                    intent.putExtra("subject_id", subject);
                    intent.putExtra("quiz_type", "regular");
                    c.startActivity(intent);
                }
            });

            dialog.show();
        }
    }

    public void do_charge(final String subject) {

        final LoadingBarCharge loadingBar = new LoadingBarCharge();
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
                        CustomToast.showCustomToast(activity, "Charging failed.Please recharge or try again later");
                    }

                } else {
                    loadingBar.cancelDialog();
                    Toast.makeText(c, "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ModelCharging> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(c, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void do_subscription(final String subject) {

        final LoadingBarCharge loadingBar = new LoadingBarCharge();
        loadingBar.showDialog(activity);

        ApiClient apiClient = new ApiClient();
        ApiInterface service = apiClient.createService(ApiInterface.class);
        Call<ModelCharging> call = service.do_subscription(token, user_id);
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
                        CustomToast.showCustomToast(activity, "Subscription failed");
                    }

                } else {
                    loadingBar.cancelDialog();
                    Toast.makeText(c, "Server error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ModelCharging> call, Throwable t) {
                loadingBar.cancelDialog();
                Toast.makeText(c, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
