package com.gker.gkerlove.service;


import com.alibaba.fastjson.JSONObject;
import com.gker.gkerlove.util.HttpUtils;
import com.tencentyun.TLSSigAPIv2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeetingService {
    public String MeetingAddlover(String formusername,String tousername) {
        TLSSigAPIv2 api = new TLSSigAPIv2(1600009914, "d82b484df8d55fc6077400a56a4a42a5ef7ce2a53ee05777e7b416f6ee6d0c79");
        String Sig = api.genUserSig("administrator", 60);
        String url = "https://console.tim.qq.com/v4/sns/friend_add?sdkappid=1600009914&identifier=administrator&usersig="+Sig+"&random=99999999&contenttype=json";

        JSONObject param = new JSONObject();
        param.put("From_Account",formusername);
        List<JSONObject> frienditems = new ArrayList<>();
        JSONObject frienditem = new JSONObject();
        frienditem.put("To_Account",tousername);
        frienditem.put("GroupName","我喜欢的人");
        frienditem.put("AddSource","AddSource_Type_Meeting");
        frienditem.put("AddWording","我将你列为了我喜欢的人，如果对我的个人信息感兴趣的话，就将我添加为好友开始聊天吧！");
        frienditems.add(frienditem);
        param.put("AddFriendItem",frienditems);
        return HttpUtils.sendPost(url, param.toJSONString());
    }
}
