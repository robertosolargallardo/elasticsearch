
package com.tbd.elasticsearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@ComponentScan
@RestController
@EnableAutoConfiguration
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookDao bookDao;

    public BookController(BookDao bookDao) {
        this.bookDao = bookDao;
    }

	@GetMapping("/{id}")
	public Map<String, Object> getBookById(@PathVariable String id){
	  return bookDao.getBookById(id);
	}
	
	@PostMapping
	public Book insertBook(@RequestBody Book book) throws Exception {
	  return bookDao.insertBook(book);
	}
	
	@PutMapping("/{id}")
	public Map<String, Object> updateBookById(@RequestBody Book book, @PathVariable String id) {
	  return bookDao.updateBookById(id, book);
	}
	
	@DeleteMapping("/{id}")
	public void deleteBookById(@PathVariable String id) {
	  bookDao.deleteBookById(id);
	}

}
