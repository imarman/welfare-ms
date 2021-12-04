package com.welfaresystem;

import cn.hutool.crypto.digest.MD5;
import com.manage.WelfareMsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = WelfareMsApplication.class)
class WelfareMsApplicationTests {

    @Test
    void contextLoads() {

        String s = MD5.create().digestHex16("123");
        System.out.println(s);
    }

}
