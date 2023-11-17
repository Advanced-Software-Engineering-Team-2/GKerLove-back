package com.gker.gkerlove.service;

import com.alibaba.fastjson.JSONObject;
import com.gker.gkerlove.bean.User;
import com.gker.gkerlove.bean.dto.UpdateUserInfoReq;
import com.gker.gkerlove.util.HttpUtils;
import com.tencentyun.TLSSigAPIv2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Service
public class ImService {
    public String Imregister(String username) {
        TLSSigAPIv2 api = new TLSSigAPIv2(1600009914, "d82b484df8d55fc6077400a56a4a42a5ef7ce2a53ee05777e7b416f6ee6d0c79");
        String Sig = api.genUserSig("administrator", 60);
        String url = "https://console.tim.qq.com/v4/im_open_login_svc/account_import?sdkappid=1600009914&identifier=administrator&usersig=" + Sig + "&random=99999999&contenttype=json";
        JSONObject param = new JSONObject();
        param.put("UserID", username);
        param.put("Nick", username);
        param.put("FaceUrl", "http://gker-love.oss-cn-beijing.aliyuncs.com/default-avatar");
        return HttpUtils.sendPost(url, param.toJSONString());
    }

    public String CreateFriendGroup(String username) {
        TLSSigAPIv2 api = new TLSSigAPIv2(1600009914, "d82b484df8d55fc6077400a56a4a42a5ef7ce2a53ee05777e7b416f6ee6d0c79");
        String Sig = api.genUserSig("administrator", 60);
        String url = "https://console.tim.qq.com/v4/sns/group_add?sdkappid=1600009914&identifier=administrator&usersig=" + Sig + "&random=99999999&contenttype=json";
        JSONObject param = new JSONObject();
        List<String> groupnames = new ArrayList<>();
        groupnames.add("我喜欢的人");
        groupnames.add("喜欢我的人");
        param.put("From_Account", username);
        param.put("GroupName", groupnames);
        return HttpUtils.sendPost(url, param.toJSONString());
    }
    public String UpdateProfile(User user, UpdateUserInfoReq updateUserInfoReq) {
        String profile = null;
        if(StringUtils.hasLength(updateUserInfoReq.getGender())){
            profile = "性别:" + updateUserInfoReq.getGender() + "; ";
        }
        if(updateUserInfoReq.getAge() != null){
            if(profile != null)
                profile += "年龄:" + updateUserInfoReq.getAge() + "; ";
            else
                profile = "年龄:" + updateUserInfoReq.getAge() + "; ";
        }
        if(StringUtils.hasLength(updateUserInfoReq.getCity())){
            if(profile != null)
                profile += "所在城市:" + updateUserInfoReq.getCity() + "; ";
            else
                profile = "所在城市:" + updateUserInfoReq.getCity() + "; ";
        }
        if(StringUtils.hasLength(updateUserInfoReq.getInstitute())){
            if(profile != null)
                profile += "培养单位:" + updateUserInfoReq.getInstitute() + "; ";
            else
                profile = "培养单位:" + updateUserInfoReq.getInstitute() + "; ";
        }
        if(profile != null){
            TLSSigAPIv2 api = new TLSSigAPIv2(1600009914, "d82b484df8d55fc6077400a56a4a42a5ef7ce2a53ee05777e7b416f6ee6d0c79");
            String Sig = api.genUserSig("administrator", 60);
            String url = "https://console.tim.qq.com/v4/profile/portrait_set?sdkappid=1600009914&identifier=administrator&usersig=" + Sig + "&random=99999999&contenttype=json";
            JSONObject param = new JSONObject();
            param.put("From_Account", user.getUsername());
            List<JSONObject> profileitems = new ArrayList<>();
            JSONObject slefsignature = new JSONObject();
            slefsignature.put("Tag","Tag_Profile_IM_SelfSignature");
            slefsignature.put("Value",profile);
            profileitems.add(slefsignature);
            JSONObject avatar = new JSONObject();
            avatar.put("Tag","Tag_Profile_IM_Image");
            avatar.put("Value",user.getAvatar());
            profileitems.add(avatar);
            param.put("ProfileItem",profileitems);
            return HttpUtils.sendPost(url, param.toJSONString());
        }
        else{
            TLSSigAPIv2 api = new TLSSigAPIv2(1600009914, "d82b484df8d55fc6077400a56a4a42a5ef7ce2a53ee05777e7b416f6ee6d0c79");
            String Sig = api.genUserSig("administrator", 60);
            String url = "https://console.tim.qq.com/v4/profile/portrait_set?sdkappid=1600009914&identifier=administrator&usersig=" + Sig + "&random=99999999&contenttype=json";
            JSONObject param = new JSONObject();
            param.put("From_Account", user.getUsername());
            List<JSONObject> profileitems = new ArrayList<>();
            JSONObject avatar = new JSONObject();
            avatar.put("Tag","Tag_Profile_IM_Image");
            avatar.put("Value",user.getAvatar());
            profileitems.add(avatar);
            param.put("ProfileItem",profileitems);
            return HttpUtils.sendPost(url, param.toJSONString());
        }
    }

}
