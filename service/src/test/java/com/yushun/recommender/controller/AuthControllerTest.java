package com.yushun.recommender.controller;

import com.yushun.recommender.RecommenderApplication;
import com.yushun.recommender.security.user.UserRepository;
import com.yushun.recommender.security.utils.JwtTokenProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommenderApplication.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Before()
    public void setup () {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    /**
     * test userSystemRegister
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void userSystemRegister_validDetail_success() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/authentication/userSystemRegister")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"test\", \"email\":\"990415zys@gmail.com\",\"password\":\"Qpuur990415#zys\",\"verification\":\"123456\"}");

        MvcResult response = mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test userSystemLogin
     */
    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void uuserSystemLogin_validDetail_success() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/authentication/userSystemLogin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"990415zys@gmail.com\",\"password\":\"Qpuur990415#zys\"}");

        MvcResult response = mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test googleLogin
     */
    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void googleLogin_validDetail_success() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/authentication/googleLogin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"d18130495@mytudublin.ie\", \"username\":\"test\", \"avatar\":\"test\"}");;

        MvcResult response = mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test tokenLoginRefresh
     */
    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void tokenLoginRefresh_validDetail_success() throws Exception {
        String newToken = jwtTokenProvider.createToken("990415zys@gmail.com",
                userRepository.findByUsername("990415zys@gmail.com")
                        .orElseThrow(() -> new UsernameNotFoundException("User " + "990415zys@gmail.com" + "not found")).getRoles()
        );

        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/authentication/tokenLoginRefresh")
                .contentType(MediaType.APPLICATION_JSON)
                .param("token", newToken);

        MvcResult response = mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }
}
