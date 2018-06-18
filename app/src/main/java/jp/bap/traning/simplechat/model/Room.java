package jp.bap.traning.simplechat.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Room extends RealmObject {
    @PrimaryKey
    int roomId;
    RealmList<User> users;
}