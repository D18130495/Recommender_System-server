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
public class BookRatingControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Before()
    public void setup () {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    /**
     * test getUserBookRating
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookRating_userRated_findResult() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/book/rating/getUserBookRating")
                .contentType(MediaType.APPLICATION_JSON)
                .param("isbn", "0805076557")
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Successfully get book rating"));
    }

    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getUserBookRating_userNotRated_findResult() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/book/rating/getUserBookRating")
                .contentType(MediaType.APPLICATION_JSON)
                .param("isbn", "067973855X")
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("User not rating for this book"));
    }

    /**
     * test addOrUpdateUserBookRating
     */
    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserBookRating_userNotExist_userNotFind() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/book/rating/addOrUpdateUserBookRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"0545935202\", \"email\":\"990415zys@gmail.c\", \"rating\":\"4\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Can not find this user"));
    }

    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserBookRating_bookNotExist_bookNotFind() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/book/rating/addOrUpdateUserBookRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"0545935201\", \"email\":\"d18130495@mytudublin.ie\", \"rating\":\"4\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("This book is not exist in this system"));
    }

    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserBookRating_rateBook_rated() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/book/rating/addOrUpdateUserBookRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"0771065175\", \"email\":\"d18130495@mytudublin.ie\", \"rating\":\"3\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Successfully rating the book"));
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void addOrUpdateUserBookRating_updateBookRating_updatedRating() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .post("/book/rating/addOrUpdateUserBookRating")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"0805076557\", \"email\":\"d18130495@mytudublin.ie\", \"rating\":\"4\"}");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertTrue(response.getResponse().getContentAsString().contains("Successfully updated rating"));
    }
}
