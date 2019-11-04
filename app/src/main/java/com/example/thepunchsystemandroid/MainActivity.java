package com.example.thepunchsystemandroid;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.thepunchsystemandroid.Fragment.PersonFragment;
import com.example.thepunchsystemandroid.Fragment.RankFragment;
import com.example.thepunchsystemandroid.Fragment.SettingFragment;
import com.example.thepunchsystemandroid.R;

public class MainActivity extends BaseActivity  implements View.OnClickListener {

    private PersonFragment personFragment;
    private RankFragment rankFragment;
    private SettingFragment settingFragment;
    private View personLayout;
    private View rankLayout;
    private View settingLayout;
    private ImageView personImage;
    private ImageView rankImage;
    private ImageView settingImage;

    private FragmentManager fragmentManager;

    private long exitTime = 0;



    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityList.Add(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initViews();
        fragmentManager=getSupportFragmentManager();
        setTabSelection(0);
    }

    private void initViews(){
        personLayout=findViewById(R.id.person_layout);
        rankLayout=findViewById(R.id.rank_layout);
        settingLayout=findViewById(R.id.setting_layout);

        personImage=findViewById(R.id.person_image);
        rankImage=findViewById(R.id.rank_image);
        settingImage=findViewById(R.id.setting_image);

        personLayout.setOnClickListener(this);
        rankLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.person_layout:
                // 当点击了个人tab时，选中第1个tab
                setTabSelection(1);
                break;
            case R.id.rank_layout:
                setTabSelection(0);
                break;
            case R.id.setting_layout:
                setTabSelection(2);
                break;
            default:
                break;
        }
    }
    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     *            每个tab页对应的下标。0表示个人，1表示排行，2表示设置。
     */

    private void setTabSelection(int index){
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index){
            case 1:
                personImage.setImageResource(R.drawable.select_person);
                if(personFragment==null){
                    personFragment=new PersonFragment();
                    transaction.add(R.id.content,personFragment);
                }else {
                    transaction.show(personFragment);
                }
                break;
            case 0:
                rankImage.setImageResource(R.drawable.rank_select);
                if(rankFragment==null){
                    rankFragment=new RankFragment();
                    transaction.add(R.id.content,rankFragment);
                }else {
                    transaction.show(rankFragment);
                }
                break;
            case 2:
                settingImage.setImageResource(R.drawable.settings_select);
                if(settingFragment==null){
                    settingFragment=new SettingFragment();
                    transaction.add(R.id.content,settingFragment);
                }else {
                    transaction.show(settingFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }
    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        personImage.setImageResource(R.drawable.person);
        rankImage.setImageResource(R.drawable.rank);
        settingImage.setImageResource(R.drawable.settings);
    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction
     *            用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if(personFragment!=null){
            transaction.hide(personFragment);
        }
        if(rankFragment!=null){
            transaction.hide(rankFragment);
        }
        if(settingFragment!=null){
            transaction.hide(settingFragment);
        }
    }

}
