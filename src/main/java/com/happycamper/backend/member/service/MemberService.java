package com.happycamper.backend.member.service;

import com.happycamper.backend.member.service.request.NormalMemberRegisterRequest;

public interface MemberService {

    Boolean normalMemberRegister(NormalMemberRegisterRequest request);

}
