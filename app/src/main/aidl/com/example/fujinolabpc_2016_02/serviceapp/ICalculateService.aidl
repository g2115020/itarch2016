// ICalculateService.aidl
package com.example.fujinolabpc_2016_02.serviceapp;

// Declare any non-default types here with import statements
import com.example.fujinolabpc_2016_02.serviceapp.ICalculateCallback;
import com.example.fujinolabpc_2016_02.serviceapp.CalculateExpression;

interface ICalculateService {

	oneway void registerCallback(ICalculateCallback callback); //コールバック登録


	oneway void unregisterCallback(ICalculateCallback callback); //コールバック解除


	int add(int lhs, int rhs); //加算　lhs:左辺 rhs:右辺

	void sum(in List values); //左辺合算

	void rotate(inout int[] array, int num); //arrayをnum回ローテーション　numがプラスで右、マイナスで左にローテーション

	int eval(in CalculateExpression exp); //四則演算　exp:計算式
}

