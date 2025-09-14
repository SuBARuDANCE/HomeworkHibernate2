package com.example.dao;

import com.example.model.User;
import com.example.util.HibernateUtil;
import com.example.exception.UserException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAOImpl implements UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAOImpl.class.getName());

    @Override
    public User createUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
            logger.info("User created successfully: " + user.getEmail());
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error creating user: " + user.getEmail(), e);
            throw new UserException("Failed to create user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving user with id: " + id, e);
            throw new UserException("Failed to retrieve user: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving all users", e);
            throw new UserException("Failed to retrieve users: " + e.getMessage(), e);
        }
    }

    @Override
    public User updateUser(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(user);
            transaction.commit();
            logger.info("User updated successfully: " + user.getEmail());
            return user;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error updating user: " + user.getEmail(), e);
            throw new UserException("Failed to update user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean deleteUser(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
                transaction.commit();
                logger.info("User deleted successfully: " + id);
                return true;
            }
            return false;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.log(Level.SEVERE, "Error deleting user with id: " + id, e);
            throw new UserException("Failed to delete user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);
            return query.uniqueResultOptional();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving user by email: " + email, e);
            throw new UserException("Failed to retrieve user by email: " + e.getMessage(), e);
        }
    }
}