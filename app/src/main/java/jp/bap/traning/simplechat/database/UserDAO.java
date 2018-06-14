package jp.bap.traning.simplechat.database;

import io.realm.Realm;
import io.realm.RealmObject;
import jp.bap.traning.simplechat.model.User;

public class UserDAO {

    public UserDAO() {
    }

    public void insertOrUpdate(User user) {
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(realm -> {
            realm.insertOrUpdate(user);
        });
        mRealm.close();
    }

    public User getUser(int id) {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class)
                .equalTo("id", id)
                .findFirst();
        //realm.close();
        return user;
    }
}