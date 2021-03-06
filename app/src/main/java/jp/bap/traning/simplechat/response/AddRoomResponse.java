package jp.bap.traning.simplechat.response;

import jp.bap.traning.simplechat.model.RoomData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddRoomResponse extends BaseResponse {
    RoomData data;
}
