package org.delphy.tokenheroserver.service;

import org.delphy.tokenheroserver.pojo.OracleNewsVo;
import org.delphy.tokenheroserver.pojo.OracleResultVo;
import org.delphy.tokenheroserver.pojo.OracleVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OracleServiceTest {
    @Autowired
    private OracleService oracleService;

    @Test
    public void testGetNews() {
        OracleNewsVo oracleNewsVo = oracleService.getNews(119L);
        System.out.println(oracleNewsVo);
    }
    @Test
    public void testGetResult() {
        OracleVo<OracleResultVo> oracleResultVo = oracleService.getResult(121L);
        System.out.println(oracleResultVo);
        oracleResultVo = oracleService.getResult(119L);
        System.out.println(oracleResultVo);
    }
}
