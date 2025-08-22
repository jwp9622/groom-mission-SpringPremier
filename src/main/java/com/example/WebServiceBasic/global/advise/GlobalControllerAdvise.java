package com.example.WebServiceBasic.global.advise;

import com.example.WebServiceBasic.domain.user.exception.MemberNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalControllerAdvise extends RuntimeException {


    @ExceptionHandler(MemberNotFoundException.class)
    public ModelAndView handleMemberNotFound(MemberNotFoundException e, RedirectAttributes redirectAttributes) {

        System.out.println("ExceptionHandler  500 -111");

        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/500");
        mv.addObject("errorMessage", e.getMessage());
        return mv;
    }

    //404 error
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundException(NoHandlerFoundException e){

        // console에 error message 출력
        System.out.println("ExceptionHandler  404");

        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/404");
        mv.addObject("errorMessage", "요청하신 페이지를 찾을 수 없습니다.");
        return mv;
    }

    //403 error
    @ExceptionHandler(value = {HttpClientErrorException.Forbidden.class})
    public ModelAndView handleForbiddenException(HttpClientErrorException.Forbidden e){

        // console에 error message 출력
        System.out.println("ExceptionHandler  403");

        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/403");
        mv.addObject("errorMessage", "서버에 오류가 발생했습니다: " + e.getMessage());
        return mv;

    }

    //500 error
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception e){
        //
        // console에 error message 출력
        System.out.println("ExceptionHandler  500 222");

        ModelAndView mv = new ModelAndView();
        mv.setViewName("error/500");
        mv.addObject("errorMessage", "서버에 오류가 발생했습니다: " + e.getMessage());
        return mv;
    }




}
