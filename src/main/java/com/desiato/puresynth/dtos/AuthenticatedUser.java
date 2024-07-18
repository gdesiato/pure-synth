package com.desiato.puresynth.dtos;

import com.desiato.puresynth.dtos.PureSynthToken;
import com.desiato.puresynth.models.User;

public record AuthenticatedUser(User user, PureSynthToken pureSynthToken) {}
