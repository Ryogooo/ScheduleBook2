package sample.android.example.schedulebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmTestActivity extends AppCompatActivity {

    Realm mRealm;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_test);

        //Realmのインスタンスを取得
        mRealm = Realm.getDefaultInstance();

        mTextView = (TextView)findViewById(R.id.textView);
        Button create = (Button)findViewById(R.id.create);
        Button read = (Button)findViewById(R.id.read);
        Button update = (Button)findViewById(R.id.update);
        Button delete = (Button)findViewById(R.id.delete);

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //登録処理
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        Number max = realm.where(Schedule.class).max("id");
                        long newId= 0;
                        if(max != null){
                            newId = max.longValue()+1;
                        }
                        Schedule schedule
                                = realm.createObject(Schedule.class,newId);
                        schedule.date = new Date();
                        schedule.title = "登録テスト";
                        schedule.detail = "スケジュールの詳細情報です";

                        //保存するスケジュールをTextViewに表示する
                        //toStringはオブジェクトの内容を文字列で表示する
                        mTextView.setText("登録しました＼n"
                        +schedule.toString());
                    }
                });
            }
        });

        //取得処理
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        RealmResults<Schedule> schedules
                                = realm.where(Schedule.class).findAll();

                        mTextView.setText("取得");
                        for (Schedule schedule:
                                schedules) {
                            String text = mTextView.getText() + "＼n"
                                    + schedule.toString();
                            mTextView.setText(text);
                        }


                    }
                });
            }
        });

        //更新処理
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Schedule schedule = realm.where(Schedule.class)
                                .equalTo("id",0)
                                .findFirst();//検索結果から最初の一件だけを返す
                        schedule.title += "<更新>";
                        schedule.detail += "<詳細>";

                        mTextView.setText("更新しました＼n"
                        + schedule.toString());
                    }
                });
            }
        });

        //削除処理
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        Number min = realm.where(Schedule.class).min("id");
                        if(min != null){//nullチェック

                        }
                        Schedule schedule = realm.where(Schedule.class)
                                .equalTo("id",min.longValue())
                                .findFirst();

                        schedule.deleteFromRealm();

                        mTextView.setText("削除しました＼n"
                        + schedule.toString());
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }
}
