package com.happycamper.backend.member.repository.userProfile;

import com.happycamper.backend.member.entity.Member;
import com.happycamper.backend.member.entity.userProfile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query("select up FROM UserProfile up join fetch up.member m where m = :member")
    Optional<UserProfile> findUserProfileByMember(Member member);
    Optional<UserProfile> findByNickName(String nickName);

    @Transactional
    void deleteByMemberId(Long id);
}
