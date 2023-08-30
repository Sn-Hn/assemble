package com.assemble.joinrequest.service;

import com.assemble.commons.base.UserContext;
import com.assemble.commons.exception.NotFoundException;
import com.assemble.event.publish.JoinRequestEvent;
import com.assemble.joinrequest.dto.request.JoinRequestAnswer;
import com.assemble.joinrequest.dto.request.JoinRequestDto;
import com.assemble.joinrequest.entity.JoinRequest;
import com.assemble.joinrequest.repository.JoinRequestRepository;
import com.assemble.post.entity.Post;
import com.assemble.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;
    private final PostRepository postRepository;
    private final UserContext userContext;
    private final ApplicationEventPublisher eventPublisher;

    // TODO: 2023-08-29 가입신청 시 이미 가입된 사람들(모임 생성자) 처리 -신한
    @Transactional
    public JoinRequest requestJoinToAssemble(JoinRequestDto joinRequestDto) {
        joinRequestRepository.findByAssembleIdAndUserId(joinRequestDto.getPostId(), userContext.getUserId())
                .ifPresent(joinRequest -> {
                    joinRequest.validateAlreadyJoinRequest();
                    joinRequest.validateAnswerStatusOfJoinRequest();
                });

        JoinRequest joinRequest = joinRequestDto.toEntity(userContext.getUserId());

        return joinRequestRepository.save(joinRequest);
    }

    @Transactional
    public JoinRequest responseJoinFromAssemble(JoinRequestAnswer joinRequestAnswer) {
        JoinRequest joinRequest = joinRequestRepository.findById(joinRequestAnswer.getJoinRequestId())
                .orElseThrow(() -> new NotFoundException(JoinRequest.class, joinRequestAnswer.getJoinRequestId()));

        joinRequest.answerJoinRequest(joinRequestAnswer, userContext.getUserId());
        if (joinRequest.isApproval()) {
            eventPublisher.publishEvent(new JoinRequestEvent(joinRequest));
        }

        return joinRequest;
    }

    @Transactional
    public boolean cancelJoinOfAssemble(Long postId) {
        JoinRequest joinRequest = joinRequestRepository.findByAssembleIdAndUserId(postId, userContext.getUserId())
                .orElseThrow(() -> new NotFoundException(JoinRequest.class, postId, userContext.getUserId()));

        joinRequest.cancelJoinRequest(userContext.getUserId());

        return true;
    }

    @Transactional(readOnly = true)
    public Page<JoinRequest> getJoinRequests(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class, postId));

        if (!post.getUser().getUserId().equals(userContext.getUserId())) {
            throw new IllegalArgumentException("모임장이 아닙니다.");
        }

        long count = joinRequestRepository.countByPostId(postId);
        return new PageImpl<>(joinRequestRepository.findAllByPostId(postId, pageable), pageable, count);
    }
}
