package com.hp.householdpolicies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hp.householdpolicies.R;
import com.rsc.impl.RscServiceConnectionImpl;
import com.rsc.reemanclient.ConnectServer;
import com.synjones.idcard.IDCardInfo;
import com.synjones.idcard.OnIDListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public class InputActivity extends BaseActivity {
    //    身份证号
    @BindView(R.id.edtIDNumber)
    EditText edtIDNumber;
    //    手机号
    @BindView(R.id.textPhone)
    EditText textPhone;
    @BindView(R.id.textName)
    EditText textName;
    //    查询按钮
    @BindView(R.id.btnSearch)
    Button btnSearch;
    private ConnectServer cs;

    @BindView(R.id.rg)
    RadioGroup rg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_input);
        ButterKnife.bind(this);
//        cs = ConnectServer.getInstance(getApplication(), impl);
    }
    @OnClick({R.id.btnSearch})
    void ViewClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                Intent intent = new Intent(this, AppointmentDetailsActivity.class);
                intent.putExtra("phone",textPhone.getText().toString());
                intent.putExtra("name",textName.getText().toString());
                intent.putExtra("idcard",edtIDNumber.getText().toString());
                int radioButtonId = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) InputActivity.this.findViewById(radioButtonId);
                intent.putExtra("type",rb.getText().toString());
                startActivity(intent);
                break;
        }
    }
    private RscServiceConnectionImpl impl = new RscServiceConnectionImpl() {
        public void onServiceConnected(int name) {
            if (cs == null)
                return;
            if (name == ConnectServer.Connect_Pr_Id) {
                cs.registerIDListener(Ilistener);
            }
        }

        public void onServiceDisconnected(int name) {
            System.out.println("onServiceDisconnected......");
        }
    };
    private OnIDListener Ilistener = new OnIDListener.Stub() {

        @Override
        public void onResult(IDCardInfo info, byte[] photo)
                throws RemoteException {
            Log.e("ID",
                    "name: " + info.getName() + ",nation: " + info.getNation()
                            + ",birthday: " + info.getBirthday() + ",sex: "
                            + info.getSex() + ",address: " + info.getAddress()
                            + ",append: " + info.getAppendAddress()
                            + ",fpname: " + info.getFpName() + ",grantdept: "
                            + info.getGrantdept() + ",idcardno: "
                            + info.getIdcardno() + ",lifebegin: "
                            + info.getUserlifebegin() + ",lifeend: "
                            + info.getUserlifeend());
            if (photo != null)
                Log.e("ID", "photo: " + photo.length);
            else
                Log.e("ID", "photo=null");
            Bundle bundle = new Bundle();
            bundle.putParcelable("info", info);
            bundle.putByteArray("photo",photo);
            Message msg = mHandler.obtainMessage();
            msg.setData(bundle);
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    };
    private Handler mHandler = new Handler(new Handler.Callback() {
        public boolean  handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle0 = msg.getData();
                    if (bundle0 == null)
                        return false;
                    IDCardInfo info = (IDCardInfo) bundle0.get("info");
                    byte[] photos = bundle0.getByteArray("photo");
                    //TODO
                    edtIDNumber.setText(info.getIdcardno());
                    break;
                default:
                    break;
            }
            return true;
        }
    });

}
