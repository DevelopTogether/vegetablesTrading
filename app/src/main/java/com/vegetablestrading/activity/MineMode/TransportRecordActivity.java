package com.vegetablestrading.activity.MineMode;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vegetablestrading.R;
import com.vegetablestrading.adapter.DividerItemDecoration;
import com.vegetablestrading.adapter.TransportRecordAdapter;
import com.vegetablestrading.bean.LogisticsInfo;
import com.vegetablestrading.bean.TransportRecord;
import com.vegetablestrading.utils.PublicUtils;

import java.util.ArrayList;

import static com.vegetablestrading.R.id.top_left_image_iv;

/**
 * 配送记录
 */
public class TransportRecordActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mTopLeftImageIv;
    /**
     * 标题
     */
    private TextView mTopTitleTv;
    private ImageView mTopRightImageIv;
    private RecyclerView mTransportRecordRv;
    private TransportRecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
        initView();
        initActionBar();
    }

    /**
     * 初始化actionBar
     */
    private void initActionBar() {
        mTopTitleTv.setText("配送记录");
    }

    private void initView() {
        mTopLeftImageIv = (ImageView) findViewById(top_left_image_iv);
        mTopLeftImageIv.setOnClickListener(this);
        mTopTitleTv = (TextView) findViewById(R.id.top_title_tv);
        mTopRightImageIv = (ImageView) findViewById(R.id.top_right_image_iv);
        mTransportRecordRv = (RecyclerView) findViewById(R.id.transport_record_rv);
        mTransportRecordRv.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.HORIZONTAL,R.drawable.horizontal_line_grey));
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mTransportRecordRv.setLayoutManager(manager);
        adapter = new TransportRecordAdapter();
        mTransportRecordRv.setAdapter(adapter);
        adapter.setData(getTransportRecordData());
        adapter.setTransportRecordItemClick(new TransportRecordAdapter.TransportRecordItemClick() {
            @Override
            public void itemClick(TransportRecord transportRecord) {//item点击事件
                PublicUtils.transportRecord = transportRecord;
                Toast.makeText(getApplicationContext(), transportRecord.getResidualIntegral(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(TransportRecordActivity.this,TransportInfoActivity.class));
            }

        });
    }

    /**
     * 测试数据
     * @return
     */
    private ArrayList<TransportRecord> getTransportRecordData(){
        ArrayList<TransportRecord> arrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TransportRecord bean = new TransportRecord();
            bean.setLogisticsNo("物流单号：110121211000200"+i);
            bean.setLogisticsName("顺丰快递");
            bean.setLogisticsInfos(getLogisticsInfos());
            bean.setTransportPeople("配送人：王彬");
            bean.setTransportPeopleMobile("配送人电话：15311810032");
            bean.setTransportTime("配送时间：2017-11-20 14:21:30");
            bean.setTransportInfo("配送备注：多送点香菜");
            bean.setPetName("收件人：王司令");
            bean.setMobile("收件人电话：18888888888");
            bean.setAddress("收件人地址：北京市海淀区增光路30号");
            bean.setResidualIntegral(i+"");
            bean.setRelayBoxNo("中转箱：168712357164563");
            bean.setOperatingPeople("操作员：文员1");
            bean.setOperateTime("操作时间：2017-11-20 14:22:46");
            bean.setNoteInfo("操作备注：.dlkfj");
            arrayList.add(bean);
        }

        return arrayList;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_left_image_iv:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取配送列表中的蔬菜信息
     *
     * @return
     */
    public ArrayList<LogisticsInfo> getLogisticsInfos() {
        ArrayList<LogisticsInfo> arrays = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            LogisticsInfo bean = new LogisticsInfo();
            bean.setTime("2017年11月23日 上午9:06:23");
            bean.setDescription("苏州市|签收|苏州市【运东一分布】，宝尊仓库电商退货组，先签收，后验货！（13号门） 已签收");
            arrays.add(bean);
        }
        return arrays;
    }
}