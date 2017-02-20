// ICalculatorService.aidl
package com.example.fujinolabpc_2016_02.serviceapp;

// Declare any non-default types here with import statements
import com.example.fujinolabpc_2016_02.serviceapp.ICalculatorCallback;
import com.example.fujinolabpc_2016_02.serviceapp.CalculatorExpression;

interface ICalculatorService {
	/**
	 * コールバック登録。
	 * @param callback 登録するコールバック。
	 */
	oneway void registerCallback(ICalculatorCallback callback);

	/**
	 * コールバック解除。
	 * @param callback 解除するコールバック。
	 */
	oneway void unregisterCallback(ICalculatorCallback callback);

	/**
	 * 同期の加算。
	 * @param lhs 加算対象
	 * @param rhs 加算対象
	 * @return 加算結果
	 */
	int add(int lhs, int rhs);

	/**
	 * 非同期の合算。
	 * 合算結果はコールバックで通知される。
	 * @see ICalculator#resultSum(int)
	 */
	void sum(in List values);

	/**
	 * 同期のローテーション。
	 * ローテーション結果は引数に反映される。
	 * @param array ローテーション対象。
	 * @param num ローテーション数。プラスで右、マイナスで左にローテーション
	 */
	void rotate(inout int[] array, int num);

	/**
	 * 同期の計算。任意の四則演算が行える。
	 * @param exp 計算式
	 * @return 計算結果
	 */
	int eval(in CalculatorExpression exp);
}

