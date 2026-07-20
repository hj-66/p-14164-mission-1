package com.back;

import com.back.domain.member.member.entity.Member;
import com.back.domain.member.member.service.MemberService;
import com.back.domain.wiseSaying.wiseSaying.entity.WiseSaying;
import com.back.domain.wiseSaying.wiseSaying.service.WiseSayingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class BackApplicationTests {
    @Autowired
    private MemberService memberService;

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
