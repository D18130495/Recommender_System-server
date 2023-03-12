package com.yushun.recommender.service.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.yushun.recommender.excel.*;
import com.yushun.recommender.model.user.BookRating;
import com.yushun.recommender.service.BookService;
import com.yushun.recommender.service.ExcelService;
import com.yushun.recommender.service.MovieService;
import com.yushun.recommender.service.UserService;
import com.yushun.recommender.vo.user.book.BookLikeListReturnVo;
import com.yushun.recommender.vo.user.book.BookRatingListReturnVo;
import com.yushun.recommender.vo.user.book.BookRatingReturnVo;
import com.yushun.recommender.vo.user.movie.MovieLikeListReturnVo;
import com.yushun.recommender.vo.user.movie.MovieRatingListReturnVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * <p>
 * Excel Service Implementation
 * </p>
 *
 * @author yushun zeng
 * @since 2023-3-7
 */

@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private BookService bookService;

//    @Override
//    public void exportUserData(HttpServletResponse response, String email) {
//        Workbook workBook = null;
//
//        try {
//            ExportParams userExportParams = new ExportParams();
//
//            userExportParams.setSheetName("Your detail");
//
//            userExportParams.setTitle("Your detail");
//
//            Map<String, Object> userExportMap = new HashMap<>();
//
//            userExportMap.put("title", userExportParams);
//
//            userExportMap.put("entity", UserDataExportVo.class);
//
//            // get username
//            UserDetailReturnVo userDetailByEmail = userService.getUserDetailByEmail(email);
//            UserDataExportVo userDataExportVo = new UserDataExportVo();
//            userDataExportVo.setUsername(userDetailByEmail.getUsername());
//
//            List<UserDataExportVo> users = new ArrayList<>();
//            users.add(userDataExportVo);
//
//            userExportMap.put("data", users);
//
//            List<Map<String, Object>> sheetsList = new ArrayList<>();
//            sheetsList.add(userExportMap);
//
//            workBook = ExcelExportUtil.exportExcel(sheetsList, ExcelType.HSSF);
//
//            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//
//            response.setContentType("application/octet-stream");
//
//            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(email + ".xls", StandardCharsets.UTF_8.name()));
//
//            workBook.write(response.getOutputStream());
//        }catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            if(workBook != null) {
//                try {
//                    workBook.close();
//                }catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    @Override
    public void exportUserData(HttpServletResponse response, String email) {
        Workbook workBook = null;

        try {
            List<ResultEntity> resultEntityList = new ArrayList<>();
            ResultEntity resultEntity = new ResultEntity();

            // user detail
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(userService.getUserDetailByEmail(email).getUsername());
            userEntity.setAvatar(userService.getUserDetailByEmail(email).getAvatar());

            resultEntity.setUserDetail(userEntity);

            // movie favourite
            List<MovieFavouriteEntity> movieFavouriteEntities = new ArrayList<>();

            List<MovieLikeListReturnVo> userMovieLikeList = movieService.getUserMovieLikeList(email);

            if(userMovieLikeList != null) {
                for(MovieLikeListReturnVo movie:userMovieLikeList) {
                    MovieFavouriteEntity movieFavouriteEntity = new MovieFavouriteEntity();
                    movieFavouriteEntity.setMovieTitle(movie.getTitle());
                    movieFavouriteEntity.setFavouriteStatus(movie.getFavourite());

                    movieFavouriteEntities.add(movieFavouriteEntity);
                }
            }else {
                movieFavouriteEntities.add(new MovieFavouriteEntity("", ""));
            }

            resultEntity.setMovieFavourite(movieFavouriteEntities);

            // movie rating
            List<MovieRatingEntity> movieRatingEntities = new ArrayList<>();

            List<MovieRatingListReturnVo> userMovieRatingList = movieService.getUserMovieRatingList(email);

            if(userMovieRatingList != null) {
                for(MovieRatingListReturnVo movie:userMovieRatingList) {
                    MovieRatingEntity movieRatingEntity = new MovieRatingEntity();
                    movieRatingEntity.setMovieTitle(movie.getTitle());
                    movieRatingEntity.setMovieRating(Float.toString(movie.getRating()));

                    movieRatingEntities.add(movieRatingEntity);
                }
            }else {
                movieRatingEntities.add(new MovieRatingEntity("", ""));
            }

            resultEntity.setMovieRating(movieRatingEntities);

            // book favourite
            List<BookFavouriteEntity> bookFavouriteEntities = new ArrayList<>();

            List<BookLikeListReturnVo> userBookLikeList = bookService.getUserBookLikeList(email);

            if(userBookLikeList != null) {
                for(BookLikeListReturnVo book:userBookLikeList) {
                    BookFavouriteEntity bookFavouriteEntity = new BookFavouriteEntity();
                    bookFavouriteEntity.setBookTitle(book.getTitle());
                    bookFavouriteEntity.setFavouriteStatus(book.getFavourite());

                    bookFavouriteEntities.add(bookFavouriteEntity);
                }
            }else {
                bookFavouriteEntities.add(new BookFavouriteEntity("",""));
            }

            resultEntity.setBookFavourite(bookFavouriteEntities);

            // book rating
            List<BookRatingEntity> bookRatingEntities = new ArrayList<>();

            List<BookRatingListReturnVo> userBookRatingList = bookService.getUserBookRatingList(email);

            if(userBookRatingList != null) {
                for(BookRatingListReturnVo book:userBookRatingList) {
                    BookRatingEntity bookRatingEntity = new BookRatingEntity();
                    bookRatingEntity.setBookTitle(book.getTitle());
                    bookRatingEntity.setBookRating(Float.toString(book.getRating()));

                    bookRatingEntities.add(bookRatingEntity);
                }
            }else {
                bookFavouriteEntities.add(new BookFavouriteEntity("",""));
            }

            resultEntity.setBookRating(bookRatingEntities);

            // form response
            resultEntityList.add(resultEntity);

            workBook = ExcelExportUtil.exportExcel( new ExportParams("Your Detail", null, "Detail"),
                    ResultEntity.class, resultEntityList);

            response.setCharacterEncoding(StandardCharsets.UTF_8.name());

            response.setContentType("application/octet-stream");

            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(email + ".xls", StandardCharsets.UTF_8.name()));

            workBook.write(response.getOutputStream());
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(workBook != null) {
                try {
                    workBook.close();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
