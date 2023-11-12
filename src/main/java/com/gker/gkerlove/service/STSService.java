package com.gker.gkerlove.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class STSService {
    @Value("${OSS.accessKeyId}")
    private String accessKeyId;
    @Value("${OSS.accessKeySecret}")
    private String accessKeySecret;
    @Value("${OSS.roleArn}")
    private String roleArn;

    public AssumeRoleResponse assumeRole(String roleSessionName) throws ClientException {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);

        AssumeRoleRequest request = new AssumeRoleRequest();
        request.setRoleArn(roleArn);
        request.setRoleSessionName(roleSessionName);
        request.setDurationSeconds(3600L);

        return client.getAcsResponse(request);
    }

}