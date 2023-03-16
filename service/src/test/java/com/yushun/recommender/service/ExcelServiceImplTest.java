package com.yushun.recommender.service;

import com.yushun.recommender.RecommenderApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommenderApplication.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExcelServiceImplTest {
    @Autowired
    private ExcelService excelService;

    /**
     * export user data
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void exportUserData_userExist_success() throws UnsupportedEncodingException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        excelService.exportUserData(response, "d18130495@mytudublin.ie");

        Assert.assertNotNull(response.getContentAsString());
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void exportUserData_userNotLikeOrRate_success() throws UnsupportedEncodingException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        excelService.exportUserData(response, "990415zys@gmail.co");

        Assert.assertNotNull(response.getContentAsString());
    }
}
