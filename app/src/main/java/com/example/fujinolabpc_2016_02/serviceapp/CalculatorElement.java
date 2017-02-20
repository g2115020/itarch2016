package com.example.fujinolabpc_2016_02.serviceapp;

import java.io.Serializable;

/**
 * Created by fujinolabPC_2016_02 on 2017/02/20.
 */

public class CalculatorElement implements Serializable { //Serializableクラスを実装
    private static final long serialVersionUID = 5415786969637871274L;
    public static final int NUMBER = 0;
    public static final int OPERATOR = 1;
    public int mKind;
    public int mValue;

    @Override
    public String toString() {
        String kind;
        String value;
        if (mKind == NUMBER) {
            kind = "NUMBER";
            value = "" + (char) mValue;
        } else {
            kind = "OPERATOR";
            value = "" + mValue;
        }
        return "mKind=[" + kind + "] mValue=[" + value + "]";
    }
}
