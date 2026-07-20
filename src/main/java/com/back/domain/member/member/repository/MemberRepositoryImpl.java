package com.back.domain.member.member.repository;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.entity.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import java.util.List;
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

    @Override
    public Page<Member> findQPaged(String kwType, String kw, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (kw != null && !kw.isBlank()) {
            if ("username".equals(kwType)) {
                builder.and(member.username.contains(kw));
            } else if ("nickname".equals(kwType)) {
                builder.and(member.nickname.contains(kw));
            } else if ("all".equals(kwType)) {
                builder.and(
                        member.username.contains(kw)
                                .or(member.nickname.contains(kw))
                );
            }
        }

        JPAQuery<Member> query = queryFactory
                .selectFrom(member)
                .where(builder);

        if (pageable.getSort().isUnsorted()) {
            query.orderBy(member.id.asc());
        } else {
            pageable.getSort().forEach(order -> {
                boolean isAsc = order.isAscending();

                switch (order.getProperty()) {
                    case "id" -> query.orderBy(isAsc ? member.id.asc() : member.id.desc());
                    case "username" -> query.orderBy(isAsc ? member.username.asc() : member.username.desc());
                    case "nickname" -> query.orderBy(isAsc ? member.nickname.asc() : member.nickname.desc());
                }
            });
        }

        List<Member> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(member.count())
                .from(member)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }
}
