package com.assemble.user.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.user.dto.request.EmailRequest;
import com.assemble.user.dto.request.NicknameRequest;
import com.assemble.user.service.VerificationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class VerificationController {

    private final VerificationService verificationService;

    @ApiOperation(value = "이메일 검증")
    @GetMapping("email/validation")
    public ApiResult<Boolean> validateUserEmail(EmailRequest emailRequest) {
        return ApiResult.ok(verificationService.isDuplicationEmail(emailRequest.getEmail()));
    }

    @ApiOperation(value = "닉네임 검증")
    @GetMapping("nickname/validation")
    public ApiResult<Boolean> validateUserEmail(NicknameRequest nicknameRequest) {
        return ApiResult.ok(verificationService.isDuplicationNickname(nicknameRequest.getNickname()));
    }

}
