package com.assemble.user.controller;

import com.assemble.commons.response.ApiResult;
import com.assemble.user.dto.request.EmailRequest;
import com.assemble.user.dto.request.NicknameRequest;
import com.assemble.user.dto.request.ValidationUserRequest;
import com.assemble.user.service.ValidationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "검증 Apis")
@RequiredArgsConstructor
@RestController
public class ValidationController {

    private final ValidationService validationService;

    @ApiOperation(value = "이메일 검증")
    @GetMapping("email/validation")
    public ApiResult<Boolean> validateUserEmail(@Valid EmailRequest emailRequest) {
        return ApiResult.ok(validationService.isDuplicationEmail(emailRequest.getEmail()));
    }

    @ApiOperation(value = "닉네임 검증")
    @GetMapping("nickname/validation")
    public ApiResult<Boolean> validateUserEmail(@Valid NicknameRequest nicknameRequest) {
        return ApiResult.ok(validationService.isDuplicationNickname(nicknameRequest.getNickname()));
    }

    // TODO: 2023-08-28 3차 개발 기간에 Email 검증 추가 예정 -신한
    @ApiOperation(value = "비밀번호 변경 전 계정 확인 (추후 이메일 검증 추가)")
    @GetMapping("user/validation")
    public ApiResult<Boolean> validateUser(@Valid ValidationUserRequest validationUserRequest) {
        return ApiResult.ok(validationService.checkUser(validationUserRequest));
    }

}
