package com.wejuai.accounts.service;

import com.endofmaster.rest.exception.BadRequestException;
import com.wejuai.accounts.repository.CommentRepository;
import com.wejuai.accounts.repository.CommentStarRepository;
import com.wejuai.accounts.repository.SubCommentRepository;
import com.wejuai.accounts.repository.SubCommentStarRepository;
import com.wejuai.entity.mongo.Comment;
import com.wejuai.entity.mongo.CommentStar;
import com.wejuai.entity.mongo.SubComment;
import com.wejuai.entity.mongo.SubCommentStar;
import com.wejuai.entity.mysql.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author ZM.Wang
 */
@Service
public class CommentService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;
    private final CommentStarRepository commentStarRepository;
    private final SubCommentStarRepository subCommentStarRepository;

    public CommentService(CommentRepository commentRepository, CommentStarRepository commentStarRepository, SubCommentRepository subCommentRepository, SubCommentStarRepository subCommentStarRepository) {
        this.commentRepository = commentRepository;
        this.commentStarRepository = commentStarRepository;
        this.subCommentRepository = subCommentRepository;
        this.subCommentStarRepository = subCommentStarRepository;
    }

    public void starComment(User user, String id) {
        Comment comment = getComment(id);
        boolean hasCommentStar = commentStarRepository.existsByCommentIdAndUserId(id, user.getId());
        if (hasCommentStar) {
            throw new BadRequestException("该评论已经赞过了");
        }
        commentStarRepository.save(new CommentStar(user.getId(), id));
        commentRepository.save(comment.setStarNum(comment.getStarNum() + 1));
    }

    public void unStarComment(User user, String id) {
        Comment comment = getComment(id);
        CommentStar commentStar = commentStarRepository.findByCommentIdAndUserId(id, user.getId());
        if (commentStar == null) {
            throw new BadRequestException("该评论还未点赞");
        }
        commentStarRepository.delete(commentStar);
        commentRepository.save(comment.setStarNum(comment.getStarNum() - 1));
    }

    public void starSubComment(User user, String id) {
        SubComment subComment = subCommentRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该二级评论"));
        boolean hasCommentStar = subCommentStarRepository.existsBySubCommentIdAndUserId(id, user.getId());
        if (hasCommentStar) {
            logger.warn("该二级评论已经赞过了");
            return;
        }
        subCommentStarRepository.save(new SubCommentStar(user.getId(), id));
        subCommentRepository.save(subComment.setStarNum(subComment.getStarNum() + 1));
    }

    public void unStarSubComment(User user, String id) {
        SubComment subComment = subCommentRepository.findById(id).orElseThrow(() -> new BadRequestException("没有该二级评论"));
        SubCommentStar subCommentStar = subCommentStarRepository.findBySubCommentIdAndUserId(id, user.getId());
        if (subCommentStar == null) {
            logger.warn("该评论还未点赞");
            return;
        }
        subCommentStarRepository.delete(subCommentStar);
        subCommentRepository.save(subComment.setStarNum(subComment.getStarNum() - 1));
    }

    private Comment getComment(String id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (commentOptional.isEmpty()) {
            throw new BadRequestException("没有该评论");
        }
        return commentOptional.get();
    }
}
