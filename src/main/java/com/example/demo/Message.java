package com.example.demo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    @NotNull
    @Size(min=3)
    private String title;

    @Column(name = "content")
    @NotNull
    @Size(min=3, max=40)
    private String content;

    @Column(name = "date")
    @NotNull
    @Size(min=4)
    private String date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "uid")
    private long uid;

    public Message() {
    }

    public Message(@NotNull @Size(min = 3) String title, @NotNull @Size(min = 3, max = 20) String content, @NotNull @Size(min = 4) String date, User user) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.user = user;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}
