package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.DataNotFoundException;
import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;


    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {

        if (member.hasConnectedInstaMember() == false) {
            return RsData.of("F-2", "먼저 인스타그램 아이디를 등록해주세요.");
        }

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }

        List<LikeablePerson> membersLikeable = member.getInstaMember().getMyLikeableList();

        for (LikeablePerson likeable : membersLikeable) {
            if (Objects.equals(likeable.getToInstaMemberUsername(), username)) {
                if (Objects.equals(likeable.getAttractiveTypeCode(), attractiveTypeCode)) {
                    return RsData.of("F-1", "이미 등록된 호감표시입니다.");
                }
                likeable.setAttractiveTypeCode(attractiveTypeCode);
                return RsData.of("S-2", "호감 사유를 변경했습니다.");
            }
        }
        InstaMember fromInstaMember = member.getInstaMember();
        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        if (member.getInstaMember().getMyLikeableList().size()>= 10) {
            return RsData.of("F-1", "호감 표시할 수 있는 최대 횟수를 넘었습니다.");
        }

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(fromInstaMember) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(member.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장

        fromInstaMember.addMyLikeableList(likeablePerson);
        toInstaMember.addWhoLikesMe(likeablePerson);

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다.".formatted(username), likeablePerson);
    }

    public List<LikeablePerson> findByFromInstaMemberId(Long id) {
        return likeablePersonRepository.findByFromInstaMemberId(id);
    }

    public LikeablePerson getLikeablePerson(int id) {
        Optional<LikeablePerson> oLikeablePerson = this.likeablePersonRepository.findById(id);
        if (oLikeablePerson.isPresent()) {
            return oLikeablePerson.get();
        } else {
            throw new DataNotFoundException("likeablPerson Not Found");
        }
    }

    @Transactional
    public RsData cancel(LikeablePerson likeablePerson) {
        this.likeablePersonRepository.delete(likeablePerson);
        return RsData.of("S-1", "%s님에 대한 호감을 취소했습니다.".formatted(likeablePerson.getToInstaMemberUsername()));
    }

    public RsData canUDelete(Member member, LikeablePerson likeablePerson) {
        // 호감 대상의 인스타 아이디 값이 없을 경우의 예외처리
        if (likeablePerson == null) {
            return RsData.of("F-1", "이미 삭제되었습니다.");
        }

        //항목에 대한 소유권이 본인(로그인한 사람)에게 있는지 체크
        if (!(member.getInstaMember().getId()).equals(likeablePerson.getFromInstaMember().getId())) {
            return RsData.of("F" +
                    "-1", "권한이 없습니다.");
        }

        return RsData.of("S-1", "삭제할 수 있습니다.");
    }
}
