package com.back.domain.wiseSaying.wiseSaying.repository;

import com.back.domain.wiseSaying.wiseSaying.entity.WiseSaying;

import java.util.List;
import java.util.Optional;

interface WiseSayingRepositoryCustom {
    Optional<WiseSaying> findQById(int id);

    List<WiseSaying> findQAll();

    Long qCount();
}