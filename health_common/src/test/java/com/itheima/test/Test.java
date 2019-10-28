package com.itheima.test;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.utils.SMSUtils;

public class Test {
    public static void main(String[] args) throws ClientException {
        SMSUtils.sendShortMessage("SMS_175537552","18435223984","2234");
    }
}
