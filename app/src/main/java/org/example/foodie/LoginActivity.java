package org.example.foodie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.example.foodie.models.RestaurantLogIn.ResponseRestaurant;
import org.example.foodie.models.RestaurantLogIn.ResponseRestaurantUser;
import org.example.foodie.models.ResponseUser;
import org.example.foodie.models.RestaurantLogIn.RestaurantUser;
import org.example.foodie.models.User;
import org.example.foodie.org.example.foodie.apifetch.FoodieClient;
import org.example.foodie.org.example.foodie.apifetch.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static User user = new User("", "");
    private Button LoginButton;
    private EditText InputPhone, InputPassword,InputRestaurantId;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        initWidgets();
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   restaurantLogin();
            }
        });
        if (user.getToken() != null) {
            Log.i("ok", user.getToken());
            WelcomeActvity.getInstance().finish();
        }

    }

    private void restaurantLogin() {
        FoodieClient foodieClient = ServiceGenerator.createService(FoodieClient.class);
        RestaurantUser restaurantUser=new RestaurantUser(InputRestaurantId.getText().toString(),InputPassword.getText().toString());
        Call<ResponseRestaurantUser> responseRestaurantUserCall= foodieClient.logInRestaurant(restaurantUser);
        spinner.setVisibility(View.VISIBLE);
        responseRestaurantUserCall.enqueue(new Callback<ResponseRestaurantUser>() {
            @Override
            public void onResponse(Call<ResponseRestaurantUser> call , Response<ResponseRestaurantUser> response) {
                if (response.code()==200) {
                    Toast.makeText(getApplicationContext() , "Success!" , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intent);
//
//                    ResponseRestaurant restaurantObj=response.body().getRestaurant();
//
//                    intent.putExtra("token", response.body().getToken());
//                    intent.putExtra("name",restaurantObj.getName());
//                    intent.putExtra("restId",restaurantObj.getRest_id());
//                    intent.putExtra("address",restaurantObj.getAddress());
//                    startActivity(intent);
                    spinner.setVisibility(View.GONE);
                    WelcomeActvity.getInstance().finish();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseRestaurantUser> call , Throwable t) {

                spinner.setVisibility(View.GONE);
                Log.d("errorMessage",t.getMessage());
            }
        });

    }

    //function to initialise all the widgets
    public void initWidgets() {
        LoginButton = (Button) findViewById(R.id.login_btn);
        InputPassword = (EditText) findViewById(R.id.login_password_input);
        InputRestaurantId = (EditText) findViewById(R.id.login_restaurant_id_input);

    }

}
