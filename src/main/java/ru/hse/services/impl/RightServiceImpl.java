package ru.hse.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hse.models.Right;
import ru.hse.models.Role;
import ru.hse.repositories.RightRepository;
import ru.hse.services.RightService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RightServiceImpl implements RightService {
    private final RightRepository rightRepository;
    @Override
    public void updateRight(UUID curUserId, UUID secretId, UUID userId, Role role) {
        Right right = rightRepository.findByUserIdAndSecretId(curUserId, secretId);
        if (right != null && right.getRole() == Role.ADMIN) {
            rightRepository.save(new Right(userId, secretId, role));
        } else {
            throw new IllegalArgumentException("Have no enough rights to add new right");
        }
    }
}
