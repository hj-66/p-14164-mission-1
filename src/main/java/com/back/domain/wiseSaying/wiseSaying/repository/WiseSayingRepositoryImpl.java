package com.back.domain.wiseSaying.wiseSaying.repository;

import com.back.domain.wiseSaying.wiseSaying.entity.QWiseSaying;
import com.back.domain.wiseSaying.wiseSaying.entity.WiseSaying;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
class WiseSayingRepositoryImpl implements WiseSayingRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    QWiseSaying wiseSaying = QWiseSaying.wiseSaying;

    @Override
    public Optional<WiseSaying> findQById(int id) {
        return Optional.ofNullable(queryFactory
                .select(wiseSaying)
                .from(wiseSaying)
                .where(wiseSaying.id.eq(id))
                .fetchOne());
    }

    @Override
    public List<WiseSaying> findQAll() {
        return queryFactory
                .select(wiseSaying)
                .from(wiseSaying)
                .fetch();
    }

    @Override
    public Long qCount() {
        return queryFactory
                .select(wiseSaying.count())
                .from(wiseSaying)
                .fetchOne();
    }
}