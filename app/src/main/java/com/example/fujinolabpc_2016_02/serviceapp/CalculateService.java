package com.example.fujinolabpc_2016_02.serviceapp;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;

public class CalculateService extends Service {
    private static final String TAG = "CalculateService";
    private ICalculateService.Stub mStub = new ICalculateService.Stub() { //Stubを実装
        private RemoteCallbackList<ICalculateCallback> mCallbacks = new RemoteCallbackList<ICalculateCallback>();

        @Override
        public void registerCallback(ICalculateCallback callback) throws RemoteException {
            Log.d(TAG, "registerCallback");
            mCallbacks.register(callback);
        }

        @Override
        public void unregisterCallback(ICalculateCallback callback) throws RemoteException {
            Log.d(TAG, "unregisterCallback");
            mCallbacks.unregister(callback);
        }

        // AIDLファイルで定義した各処理を実装
        @Override
        public int add(int lhs, int rhs) throws RemoteException {
            Log.d(TAG, "add (" + lhs + " + " + rhs + " = " + (lhs + rhs) + ")");
            return lhs + rhs;
        }

        @Override
        public void sum(@SuppressWarnings("rawtypes") List values) throws RemoteException {
            Log.d(TAG, "sum");
            int total = 0;
            for (Object obj : values) {
                int value = (Integer) obj;
                total += value;
            }
            Log.d(TAG, "total=" + total);
            int n = mCallbacks.beginBroadcast();
            for (int i = 0; i < n; i++) {
                try {
                    mCallbacks.getBroadcastItem(i).resultSum(total);
                    Log.d(TAG, "called callback");
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.getMessage(), e);
                }
            }
            mCallbacks.finishBroadcast();
        }

        @Override
        public void rotate(int[] array, int num) throws RemoteException {
            Log.d(TAG, "rotate");
            int[] copy = new int[array.length];
            System.arraycopy(array, 0, copy, 0, array.length);
            int n = num % array.length;
            if (n < 0) {
                n = array.length + n;
            }
            for (int i = 0; i < array.length; i++) {
                int to = (i + n) % array.length;
                array[to] = copy[i];
                Log.d(TAG, "array[" + to + "] = copy[" + i + "] (" + array[to]
                        + " " + copy[i] + ")");
            }
        }

        @Override
        public int eval(CalculateExpression exp) throws RemoteException {
            Log.d(TAG, "eval:" + (char) exp.mOp.mValue);
            int result;
            switch (exp.mOp.mValue) {
                case '+':
                    result = exp.mLhs.mValue + exp.mRhs.mValue;
                    break;
                case '-':
                    result = exp.mLhs.mValue - exp.mRhs.mValue;
                    break;
                case '*':
                    result = exp.mLhs.mValue * exp.mRhs.mValue;
                    break;
                case '/':
                    result = exp.mLhs.mValue / exp.mRhs.mValue;
                    break;
                default:
                    result = 0;
                    break;
            }
            return result;
        }
    };

    @Override
    public IBinder onBind(Intent intent) { // Stubの実装をonBind(Intent)の戻り値として返す
        Log.d(TAG, "onBind");
        return mStub;
    }

    @Override
    public boolean onUnbind(Intent intent) { // サービスから切断するとき呼び出す
        boolean result = super.onUnbind(intent);
        Log.d(TAG, "onUnbind:" + result);
        return result;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }
}
