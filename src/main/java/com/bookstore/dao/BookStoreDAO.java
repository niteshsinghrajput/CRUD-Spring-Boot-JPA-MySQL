package com.bookstore.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.entity.Book;

@Transactional
@Repository
public class BookStoreDAO implements IBookStoreDAO {
	
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * This method is responsible to get all books available in database and return it as List<Book>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Book> getBooks() {
		
		String hql = "FROM Book as atcl ORDER BY atcl.id";
		return (List<Book>) entityManager.createQuery(hql).getResultList();
	}

	/**
	 * This method is responsible to get a particular Book detail by given book id 
	 */
	@Override
	public Book getBook(int bookId) {
		
		return entityManager.find(Book.class, bookId);
	}

	/**
	 * This method is responsible to create new book in database
	 */
	@Override
	public Book createBook(Book book) {
		entityManager.persist(book);
		Book b = getLastInsertedBook();
		return b;
	}

	/**
	 * This method is responsible to update book detail in database
	 */
	@Override
	public Book updateBook(int bookId, Book book) {
		
		//First We are taking Book detail from database by given book id and 
		// then updating detail with provided book object
		Book bookFromDB = getBook(bookId);
		bookFromDB.setName(book.getName());
		bookFromDB.setAuthor(book.getAuthor());
		bookFromDB.setCategory(book.getCategory());
		bookFromDB.setPublication(book.getPublication());
		bookFromDB.setPages(book.getPages());
		bookFromDB.setPrice(book.getPrice());
		
		entityManager.flush();
		
		//again i am taking updated result of book and returning the book object
		Book updatedBook = getBook(bookId);
		
		return updatedBook;
	}

	/**
	 * This method is responsible for deleting a particular(which id will be passed that record) 
	 * record from the database
	 */
	@Override
	public boolean deleteBook(int bookId) {
		Book book = getBook(bookId);
		entityManager.remove(book);
		
		//we are checking here that whether entityManager contains earlier deleted book or not
		// if contains then book is not deleted from DB that's why returning false;
		boolean status = entityManager.contains(book);
		if(status){
			return false;
		}
		return true;
	}
	
	/**
	 * This method will get the latest inserted record from the database and return the object of Book class
	 * @return book
	 */
	private Book getLastInsertedBook(){
		String hql = "from Book order by id DESC";
		Query query = entityManager.createQuery(hql);
		query.setMaxResults(1);
		Book book = (Book)query.getSingleResult();
		return book;
	}

}
