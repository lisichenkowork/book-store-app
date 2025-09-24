package mate.academy.bookstoreappspring.repository;

import mate.academy.bookstoreappspring.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can`t add book" + e);
        } finally {
            session.close();
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        Session session = sessionFactory.openSession();
        Query<Book> fromProduct = session.createQuery("FROM Book", Book.class);
        return fromProduct.getResultList();
    }
}
