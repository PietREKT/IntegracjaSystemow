package com.lg;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello, World!");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Hibernate_JPA");

        EntityManager em = factory.createEntityManager();

        //Create Roles and Users

        em.getTransaction().begin();

        Role r1 = new Role(null, "ADMIN");
        Role r2 = new Role(null, "USER");
        Role r3 = new Role(null, "MODERATOR");
        Role r4 = new Role(null, "GUEST");
        Role r5 = new Role(null, "SUPER_ADMIN");

        UserGroup g1 = new UserGroup();
        UserGroup g2 = new UserGroup();
        UserGroup g3 = new UserGroup();

        List<UserGroup> groups1 = new ArrayList<>(List.of(g1, g2));
        List<UserGroup> groups2 = new ArrayList<>(List.of(g2, g3));

        User u1 = new User(null, "test_1", "test_1", "Andrzej", "Kowalski", User.Sex.MALE, new ArrayList<>(List.of(r2)), groups1, null);
        User u2 = new User(null, "test_2", "test_2", "Jan", "Nowak", User.Sex.MALE, new ArrayList<>(List.of(r1)), groups2, null);
        User u3 = new User(null, "test_3", "test_3", "Anna", "Wiśniewska", User.Sex.FEMALE, new ArrayList<>(List.of(r3)), groups2, null);
        User u4 = new User(null, "test_4", "test_4", "Maria", "Zielińska", User.Sex.FEMALE, new ArrayList<>(List.of(r4)), groups1, null);
        User u5 = new User(null, "test_5", "test_5", "Piotr", "Lewandowski", User.Sex.MALE, new ArrayList<>(List.of(r5)), groups1, null);

        em.persist(r1);
        em.persist(r2);
        em.persist(r3);
        em.persist(r4);
        em.persist(r5);

        em.persist(g1);
        em.persist(g2);
        em.persist(g3);

        em.persist(u1);
        em.persist(u2);
        em.persist(u3);
        em.persist(u4);
        em.persist(u5);

        em.getTransaction().commit();

        //Find user with id 5
        User fU = em.find(User.class, 1L);
        fU.setPassword("newpassword");
        em.merge(fU);


        //Delete role with id 5
        em.getTransaction().begin();
        Role r = em.find(Role.class, 5L);
        List<User> usersWithRole = em.createQuery("select u from User u where :role member of u.roles", User.class)
                        .setParameter("role", r)
                                .getResultList();
        usersWithRole.forEach(u -> u.getRoles().remove(r));
        em.remove(r);
        em.getTransaction().commit();



        em.getTransaction().begin();
        Query query = em.createQuery("SELECT u FROM User u WHERE u.sex = 'FEMALE'");
        List<User> kobiety = query.getResultList();
        em.getTransaction().commit();

        kobiety.forEach(u -> {
            System.out.println(u.toString());
        });

        em.getTransaction().begin();
        File file = new File("C:\\Users\\Pjoter\\IdeaProjects\\Lab4\\src\\main\\resources\\pngtree-blue-bird-vector-or-color-illustration-png-image_2013004.jpg");
        fU = em.find(User.class, 1L);
        byte[] bytes = Files.readAllBytes(file.toPath());
        fU.setImage(bytes);
        em.merge(fU);
        em.getTransaction().commit();

        em.close();
        factory.close();
    }
}