package com.rohfl.pagination.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rohfl.pagination.R;
import com.rohfl.pagination.adapters.ImageListAdapter;
import com.rohfl.pagination.listeners.OnClickListener;
import com.rohfl.pagination.models.ResponseGetImageListItem;
import com.rohfl.pagination.retrofit.ImageListInterface;
import com.rohfl.pagination.retrofit.RetrofitClient;
import com.rohfl.pagination.utils.SharedPreferenceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity implements OnClickListener {

    ImageView imgUser;
    RecyclerView rvImages;
    ImageListAdapter adapter;
    ProgressBar progressBar;

    // the album id and at max it will be 10
    int albumId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        updateStatusBarColorMain();

        // getting all the views
        imgUser = findViewById(R.id.img_user);
        rvImages = findViewById(R.id.rv_images);
        progressBar = findViewById(R.id.progress);

        // init the adapter
        adapter = new ImageListAdapter(DashboardActivity.this);
        rvImages.setLayoutManager(new LinearLayoutManager(this));
        rvImages.setAdapter(adapter);

        // setting the onLastItemScrolledListener in the adapter, i have passed the anonymous class
        adapter.setOnLastItemScrolledListener(() -> {
            // if we haven't reached the last page then again hit the api (last page is 10)
            if (albumId < 10) {
                if (checkNetworkConnection()) {
                    getImages();
                } else {
                    showToast("No Internet, Please check your network connection");
                }
            }
        });

        // setting the click listener
        adapter.setOnClickListener(this);

        attachListeners();

        // getting the list for the first time
        if (checkNetworkConnection()) {
            getImages();
        } else {
            showToast("No Internet, Please check your network connection");
        }
    }

    /**
     * attach the listeners to the views
     */
    private void attachListeners() {
        imgUser.setOnClickListener(v -> showLogoutDialog());
    }

    /**
     * get the list of images
     */
    private void getImages() {
        albumId++;
        if (albumId == 1) progressBar.setVisibility(View.VISIBLE);

        ImageListInterface api = RetrofitClient.getClient().create(ImageListInterface.class);
        Call<List<ResponseGetImageListItem>> call = api.getImageList(albumId);
        call.enqueue(new Callback<List<ResponseGetImageListItem>>() {
            @Override
            public void onResponse(Call<List<ResponseGetImageListItem>> call, Response<List<ResponseGetImageListItem>> response) {
                try {
                    if (response.code() == 200 && response.body() != null) {
                        if (albumId == 1) progressBar.setVisibility(View.GONE);
                        adapter.removeNull();
                        List<ResponseGetImageListItem> listItems = response.body();
                        // if albumId is less than 10 then we add null to show the progressbar
                        if (albumId < 10) {
                            listItems.add(null);
                        }
                        adapter.updateList(listItems);
                    }
                } catch (Exception e) {
                    if (albumId == 1) progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<ResponseGetImageListItem>> call, Throwable t) {
                if (albumId == 1) progressBar.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    private void showLogoutDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes",
                        (dialog, which) -> {
                            SharedPreferenceManager.deletePref();
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                )
                .setNegativeButton("No", null)
                .show();

        Typeface face = ResourcesCompat.getFont(this, R.font.poppins_500);

        TextView txtMessage = alertDialog.findViewById(android.R.id.message);
        Button dialogButton1 = alertDialog.getWindow().findViewById(android.R.id.button1);
        Button dialogButton2 = alertDialog.getWindow().findViewById(android.R.id.button2);
        txtMessage.setTypeface(face);
        dialogButton1.setTypeface(face);
        dialogButton2.setTypeface(face);
    }

    @Override
    public void onItemClicked(int position) {
        // building alert dialog to let user select if they want to delete the data
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this image?")
                .setPositiveButton("Yes",
                        (dialog, which) -> adapter.deleteItem(position)
                )
                .setNegativeButton("No", null).show();

        Typeface face = ResourcesCompat.getFont(this, R.font.poppins_500);

        TextView txtMessage = alertDialog.findViewById(android.R.id.message);
        Button dialogButton1 = alertDialog.getWindow().findViewById(android.R.id.button1);
        Button dialogButton2 = alertDialog.getWindow().findViewById(android.R.id.button2);
        txtMessage.setTypeface(face);
        dialogButton1.setTypeface(face);
        dialogButton2.setTypeface(face);
    }

    /**
     * method to check the internet connection of the user
     *
     * @return true if the internet is connected else false
     */
    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * method to show a toast message
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * updating the status bar color by this method because from xml it hides the data in the status bar
     */
    @SuppressLint("ObsoleteSdkInt")
    public void updateStatusBarColorMain() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setNavigationBarColor(Color.parseColor("#FFFFFF"));
                window.setStatusBarColor(Color.parseColor("#FFFFFF"));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int systemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                int flagLightStatusBar = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                systemUiVisibility |= flagLightStatusBar;
                getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
