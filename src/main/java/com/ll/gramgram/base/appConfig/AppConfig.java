package com.ll.gramgram.base.appConfig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class AppConfig {
    @Getter
    private static long likeablePersonFromMax;

    @Value("${custom.likeablePerson.from.max}")
    public void setLikeablePersonFromMax(long likeablePersonFromMax) {
        AppConfig.likeablePersonFromMax = likeablePersonFromMax;
    }

    @Getter
    private static long likeablePersonCoolTime;

    @Value("${custom.likeablePerson.coolTime}")
    public void setLikeablePersonCoolTime(long likeablePersonCoolTime) {
        AppConfig.likeablePersonCoolTime = likeablePersonCoolTime;
    }

    public static LocalDateTime genLikeablePersonUnlockCoolTime() {
        return LocalDateTime.now().plusSeconds(likeablePersonCoolTime); //  현재 시간 + 쿨타임으로 정한 시간
    }

}