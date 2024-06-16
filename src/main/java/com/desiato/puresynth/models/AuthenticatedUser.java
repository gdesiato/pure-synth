package com.desiato.puresynth.models;

import com.desiato.puresynth.dtos.PureSynthToken;

public record AuthenticatedUser(User user, PureSynthToken pureSynthToken) {}
