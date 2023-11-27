package com.tech.springbootlibraryservice.service;


import com.tech.springbootlibraryservice.dao.BookRepository;
import com.tech.springbootlibraryservice.dao.CheckoutRepository;
import com.tech.springbootlibraryservice.dao.HistoryRepository;
import com.tech.springbootlibraryservice.entity.Book;
import com.tech.springbootlibraryservice.entity.Checkout;
import com.tech.springbootlibraryservice.entity.History;
import com.tech.springbootlibraryservice.responseModel.ShelfCurrentLoansResponse;
import org.hibernate.annotations.Check;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;

    private CheckoutRepository checkoutRepository;

    private HistoryRepository historyRepository;

    public BookService(BookRepository pBookRepository, CheckoutRepository pCheckoutRepository, HistoryRepository historyRepository) {
        this.bookRepository = pBookRepository;
        this.checkoutRepository = pCheckoutRepository;
        this.historyRepository = historyRepository;

    }

    public Book checkoutBook(String userEmail, long bookId) throws Exception {

        Optional<Book> book = bookRepository.findById(bookId);

        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (!book.isPresent() || validateCheckout != null || book.get().getCopies() <= 0) {
            throw new Exception("Book doesn't exist or already checkout by the user");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable() - 1);
        bookRepository.save(book.get());
        Checkout checkout = new Checkout(userEmail, LocalDate.now().toString(),
                LocalDate.now().plusDays(7).toString(), book.get().getId());
        checkoutRepository.save(checkout);
        return book.get();

    }

    public boolean checkoutBookByUser(String userEmail, long bookId) {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if (validateCheckout != null) {
            return true;
        } else {
            return false;
        }
    }

    public Integer currentLoansCount(String userEmail) {
        List<Checkout> booksCheckedOutList = checkoutRepository.findBooksByUserEmail(userEmail);
        if (booksCheckedOutList != null && booksCheckedOutList.size() > 0) {
            return booksCheckedOutList.size();
        } else {
            return 0;
        }
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception {
        List<Checkout> checkoutbookList = checkoutRepository.findBooksByUserEmail(userEmail);
        List<ShelfCurrentLoansResponse> ShelfCurrentLoansResponse = new ArrayList<ShelfCurrentLoansResponse>();
        List<Long> bookids = new ArrayList<Long>();
        checkoutbookList.stream().forEach(checkout -> {
            bookids.add(checkout.getBookId());

        });
        List<Book> books = bookRepository.findBooksByBookId(bookids);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for (Book book : books) {
            Optional<Checkout> checkout = checkoutbookList.stream().filter(checkoutobj ->
                    checkoutobj.getBookId() == book.getId()).findFirst();
            if (checkout.isPresent()) {
                Date returnDate = format.parse(checkout.get().getReturnDate());
                Date currentDate = format.parse(LocalDate.now().toString());
                TimeUnit timeUnit = TimeUnit.DAYS;
                long daysLeft = timeUnit.convert(returnDate.getTime() - currentDate.getTime(), TimeUnit.MILLISECONDS);
                ShelfCurrentLoansResponse.add(new ShelfCurrentLoansResponse(book, (int) daysLeft));
            }
        }

        return ShelfCurrentLoansResponse;
    }

    public void returnBook(String userEmail, Long bookId) throws Exception{
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        Optional<Book> book = bookRepository.findById(bookId);
        if(validateCheckout == null || !book.isPresent() ){
            throw new Exception("Book does not exist or not checked out by the user");
        }
            checkoutRepository.deleteById(validateCheckout.getId());
            if(book.isPresent()){
                book.get().setCopiesAvailable(book.get().getCopiesAvailable() + 1);
            }
            bookRepository.save(book.get());

        History history = new History(validateCheckout.getUserEmail(),validateCheckout.getCheckoutDate()
                , LocalDate.now().toString(),book.get().getTitle(),book.get().getAuthor(),book.get().getDescription(),book.get().getImg() );

        historyRepository.save(history);

        }


    public void renewBook(String userEmail, Long bookId) throws Exception {
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail, bookId);
        if(validateCheckout == null ){
            throw new Exception("Book checked out by the user");
        }
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dddd");
        Date formattedReturnDate = format.parse(validateCheckout.getReturnDate());
        Date formattedCurrentDate = format.parse(LocalDate.now().toString());
        if(formattedReturnDate.compareTo(formattedCurrentDate) > 0 || formattedReturnDate.compareTo(formattedCurrentDate) ==0){
            validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());

        }
        checkoutRepository.save(validateCheckout);



    }
}



