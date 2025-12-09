package com.project;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class Manager {

    private static SessionFactory factory;

    public static void createSessionFactory() {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static void close() {
        if (factory != null) factory.close();
    }

    // GENERIC EXECUTORS
    private static void exec(SessionAction action) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            action.execute(session);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
        }
    }

    private static <T> T execResult(SessionFunction<T> f) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            T result = f.execute(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            return null;
        }
    }

    public interface SessionAction { void execute(Session session); }
    public interface SessionFunction<T> { T execute(Session session); }

    // CRUD CIUTAT
    public static Ciutat addCiutat(String nom, String pais, int poblacio) {
        return execResult(s -> {
            Ciutat c = new Ciutat(nom, pais, poblacio);
            s.persist(c);
            return c;
        });
    }

    public static void updateCiutat(long id, String nom, String pais, int poblacio) {
        exec(s -> {
            Ciutat c = s.get(Ciutat.class, id);
            if (c != null) {
                c.setNom(nom);
                c.setPais(pais);
                c.setPoblacio(poblacio);
                s.merge(c);
            }
        });
    }

    public static <T> void delete(Class<T> c, long id) {
        exec(s -> {
            T obj = s.get(c, id);
            if (obj != null) s.remove(obj);
        });
    }

    public static <T> List<T> list(Class<T> c) {
        return execResult(s -> s.createQuery("FROM " + c.getName(), c).list());
    }

    // CRUD CIUTADA
    public static Ciutada addCiutada(String nom, String cognom, int edat) {
        return execResult(s -> {
            Ciutada ci = new Ciutada(nom, cognom, edat);
            s.persist(ci);
            return ci;
        });
    }

    public static void updateCiutada(long id, String nom, String cognom, int edat) {
        exec(s -> {
            Ciutada ci = s.get(Ciutada.class, id);
            if (ci != null) {
                ci.setNom(nom);
                ci.setCognom(cognom);
                ci.setEdat(edat);
                s.merge(ci);
            }
        });
    }
}
