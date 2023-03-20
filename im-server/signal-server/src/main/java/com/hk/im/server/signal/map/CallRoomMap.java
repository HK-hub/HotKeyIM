package com.hk.im.server.signal.map;

import com.hk.im.server.signal.room.CallRoom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : HK意境
 * @ClassName : CallRoomMap
 * @date : 2023/3/20 15:36
 * @description : 通话房间映射
 * @Todo :
 * @Bug :
 * @Modified :
 * @Version : 1.0
 */
public class CallRoomMap {

    private static final Map<String, CallRoom> roomMap = new ConcurrentHashMap<>();




}
