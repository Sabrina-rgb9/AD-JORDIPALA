package com.project;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import org.hibernate.Hibernate;
import org.hibernate.Session; 
import org.hibernate.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;

public class Manager {

    private static SessionFactory factory;

    public static void createSessionFactory() {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) { 
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex); 
        }
    }

    public static void close() {
        if (factory != null) factory.close();
    }
  
    // ============================================================
    // MÈTODES GENÈRICS PER TRANSACCIONS
    // ============================================================

    private static void executeInTransaction(Consumer<Session> action) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            action.accept(session);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error en transacció Hibernate", e);
        }
    }

    private static <T> T executeInTransactionWithResult(Function<Session, T> action) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            T result = action.apply(session);
            tx.commit();
            return result;
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw new RuntimeException("Error en transacció Hibernate", e);
        }
    }

    // ============================================================
    // OPERACIONS CRUD PER CIUTAT
    // ============================================================

    /**
     * CREATE: Afegeix una nova ciutat a la base de dades
     */
    public static Ciutat addCiutat(String nom, String pais, int poblacio) {
        return executeInTransactionWithResult(session -> {
            Ciutat ciutat = new Ciutat(nom, pais, poblacio);
            session.persist(ciutat);
            return ciutat;
        });
    }

    /**
     * CREATE: Afegeix un nou ciutadà a la base de dades
     */
    public static Ciutada addCiutada(String nom, String cognom, int edat) {
        return executeInTransactionWithResult(session -> {
            Ciutada ciutada = new Ciutada(nom, cognom, edat);
            session.persist(ciutada);
            return ciutada;
        });
    }

    /**
     * UPDATE: Actualitza una ciutat i les seves relacions amb ciutadans
     */
    public static void updateCiutat(long ciutatId, String nom, String pais, int poblacio, Set<Ciutada> newCiutadans) {
        executeInTransaction(session -> {
            Ciutat ciutat = session.get(Ciutat.class, ciutatId);
            if (ciutat == null) return;
            
            ciutat.setNom(nom);
            ciutat.setPais(pais);
            ciutat.setPoblacio(poblacio);
            
            // Si newCiutadans és null, no toquem les relacions existents
            if (newCiutadans != null) {
                // Netejar ciutadans existents (mantenint la coherència bidireccional)
                if (ciutat.getCiutadans() != null && !ciutat.getCiutadans().isEmpty()) {
                    // Creem una còpia per evitar ConcurrentModificationException
                    List<Ciutada> ciutadansToRemove = List.copyOf(ciutat.getCiutadans());
                    ciutadansToRemove.forEach(ciutat::removeCiutada);
                }

                // Afegir nous ciutadans (recuperant-los com a "managed")
                for (Ciutada ciutada : newCiutadans) {
                    Ciutada managedCiutada = session.get(Ciutada.class, ciutada.getCiutadaId());
                    if (managedCiutada != null) {
                        ciutat.addCiutada(managedCiutada);
                    }
                }
            }

            session.merge(ciutat);
        });
    }

    /**
     * UPDATE: Actualitza només les dades d'una ciutat (sense tocar relacions)
     */
    public static void updateCiutat(long ciutatId, String nom, String pais, int poblacio) {
        executeInTransaction(session -> {
            Ciutat ciutat = session.get(Ciutat.class, ciutatId);
            if (ciutat != null) {
                ciutat.setNom(nom);
                ciutat.setPais(pais);
                ciutat.setPoblacio(poblacio);
                session.merge(ciutat);
            }
        });
    }

    /**
     * UPDATE: Actualitza les dades d'un ciutadà
     */
    public static void updateCiutada(long ciutadaId, String nom, String cognom, int edat) {
        executeInTransaction(session -> {
            Ciutada ciutada = session.get(Ciutada.class, ciutadaId);
            if (ciutada != null) {
                ciutada.setNom(nom);
                ciutada.setCognom(cognom);
                ciutada.setEdat(edat);
                session.merge(ciutada);
            }
        });
    }

    /**
     * READ: Recupera una ciutat amb tots els seus ciutadans carregats
     */
    public static Ciutat getCiutatWithCiutadans(long ciutatId) {
        return executeInTransactionWithResult(session -> {
            Ciutat ciutat = session.get(Ciutat.class, ciutatId);
            if (ciutat != null) {
                // Força la càrrega de la col·lecció lazy
                Hibernate.initialize(ciutat.getCiutadans());
            }
            return ciutat;
        });
    }

    /**
     * DELETE: Elimina una entitat per la seva ID
     */
    public static <T> void delete(Class<T> clazz, Serializable id) {
        executeInTransaction(session -> {
            T obj = session.get(clazz, id);
            if (obj != null) {
                session.remove(obj);
            }
        });
    }

    /**
     * READ: Llista totes les entitats d'un tipus
     */
    public static <T> List<T> listCollection(Class<T> clazz, String whereClause) {
        return executeInTransactionWithResult(session -> {
            String hql = "FROM " + clazz.getName();
            if (whereClause != null && !whereClause.trim().isEmpty()) {
                hql += " WHERE " + whereClause;
            }
            return session.createQuery(hql, clazz).list();
        });
    }

    /**
     * Mètode auxiliar per convertir una col·lecció a String
     */
    public static <T> String collectionToString(Class<T> clazz, Collection<T> collection) {
        if (collection == null || collection.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(clazz.getSimpleName()).append("]\n");
        for (T obj : collection) {
            sb.append(obj.toString()).append("\n");
        }
        return sb.toString();
    }
}