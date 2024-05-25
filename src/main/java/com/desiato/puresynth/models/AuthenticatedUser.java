package com.desiato.puresynth.models;

public record AuthenticatedUser(User user, String authToken) {}
