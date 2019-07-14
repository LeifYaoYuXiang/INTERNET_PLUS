package com.example.demo0426;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String idInformation;
    public static String imageInformation;

    private GroupBuildings[] groupBuildings={
            new GroupBuildings("北工大运动场馆", R.drawable.field),
            new GroupBuildings("第三教学楼",R.drawable.tb3),
            new GroupBuildings("北工大第四教学楼",R.drawable.bdic),
            new GroupBuildings("北工大旧图",R.drawable.bjut_library),
            new GroupBuildings("工大建国饭店",R.drawable.gongdajianguo),
            new GroupBuildings("北工大礼堂",R.drawable.hall),
            new GroupBuildings("北工大科学楼",R.drawable.science_building),
            new GroupBuildings("北工大交通楼",R.drawable.transport_building)
                                                };


    private List<GroupBuildings> groupBuildingsList=new ArrayList<>();
    private GroupBuildingsAdapter groupBuildingsAdapter;
    public static final int TAKE_PHOTO=1;
    private Uri imageUri;
    public String responseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGroupsBuildings();
        final RecyclerView mainRecylcle=findViewById(R.id.main_recycle);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        mainRecylcle.setLayoutManager(gridLayoutManager);
        groupBuildingsAdapter=new GroupBuildingsAdapter(groupBuildingsList);
        mainRecylcle.setAdapter(groupBuildingsAdapter);

        groupBuildingsAdapter.setItemClickListener(new onRecycleViewClickListener() {
            @Override
            public void onItemClickListener(View view) {
                int position=mainRecylcle.getChildAdapterPosition(view);
                Toast.makeText(MainActivity.this, "测试成功！"+position, Toast.LENGTH_SHORT).show();
                if(position==0){
                    Intent intent=new Intent(MainActivity.this,SportsFieldActivity.class);
                    startActivity(intent);
                }
                if(position==6){
                    Intent intent=new Intent(MainActivity.this,ScienceBuildingActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemLongClickListener(View view) {

            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.scenedetection.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,TAKE_PHOTO);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initGroupsBuildings() {
        groupBuildingsList.clear();
        for(int i=0;i<groupBuildings.length;i++){
            groupBuildingsList.add(groupBuildings[i]);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        switch (requestCode){
            case TAKE_PHOTO:
                if(resultCode==RESULT_OK){
                    //sendWithOKHttpArea(2,Uri.parse(imageUri.toString()));
                    sendWithOKHttpArea(2,Uri.parse(imageUri.toString()));
                }
                break;
            default:
                break;
        }
    }

    public void sendWithOKHttpImage(Uri uri){
        OkHttpClient client=new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout( 60 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(60 * 1000, TimeUnit.MILLISECONDS).build();


        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.ALTERNATIVE);
        File file = null;
        try {
            file = new File(new URI(uri.toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (file != null) {
            Log.i("TAG","File Not Null");
            RequestBody fileBody=RequestBody.create(MediaType.parse("image/jpg"), file);
            builder.addFormDataPart("CameraImages", file.getName(), fileBody);
        }
        Log.i("TAG","information of file: "+file.getName()+" "+file.length());
        MultipartBody multipartBody = builder.build();
        final Request request = new Request.Builder()
                .url("http://222.128.45.37:8500/uploadimage")
                //请求地址
                .post(multipartBody)
                .addHeader("Connection","close")
                //添加请求体参数
                .build();

        Call call = client.newCall(request);
        Callback imageCallback=new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("Failure-Image","上传失败"+e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                imageInformation=response.body().toString();
                Log.e("Success-Image","上传成功"+imageInformation);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(MainActivity.this,feedbackActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("imageUri",imageUri.toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        };
        call.enqueue(imageCallback);

    }

    public void sendWithOKHttpArea(int id, final Uri uri){
        OkHttpClient client=new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout( 60 * 1000, TimeUnit.MILLISECONDS)
                .writeTimeout(60 * 1000, TimeUnit.MILLISECONDS).build();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.ALTERNATIVE);
        builder.addFormDataPart("id",id+"");
        MultipartBody multipartBody = builder.build();
        final Request request = new Request.Builder()
                .url("http://222.128.45.37:8500/loadarea")
                //请求地址
                .post(multipartBody)
                .addHeader("Connection","close")
                //添加请求体参数
                .build();
        Call call = client.newCall(request);
        Callback areaCallback=new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e("TAG-Failure-Area","上传失败"+e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                idInformation=response.body().toString();
                Log.e("TAG-Success-Area","上传成功"+idInformation);
                sendWithOKHttpImage(uri);
            }
        };
        call.enqueue(areaCallback);
    }


}
