package com.example.fujinolabpc_2016_02.serviceapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fujinolabPC_2016_02 on 2017/02/20.
 */

public class CalculateExpression implements Parcelable {
    public CalculateElement mOp;
    public CalculateElement mLhs;
    public CalculateElement mRhs;

    public static final Parcelable.Creator<CalculateExpression> CREATOR = new Parcelable.Creator<CalculateExpression>() {
        @Override
        public CalculateExpression createFromParcel(Parcel in) {
            return new CalculateExpression(in);
        }

        @Override
        public CalculateExpression[] newArray(int size) {
            return new CalculateExpression[size];
        }
    };

    public CalculateExpression() {
    }

    private CalculateExpression(Parcel in) { // Parcelから読み込み、データを初期化する
        mOp = (CalculateElement) in.readSerializable();
        mLhs = (CalculateElement) in.readSerializable();
        mRhs = (CalculateElement) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) { // Parcelにデータ書き込み
        out.writeSerializable(mOp);
        out.writeSerializable(mLhs);
        out.writeSerializable(mRhs);
    }

    @Override
    public String toString() {
        return "" + mLhs.mValue + "" + (char) mOp.mValue + "" + mRhs.mValue;
    }
}
