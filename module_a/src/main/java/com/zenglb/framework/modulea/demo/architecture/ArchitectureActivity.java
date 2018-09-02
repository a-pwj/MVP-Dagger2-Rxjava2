package com.zenglb.framework.modulea.demo.architecture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.zenglb.framework.modulea.R;
import com.zenglb.framework.modulea.http.AModuleApiService;
import com.zlb.http.ApiService;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * It is Just a Demo
 *
 */
public class ArchitectureActivity extends AppCompatActivity {
    @Inject
    AModuleApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_architecture);

        // Add product list fragment if this is first creation
        if (savedInstanceState == null) {
            //User ID:用户的标识符。最好使用Fragment的参数将此信息传递到Fragment中。如果Android操作系统回收了Fragment，则会保留此信息，以便下次重新启动应用时，该ID可用。
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, ArchitectureDemoFragment.newInstance("expired"), ArchitectureDemoFragment.TAG).commit();
        }

        test();
    }


    private void test(){
        String url = "http://localhost:8080/hello";
        Call<String> test = apiService.getUserProfile(url);
        //上面的实现是非常的精巧  http://www.jianshu.com/p/c1a3a881a144

        test.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s = response.body();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ewwew","dasdad");
            }
        });
    }


}
