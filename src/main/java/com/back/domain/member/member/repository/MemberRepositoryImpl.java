package com.back.domain.member.member.repository;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QMember member = QMember.member;

    @Override
    public Optional<Member> findQByUsername(String username) {
        return Optional.ofNullable(queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq(username))
                .fetchOne());
    }

    @Override
    public Long qCount() {
        return queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();
    }
}