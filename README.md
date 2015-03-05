ECHO Android SDK
======================
ECHO Android SDKは、[クラウド型Webプラットフォーム「ECHO」](http://echopf.com/)が提供するREST APIのAndroid向けラッパーライブラリです。

「データの操作」や「会員管理・ログイン」といったECHOが提供する多彩なサーバーサイド機能を、ネイティブなAndroidアプリから簡単に呼び出すことができます。

# 初期セットアップ
以下の手順で、SDKの初期セットアップを行ってください。

## 0. ECHOサイトアカウントを開設
本SDKを利用してアプリケーション開発を行うには、サイトアカウントの開設が必要となります。
開設方法は、以下ドキュメントをご覧ください。

[ECHO基本ガイド:スタートアップ](http://echopf.com/docs/guide/startup)

## 1. APIアプリケーション登録
ECHOアカウントの開設がお済の方は、管理画面よりAPIアプリケーション登録を行ってください。

APIアプリケーション登録の方法は、以下ドキュメントをご参照ください。

[REST APIガイド:APIアプリケーション登録](http://echopf.com/docs/restapi/regist)

アプリケーションの登録が完了すると、**アプリケーションID**と**アプリケーションキー**が発行されます。
この情報は、後ほど初期化の際に使用します。


## 2. パーミッションの設定
SDKの利用にあたり必要なパーミッションの設定を行います。

以下のパーミッションタグを、プロジェクトのAndroidManifest.xml内`<application>`タグの直前に追加してください。

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## 3. SDKの初期化
メインのActivityクラス内で以下のメソッドを呼び出すことで、SDKが使用できるようになります。

```
ECHO.initialize(this, "アカウントドメイン", "アプリケーションID", "アプリケーションキー");
```

第1引数にはActivityのコンテキスト(this)、第2引数にはアカウントのドメイン名、第3・第4引数には先ほど取得した**アプリケーションID**と**アプリケーションキー**を渡します。


# 使用例

## データベースレコードの登録
```
ECHORecordObject new_record = new ECHORecordObject("データベースインスタンスID");

new_record.put("title", "新しいレコード"); // レコードタイトルをセット
new_record.put("refid", "record_20150219"); // レコードIDをセット
// .....

// 非同期でプッシュ（保存）
new_record.pushInBackground(new PushCallback<ECHORecordObject>() {

	@Override // プッシュ完了後に実行するコールバックメソッドを定義
	public void done(ECHORecordObject obj, ECHOException e) {
		if(e == null) {  // 正常終了
			Log.d("Complete", obj.toString());
		}else{ // 異常発生
			Log.e("Error", e.toString());
		}
	}

});
```

## メンバー（会員）ログイン

```
try {
  ECHOMemberObject login_member = ECHOMemberQuery.login("メンバーインスタンスID", "ログインID", "ログインパスワード");
} catch (ECHOException e) { // ログインエラー
  Log.e("Error", e.toString());
}
```


# 関連ドキュメント

[ECHO-Android-SDKガイド](http://echopf.com/docs/sdk/android)

[ECHO-Android-SDKリファレンス(javadoc)](http://echopf.com/sdk/android/javadoc/index.html)

[ECHO基本ガイド](http://echopf.com/docs/guide)


# License

本SDKは、Apache License Version 2.0に準拠します。
