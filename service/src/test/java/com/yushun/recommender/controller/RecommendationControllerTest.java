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
public class RecommendationControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Before()
    public void setup () {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    /**
     * test getMoviesLikeThis
     */
    @Order(1)
    @Test(timeout = 30000)
    @Transactional
    public void getMoviesLikeThis_findMovieList_movieList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/recommendation/getMoviesLikeThis")
                .contentType(MediaType.APPLICATION_JSON)
                .param("movieId", "1");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test getBooksLikeThis
     */
    @Order(2)
    @Test(timeout = 30000)
    @Transactional
    public void getBooksLikeThis_findBookList_bookList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/recommendation/getBooksLikeThis")
                .contentType(MediaType.APPLICATION_JSON)
                .param("isbn", "0805076557");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test getRecommendMovieListByItemCF
     */
    @Order(3)
    @Test(timeout = 30000)
    @Transactional
    public void getRecommendMovieListByItemCF_findMovieList_movieList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/recommendation/getRecommendMovieListByItemCF")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test getRecommendMovieListByUserCF
     */
    @Order(4)
    @Test(timeout = 30000)
    @Transactional
    public void getRecommendMovieListByUserCF_movie_findMovieList_movieList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/recommendation/getRecommendMovieListByUserCF")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie")
                .param("type", "movie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    @Order(5)
    @Test(timeout = 30000)
    @Transactional
    public void getRecommendMovieListByUserCF_book_findMovieList_movieList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/recommendation/getRecommendMovieListByUserCF")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie")
                .param("type", "book");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    @Order(6)
    @Test(timeout = 30000)
    @Transactional
    public void getRecommendMovieListByUserCF_badOperation_findMovieList_movieList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/recommendation/getRecommendMovieListByUserCF")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie")
                .param("type", "test");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test getRecommendBookListByItemCF
     */
    @Order(7)
    @Test(timeout = 30000)
    @Transactional
    public void getRecommendBookListByItemCF_findBookList_bookList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/recommendation/getRecommendBookListByItemCF")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie");

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    /**
     * test getRecommendBookListByUserCF
     */
    @Order(8)
    @Test(timeout = 30000)
    @Transactional
    public void getRecommendBookListByUserCF_movie_findMovieList_movieList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/recommendation/getRecommendBookListByUserCF")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie")
                .param("type", "movie");;

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    @Order(9)
    @Test(timeout = 30000)
    @Transactional
    public void getRecommendBookListByUserCF_book_findMovieList_movieList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/recommendation/getRecommendBookListByUserCF")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie")
                .param("type", "book");;

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }

    @Order(10)
    @Test(timeout = 30000)
    @Transactional
    public void getRecommendBookListByUserCF_badOperation_findMovieList_movieList() throws Exception {
        MockHttpServletRequestBuilder postRequestBuilder = MockMvcRequestBuilders
                .get("/recommendation/getRecommendBookListByUserCF")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", "d18130495@mytudublin.ie")
                .param("type", "test");;

        MvcResult response =  mockMvc.perform(postRequestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        Assert.assertNotNull(response.getResponse().getContentAsString());
    }
}
