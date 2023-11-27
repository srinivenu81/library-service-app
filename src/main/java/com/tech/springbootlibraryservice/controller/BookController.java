package com.tech.springbootlibraryservice.controller;


import com.tech.springbootlibraryservice.entity.Book;
import com.tech.springbootlibraryservice.responseModel.ShelfCurrentLoansResponse;
import com.tech.springbootlibraryservice.service.BookService;
import com.tech.springbootlibraryservice.utils.ExtractJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin("https://libraryservice-demo.azurewebsites.net:8080")
public class BookController {

    @Autowired
    private BookService bookService ;

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestHeader(value = "Authorization") String token, @RequestParam Long bookId) throws Exception {
        String userEmail = ExtractJwt.payLoadJwtExtraction(token,"\"sub\"");
        System.out.println("userName extracted from Jwt token " + userEmail);
        return bookService.checkoutBook(userEmail, bookId);
    }

    @GetMapping("/secure/isbookcheckedoutbyuser")
    public Boolean isBookCheckedOut(@RequestHeader(value="Authorization") String token,@RequestParam Long bookId){
        String userEmail = ExtractJwt.payLoadJwtExtraction(token,"\"sub\"");
        return  bookService.checkoutBookByUser(userEmail,bookId);

    }

    @GetMapping("/secure/currentloans/count")
    public Integer currentLoansCount(@RequestHeader(value="Authorization") String token){
        String userEmail = ExtractJwt.payLoadJwtExtraction(token,"\"sub\"");
        return  bookService.currentLoansCount(userEmail);
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value="Authorization") String token) throws Exception {
        String userEmail = ExtractJwt.payLoadJwtExtraction(token,"\"sub\"");
        return  bookService.currentLoans(userEmail);
    }

    @PutMapping("/secure/returnbook")
    public void  returnBook(@RequestHeader(value = "Authorization") String token , Long bookId) throws Exception{
        String userEmail = ExtractJwt.payLoadJwtExtraction(token,"\"sub\"");
        bookService.returnBook(userEmail,bookId);
    }


    @PutMapping("/secure/renewBook")
    public void  renewBook(@RequestHeader(value = "Authorization") String token , Long bookId) throws Exception{
        String userEmail = ExtractJwt.payLoadJwtExtraction(token,"\"sub\"");
        bookService.renewBook(userEmail,bookId);
    }




}
