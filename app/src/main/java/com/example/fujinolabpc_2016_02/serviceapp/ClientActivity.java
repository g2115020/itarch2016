package com.example.fujinolabpc_2016_02.serviceapp;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by fujinolabPC_2016_02 on 2017/02/20.
 */

public class ClientActivity extends Activity {
    private static final String TAG = "ClientActivity";
    private EditText mEditTextLhs;
    private EditText mEditTextRhs;
    private Spinner mSpinnerOp;
    private TextView mTextViewResult;
    private Button mButtonSend;
    private Handler mHandler;
    private ICalculateService mService;
    private ICalculateCallback mCallback = new ICalculateCallback.Stub() { // Serviceではサービスの実体をStubの継承で作成したが、Activityではコールバックの実体を、コールバックのStubを継承して実装
        @Override
        public void resultSum(final int value) throws RemoteException {
            Log.d(TAG, "resultSum:" + value);
            mHandler.post(new Runnable() {
                public void run() {
                    mTextViewResult.setText("" + value);
                }
            });
        }
    };
    private ServiceConnection mServiceConnection = new ServiceConnection() { //AIDLを使用するサービスとのプロセス間通信ではServiceConnectionを実装する
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mButtonSend.setEnabled(false);
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) { // この中で*のように自動生成されたStubクラスの静的メソッドでサービスを取得
            mService = ICalculateService.Stub.asInterface(service); // *
            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mButtonSend.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditTextLhs = (EditText) findViewById(R.id.editTextLhs);
        mEditTextRhs = (EditText) findViewById(R.id.editTextRhs);
        mSpinnerOp = (Spinner) findViewById(R.id.spinnerOp);
        mTextViewResult = (TextView) findViewById(R.id.textViewResult);
        mButtonSend = (Button) findViewById(R.id.buttonSend);
        mButtonSend.setEnabled(false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.add("+");
        adapter.add("-");
        adapter.add("*");
        adapter.add("/");
        adapter.add("sum");
        adapter.add("rotate");
        mSpinnerOp.setAdapter(adapter);
        mSpinnerOp.setSelection(5);
        mSpinnerOp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4) {
                    mEditTextRhs.setVisibility(View.INVISIBLE);
                } else {
                    mEditTextRhs.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mEditTextRhs.setVisibility(View.VISIBLE);
            }
        });
        mHandler = new Handler();

        // bindService(new Intent(ICalculateService.class.getName()), mServiceConnection, BIND_AUTO_CREATE);
        // ServiceConnectionでサービスに接続するには、bindService(...)にインスタンスを渡す。指定するアクションはサービスごとに決められたもの

        // Android5.0以降は以下の方法でバインドしないとService Intent must be explicitで動かない
        Intent intent = new Intent(ICalculateService.class.getName());
        intent.setPackage("com.example.fujinolabpc_2016_02.serviceapp");
        this.bindService(intent, mServiceConnection, BIND_AUTO_CREATE); // bindService()はクライアントの指示を明確に受け取る場合に有効、startService()でもサービスに接続できるが各々特徴あり
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConnection); // サービスとの切断を行うには、unbindService(...)にServiceConnectionのインスタンスを渡す,サービス側ではonUnbind()が呼び出される
        }
    }

    public void onClick(View view) throws NumberFormatException, RemoteException {
        String lhs = mEditTextLhs.getText().toString();
        String rhs = mEditTextRhs.getText().toString();
        String op = mSpinnerOp.getSelectedItem().toString();
        String s = "";
        if (op.equals("+")) {
            lhs = new StringTokenizer(lhs, " ").nextToken();
            rhs = new StringTokenizer(rhs, " ").nextToken();
            s += mService.add(Integer.valueOf(lhs), Integer.valueOf(rhs));
        } else if (op.equals("-") || op.equals("*") || op.equals("/")) {
            lhs = new StringTokenizer(lhs, " ").nextToken();
            rhs = new StringTokenizer(rhs, " ").nextToken();
            CalculateExpression exp = new CalculateExpression();
            exp.mOp = new CalculateElement();
            exp.mLhs = new CalculateElement();
            exp.mRhs = new CalculateElement();

            exp.mOp.mKind = CalculateElement.OPERATOR;
            exp.mOp.mValue = op.charAt(0);
            exp.mLhs.mValue = Integer.valueOf(lhs);
            exp.mRhs.mValue = Integer.valueOf(rhs);
            s += mService.eval(exp);
        } else if (op.equals("sum") || op.equals("rotate")) {
            StringTokenizer st  = new StringTokenizer(lhs, " ");
            List<Integer> list = new ArrayList<Integer>();
            while (st.hasMoreTokens()) {
                list.add(Integer.valueOf(st.nextToken()));
            }
            if (op.equals("sum")) {
                mService.sum(list);
                return;
            } else {
                int[] values = new int[list.size()];
                for (int i = 0; i < values.length; i++) {
                    values[i] = (Integer)list.get(i);
                    s += values[i];
                    if (i != values.length - 1) {
                        s += " ";
                    } else {
                        s += "\n";
                    }
                }
                mService.rotate(values, Integer.valueOf(rhs));
                for (int i = 0; i < values.length; i++) {
                    s += values[i];
                    if (i != values.length - 1) {
                        s += " ";
                    }
                }
            }
        }
        final String result = s;
        mHandler.post(new Runnable() {
            public void run() {
                mTextViewResult.setText(result);
            }
        });
    }
}
