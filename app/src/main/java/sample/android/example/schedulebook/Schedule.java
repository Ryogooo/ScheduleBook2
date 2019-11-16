package sample.android.example.schedulebook;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Schedule extends RealmObject {
    @PrimaryKey //同じIDがあるとエラーで教えてくれる
    public long id;//予定を見分けるためのIDが必要
    public Date date;//日付
    public String title;//タイトル
    public String detail;//詳細
}
