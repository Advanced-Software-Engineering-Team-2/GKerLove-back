package com.gker.gkerlove.service;

import com.alibaba.fastjson.JSONObject;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.util.HttpUtils;
import com.tencentyun.TLSSigAPIv2;
import org.springframework.stereotype.Service;



@Service
public class ImService {
    public String Imregister(User user) {
        TLSSigAPIv2 api = new TLSSigAPIv2(1600009914, "d82b484df8d55fc6077400a56a4a42a5ef7ce2a53ee05777e7b416f6ee6d0c79");
        String Sig = api.genUserSig("administrator", 60);
        String url = "https://console.tim.qq.com/v4/im_open_login_svc/account_import?sdkappid=1600009914&identifier=administrator&usersig="+Sig+"&random=99999999&contenttype=json";
        JSONObject param = new JSONObject();
        param.put("UserID",user.getUsername());
        param.put("Nick",user.getUsername());
        param.put("FaceUrl","");
        return HttpUtils.sendPost(url, param.toJSONString());
    }
}
