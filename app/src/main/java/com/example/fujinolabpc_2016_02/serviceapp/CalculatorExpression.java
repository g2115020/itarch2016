package com.example.fujinolabpc_2016_02.serviceapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fujinolabPC_2016_02 on 2017/02/20.
 */

public class CalculatorExpression implements Parcelable {
    public CalculatorElement mOp;
    public CalculatorElement mLhs;
    public CalculatorElement mRhs;

    public static final Parcelable.Creator<CalculatorExpression> CREATOR = new Parcelable.Creator<CalculatorExpression>() {
        @Override
        public CalculatorExpression createFromParcel(Parcel in) {
            return new CalculatorExpression(in);
        }

        @Override
        public CalculatorExpression[] newArray(int size) {
            return new CalculatorExpression[size];
        }
    };

    public CalculatorExpression() {
    }

    private CalculatorExpression(Parcel in) {
        mOp = (CalculatorElement) in.readSerializable();
        mLhs = (CalculatorElement) in.readSerializable();
        mRhs = (CalculatorElement) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeSerializable(mOp);
        out.writeSerializable(mLhs);
        out.writeSerializable(mRhs);
    }

    @Override
    public String toString() {
        return "" + mLhs.mValue + "" + (char) mOp.mValue + "" + mRhs.mValue;
    }
}
