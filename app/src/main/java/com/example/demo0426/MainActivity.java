package com.example.demo0426;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationConfiguration;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Demo class
 *
 * @author YaoYuxiang
 * @date 2019/07/20
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static String idInformation;
    public static String imageInformation;

    public static String finalAreaInformation;
    public static String finalPossibility;
    public LocationClient locationClient;

    public double longitude;
    public double latitude;

    private MyLocationConfiguration.LocationMode locationMode;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationClient=new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());

        requestLocation();



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
                startCamera();
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

    private void requestLocation() {
        initLocation();
        locationClient.start();
    }

    private void startCamera(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 进入这儿表示没有权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // 提示已经禁止
                Toast.makeText(this, "不允许使用相机", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageUri = getImageUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent,TAKE_PHOTO);
        }
    }

    private void initLocation() {
        LocationClientOption locationOption = new LocationClientOption();

        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setCoorType("bd09ll");
        locationOption.setScanSpan(1000);
        locationOption.setIsNeedAddress(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setNeedDeviceDirect(false);
        locationOption.setLocationNotify(true);
        locationOption.setIgnoreKillProcess(true);
        locationOption.setIsNeedLocationDescribe(true);
        locationOption.setIsNeedLocationPoiList(true);
        locationOption.SetIgnoreCacheException(false);
        locationOption.setOpenGps(true);
        locationOption.setIsNeedAltitude(false);
        locationOption.setOpenAutoNotifyMode();
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);

        locationClient.setLocOption(locationOption);
    }

    public void navigateTo(BDLocation location){
        this.latitude=location.getLatitude();
        this.longitude=location.getLongitude();
    }

    private void initGroupsBuildings() {
        groupBuildingsList.clear();
        for(int i=0;i<groupBuildings.length;i++){
            groupBuildingsList.add(groupBuildings[i]);
        }
    }

    private Uri getImageUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        String path = file.getPath();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            imageUri = Uri.fromFile(file);
        } else {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, path);
            imageUri = this.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        }
        return imageUri;
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
                    Log.e("POSITION",this.latitude+"----"+this.longitude);
                    int id=this.determineID();
                    Log.e("POSI_NO",id+"");
                    sendWithOKHttpArea(12,Uri.parse(imageUri.toString()));
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
        final Callback imageCallback=new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                final IOException error=e;
                Log.e("Failure-Image","上传失败"+e.getLocalizedMessage());
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(MainActivity.this,ErrorActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putInt("ErrorCode",002);
                        bundle.putString("ErrorType","图像传输错误");
                        bundle.putString("ErrorMessage",error.getLocalizedMessage());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                imageInformation=response.body().string();
                Log.e("Success-Image","上传成功"+imageInformation);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Scanner sc=new Scanner(imageInformation);
                        sc.useDelimiter("\\s*%\\s*");

                        if(sc.hasNext()){
                            finalPossibility=sc.next();
                        }
                        if(sc.hasNext()){
                            finalAreaInformation=sc.next();
                        }

                        Intent intent=new Intent(MainActivity.this,feedbackActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("imageUri",imageUri.toString());



                        //Log.e("POSS",finalPossibility);
                        //Log.e("AREA",finalAreaInformation);

                        bundle.putString("Possibility",finalPossibility);
                        bundle.putString("AreaInformation",finalAreaInformation);

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
                final IOException error=e;
                Intent intent=new Intent(MainActivity.this,ErrorActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("ErrorCode",001);
                bundle.putString("ErrorType","地理信息传输错误");
                bundle.putString("ErrorMessage",error.getLocalizedMessage());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                idInformation=response.body().string();
                Log.e("TAG-Success-Area","上传成功"+idInformation);
                sendWithOKHttpImage(uri);
            }
        };
        call.enqueue(areaCallback);
    }

    public int determineID(){
        //通过此方法决定自己所处地区的ID
        //TODO：GPS信息不正确，需要后期调整
        if(this.latitude>39.871575&&this.latitude<39.873673&&this.longitude>116.477707&&this.longitude<116.482321){
            return 1;
        }else if(this.latitude>39.872546&&this.latitude<39.876229&&this.longitude>116.477722&&this.longitude<116.480495){
            return 2;
        }else if(this.latitude>39.873642&&this.latitude<39.876195&&this.longitude>116.480204&&this.longitude<116.482288){
            return 3;
        }else if(this.latitude>39.876205&&this.latitude<39.878478&&this.longitude>116.47799&&this.longitude<116.479727){
            return 4;
        }else if(this.latitude>39.877005&&this.latitude<39.878455&&this.longitude>116.479714&&this.longitude<116.481538){
            return 5;
        }else if(this.latitude>39.875172&&this.latitude<39.87705&&this.longitude>116.479697&&this.longitude<116.482305){
            return 6;
        }else if(this.latitude>39.873619&&this.latitude<39.876177&&this.longitude>116.480227&&this.longitude<116.485605){
            return 7;
        }else if(this.latitude>39.871567&&this.latitude<39.874225&&this.longitude>116.482283&&this.longitude<116.487073){
            return 8;
        }else if(this.latitude>39.873191&&this.latitude<39.876175&&this.longitude>116.483612&&this.longitude<116.487319){
            return 9;
        }else if(this.latitude>39.875295&&this.latitude<39.876721&&this.longitude>116.483594&&this.longitude<116.485569){
            return 10;
        }else if(this.latitude>39.876317&&this.latitude<39.87799&&this.longitude>116.482036&&this.longitude<116.485855){
            return 11;
        }else if(this.latitude>39.878422&&this.latitude<39.879425&&this.longitude>116.477411&&this.longitude<116.483654){
            return 12;
        }else if(this.latitude>39.876334&&this.latitude<39.878012&&this.longitude>116.481195&&this.longitude<116.483584){
            return 13;
        }
        return 0;
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            double latitude = location.getLatitude();

            double longitude = location.getLongitude();

            float radius = location.getRadius();

            String coorType = location.getCoorType();

            int errorCode = location.getLocType();

            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType()==BDLocation.TypeNetWorkLocation){
                navigateTo(location);
            }
        }
    }


}
