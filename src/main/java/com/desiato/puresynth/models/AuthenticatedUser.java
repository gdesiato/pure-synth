package com.desiato.puresynth.models;

import com.desiato.puresynth.dtos.Token;

public record AuthenticatedUser(User user, Token token) {}
