package com.example.SpringBot.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubUser {
    public String login;
    public int id;
    public String html_url;
    public String type;
    public String name;
}
