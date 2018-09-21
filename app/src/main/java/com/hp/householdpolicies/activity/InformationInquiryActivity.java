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
import android.widget.Toast;

import com.hp.householdpolicies.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.rsc.impl.RscServiceConnectionImpl;
import com.rsc.reemanclient.ConnectServer;
import com.synjones.idcard.IDCardInfo;
import com.synjones.idcard.OnIDListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/6/14 0014.
 */

public class InformationInquiryActivity extends BaseActivity implements Validator.ValidationListener{
    //    身份证号
    @BindView(R.id.edtIDNumber)
    @NotEmpty(messageResId = R.string.errorMessage)
    EditText edtIDNumber;
    //    手机号
    @BindView(R.id.textPhone)
    @NotEmpty(messageResId = R.string.errorMessage)
    EditText textPhone;
    //    查询按钮
    @BindView(R.id.btnSearch)
    Button btnSearch;
    private ConnectServer cs;
    Validator validator;
    Boolean verify=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_information_inquiry);
        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }
    @OnClick({R.id.btnSearch})
    void ViewClick(View view) {
        validator.validate();
        if(verify){
            switch (view.getId()) {
                case R.id.btnSearch:
                    Intent intent = new Intent(this, InformationProgressActivity.class);
                    intent.putExtra("idcard",edtIDNumber.getText().toString());
                    intent.putExtra("phone",textPhone.getText().toString());
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cs = ConnectServer.getInstance(getApplication(), impl);
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
        public boolean  handleMessage(android.os.Message msg) {
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
    @Override
    public void onValidationSucceeded() {
        verify=true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            }
        }
    }
}
