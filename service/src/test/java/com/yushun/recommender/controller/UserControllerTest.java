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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
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
import org.springframework.web.multipart.MultipartFile;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommenderApplication.class)
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class UserControllerTest {
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
     * test getUserDetailByToken
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByToken_findUser_userDetail() throws Exception {
        String newToken = jwtTokenProvider.createToken("990415zys@gmail.com",
                userRepository.findByUsername("990415zys@gmail.com")
                        .orElseThrow(() -> new UsernameNotFoundException("User " + "990415zys@gmail.com" + "not found")).getRoles()
        );

        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/user/getUserDetailByToken")
                .contentType(MediaType.APPLICATION_JSON)
                .param("token", newToken);

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test getUserDetailByEmail
     */
    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByEmail_findUser_userDetail() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/user/getUserDetailByEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Successfully find user"));
    }

    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void getUserDetailByEmail_userNotExist_notFind() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/user/getUserDetailByEmail")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "990415zys@gmail.c");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Can not find user"));
    }

    /**
     * test updateUserDetail
     */
    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void updateUserDetail_validDetail_updatedDetail() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .put("/user/updateUserDetail")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"test\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test updateUserAvatar
     */
    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void updateUserAvatar_validDetail_updatedAvatar() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "Hello, World!".getBytes());
        MultipartFile file = mockFile;

        String newToken = jwtTokenProvider.createToken("990415zys@gmail.com",
                userRepository.findByUsername("990415zys@gmail.com")
                        .orElseThrow(() -> new UsernameNotFoundException("User " + "990415zys@gmail.com" + "not found")).getRoles()
        );

        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .multipart("/user/updateUserAvatar")
                .file(mockFile)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .header("Authorization", "Bearer " + newToken);

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test updateSystemUserPassword
     */
    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void updateSystemUserPassword_validDetail_updatedPassword() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .put("/user/updateSystemUserPassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"990415zys@gmail.com\", \"oldPassword\":\"Qpuur990415#zys\", \"newPassword\":\"Qpuur990415#zys\", \"verification\":\"123456\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test getUserLikeAndRatingMovieCount
     */
    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingMovieCount_validDetail_getMovieCount() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/user/getUserLikeAndRatingMovieCount")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test getUserLikeAndRatingBookCount
     */
    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void getUserLikeAndRatingBookCount_validDetail_getBookCount() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/user/getUserLikeAndRatingBookCount")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test getUserMovieLikeList
     */
    @Order(9)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieLikeList_findMovieList_movieList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/user/getUserMovieLikeList")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test getUserMovieRatingList
     */
    @Order(10)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieRatingList_findMovieList_movieList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/user/getUserMovieRatingList")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test getUserBookLikeList
     */
    @Order(11)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookLikeList_findBookList_bookList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/user/getUserBookLikeList")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test getUserBookRatingList
     */
    @Order(12)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookRatingList_findBookList_bookList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/user/getUserBookRatingList")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }
}
