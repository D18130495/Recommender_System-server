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
public class MovieRatingControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Before()
    public void setup () {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    /**
     * test getUserMovieRating
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieRating_userRated_findResult() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/movie/rating/getUserMovieRating")
                .contentType(MediaType.APPLICATION_JSON)
                .param("movieId", "1")
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Successfully get movie rating"));
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getUserMovieRating_userNotRated_findResult() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/movie/rating/getUserMovieRating")
                .contentType(MediaType.APPLICATION_JSON)
                .param("movieId", "2")
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("User not rating for this movie"));
    }

    /**
     * test addOrUpdateUserMovieRating
     */
    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserMovieRating_userNotExist_userNotFind() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/movie/rating/addOrUpdateUserMovieRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":\"1\", \"email\":\"990415zys@gmail.c\", \"rating\":\"4\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Can not find this user"));
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserMovieRating_movieNotExist_movieNotFind() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/movie/rating/addOrUpdateUserMovieRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":\"500\", \"email\":\"d18130495@mytudublin.ie\", \"rating\":\"4\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("This book is not exist in this system"));
    }

    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserMovieRating_rateMovie_rated() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/movie/rating/addOrUpdateUserMovieRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":\"2\", \"email\":\"d18130495@mytudublin.ie\", \"rating\":\"3\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Successfully rating the movie"));
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserMovieRating_updateMovieRating_updatedRating() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/movie/rating/addOrUpdateUserMovieRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"movieId\":\"1\", \"email\":\"d18130495@mytudublin.ie\", \"rating\":\"4\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Successfully updated rating"));
    }
}
