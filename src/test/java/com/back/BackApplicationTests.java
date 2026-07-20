package com.back;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.repository.MemberRepository;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.wiseSaying.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.wiseSaying.service.WiseSayingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BackApplicationTests {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private WiseSayingService wiseSayingService;

    @Test
    void contextLoads() {
    }

    @Test
    void memberQueryDslMethodsWorkThroughService() {
        long beforeCount = memberService.count();

        Member member = memberService.join("querydsl-user", "1234", "Querydsl User");

        Optional<Member> foundMember = memberService.findByUsername("querydsl-user");
        Optional<Member> missingMember = memberService.findByUsername("missing-querydsl-user");

        assertEquals(beforeCount + 1, memberService.count());
        assertTrue(foundMember.isPresent());
        assertEquals(member.getId(), foundMember.get().getId());
        assertFalse(missingMember.isPresent());
    }

    @Test
    void memberFindQPagedWorksWithKeywordTypeAndPageableSort() {
        Page<Member> allKeywordOne = memberRepository.findQPaged(
                "all",
                "1",
                PageRequest.of(0, 10, Sort.by("id").ascending())
        );

        Page<Member> usernameKoreanUser = memberRepository.findQPaged(
                "username",
                "유저",
                PageRequest.of(0, 10, Sort.by("username").ascending())
        );

        Page<Member> nicknameKoreanUser = memberRepository.findQPaged(
                "nickname",
                "유저",
                PageRequest.of(0, 10, Sort.by("nickname").ascending())
        );

        Page<Member> usernameUser = memberRepository.findQPaged(
                "username",
                "user",
                PageRequest.of(0, 10, Sort.by("username").ascending())
        );

        Page<Member> nicknameUser = memberRepository.findQPaged(
                "nickname",
                "user",
                PageRequest.of(0, 10, Sort.by("nickname").ascending())
        );

        Page<Member> usernameUserPaged = memberRepository.findQPaged(
                "username",
                "user",
                PageRequest.of(0, 2, Sort.by("username").ascending())
        );

        assertEquals(1, allKeywordOne.getContent().size());
        assertEquals(0, usernameKoreanUser.getContent().size());
        assertEquals(3, nicknameKoreanUser.getContent().size());
        assertEquals(3, usernameUser.getContent().size());
        assertEquals(0, nicknameUser.getContent().size());
        assertEquals(2, usernameUserPaged.getContent().size());

        assertEquals(3, usernameUserPaged.getTotalElements());
        assertEquals("user1", usernameUserPaged.getContent().get(0).getUsername());
        assertEquals("user2", usernameUserPaged.getContent().get(1).getUsername());
    }

    @Test
    void wiseSayingQueryDslMethodsWorkThroughService() {
        long beforeCount = wiseSayingService.count();

        WiseSaying wiseSaying = wiseSayingService.write("Querydsl content", "Querydsl author");

        Optional<WiseSaying> foundWiseSaying = wiseSayingService.findById(wiseSaying.getId());
        Optional<WiseSaying> missingWiseSaying = wiseSayingService.findById(999_999);
        List<WiseSaying> wiseSayings = wiseSayingService.findAll();

        assertEquals(beforeCount + 1, wiseSayingService.count());
        assertTrue(foundWiseSaying.isPresent());
        assertEquals("Querydsl content", foundWiseSaying.get().getContent());
        assertTrue(wiseSayings.stream().anyMatch(it -> it.getId() == wiseSaying.getId()));
        assertFalse(missingWiseSaying.isPresent());
    }
}
