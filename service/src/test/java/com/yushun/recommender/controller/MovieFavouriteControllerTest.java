package com.yushun.recommender.controller;

import com.yushun.recommender.RecommenderApplication;
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
public class MovieFavouriteControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Before()
    public void setup () {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    /**
     * test getUserMovieFavourite
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieFavourite_userLiked_findResult() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/movie/favourite/getUserMovieFavourite")
                .contentType(MediaType.APPLICATION_JSON)
                .param("movieId", "1")
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Successfully get movie favourite"));
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieFavourite_userNotLiked_resultNotExist() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/movie/favourite/getUserMovieFavourite")
                .contentType(MediaType.APPLICATION_JSON)
                .param("movieId", "4")
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("User haven't liked this movie"));
    }

    /**
     * test likeOrUnlikeMovie
     */
    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeMovie_userNotExist_userNotFind() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/movie/favourite/likeOrUnlikeMovie")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":\"4\", \"email\":\"990415zys@gmail.c\", \"favourite\":\"1\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Can not find this user"));
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeMovie_movieNotExist_movieNotFind() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/movie/favourite/likeOrUnlikeMovie")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":\"500\", \"email\":\"d18130495@mytudublin.ie\", \"favourite\":\"1\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("This movie is not exist in this system"));
    }

    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeMovie_likeMovie_like() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/movie/favourite/likeOrUnlikeMovie")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":\"4\", \"email\":\"d18130495@mytudublin.ie\", \"favourite\":\"3\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Successfully liked this movie"));
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeMovie_notLikeMovie_unlike() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/movie/favourite/likeOrUnlikeMovie")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":\"4\", \"email\":\"d18130495@mytudublin.ie\", \"favourite\":\"1\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Successfully unliked this movie"));
    }

    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void likeOrUnlikeMovie_markAsNormal_normal() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/movie/favourite/likeOrUnlikeMovie")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":\"4\", \"email\":\"d18130495@mytudublin.ie\", \"favourite\":\"2\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Successfully marked as normal"));
    }
}
