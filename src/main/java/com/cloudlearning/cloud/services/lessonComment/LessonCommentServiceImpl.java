package com.cloudlearning.cloud.services.lessonComment;

import com.cloudlearning.cloud.configuration.utils.authentication.AuthenticationFacade;
import com.cloudlearning.cloud.global.exception.entity.EntityNotExistException;
import com.cloudlearning.cloud.models.LessonChapter;
import com.cloudlearning.cloud.models.LessonComment;
import com.cloudlearning.cloud.models.members.Member;
import com.cloudlearning.cloud.repositories.LessonChapterRepository;
import com.cloudlearning.cloud.repositories.LessonCommentRepository;
import com.cloudlearning.cloud.repositories.members.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class LessonCommentServiceImpl implements LessonCommentService {

    @Autowired
    LessonCommentRepository lessonCommentRepository;

    @Autowired
    LessonChapterRepository lessonChapterRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Override
    public Boolean isLessonCommentOwner(Long id, String username) {
        LessonComment lessonComment = lessonCommentRepository.findById(id).orElseThrow(() -> new EntityNotExistException("api.lesson.comment.notFound"));
        return lessonComment.getMember().getUser().getUsername().equals(username);
    }

    @Override
    public Page<LessonComment> findAllLessonCommentsForLessonChapter(Long lessonChapterId, Pageable pageable) {
        return lessonCommentRepository.findAllByLessonChapterId(lessonChapterId, pageable);
    }

    @Override
    public Page<LessonComment> findAllLessonCommentsReplays(Long lessonCommentId, Pageable pageable) {
        return lessonCommentRepository.findAllByLessonCommentId(lessonCommentId, pageable);
    }

    @Override
    @PreAuthorize("#lessonComment.id == null or @lessonCommentServiceImpl.isLessonCommentOwner(#lessonComment.id, authentication.name)")
    public LessonComment replayLessonComment(LessonComment lessonComment) {
        LessonComment parentLessonComment = lessonCommentRepository.findById(lessonComment.getLessonComment().getId()).orElseThrow(()-> new EntityNotExistException("api.error.lesson.comment.replay.notFound"));
        Member member = memberRepository.findByUserUsername(authenticationFacade.getAuthentication().getName()).orElseThrow(()-> new EntityNotExistException("api.error.member.user.notFound"));

        lessonComment.setLessonComment(parentLessonComment);
        lessonComment.setMember(member);

        return lessonCommentRepository.save(lessonComment);
    }

    @Override
    @PreAuthorize("#lessonComment.id == null or @lessonCommentServiceImpl.isLessonCommentOwner(#lessonComment.id, authentication.name)")
    public LessonComment postLessonComment(LessonComment lessonComment) {
        LessonChapter lessonChapter = lessonChapterRepository.findById(lessonComment.getLessonChapter().getId()).orElseThrow( ()-> new EntityNotExistException("api.error.lessonChapter.notFound"));
        Member member = memberRepository.findByUserUsername(authenticationFacade.getAuthentication().getName()).orElseThrow(()-> new EntityNotExistException("api.error.member.user.notFound"));

        lessonComment.setLessonChapter(lessonChapter);
        lessonComment.setMember(member);
        return lessonCommentRepository.save(lessonComment);
    }

    @Override
    @PreAuthorize("@lessonCommentServiceImpl.isLessonCommentOwner(#id, authentication.name)")
    public void delete(Long id) {
        try {
            lessonCommentRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new EntityNotExistException("api.error.lessonComment.notFound");
        }
    }
}
