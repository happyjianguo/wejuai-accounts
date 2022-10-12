package com.wejuai.accounts.service;

import com.wejuai.accounts.repository.FeedbackRepository;
import com.wejuai.accounts.repository.HobbyApplyRepository;
import com.wejuai.accounts.repository.ReportRepository;
import com.wejuai.accounts.repository.UserRepository;
import com.wejuai.accounts.web.dto.request.CreateFeedbackRequest;
import com.wejuai.accounts.web.dto.request.CreateHobbyApplyRequest;
import com.wejuai.accounts.web.dto.request.CreateReportRequest;
import com.wejuai.entity.mongo.Report;
import com.wejuai.entity.mongo.ReportType;
import com.wejuai.entity.mysql.Feedback;
import com.wejuai.entity.mysql.HobbyApply;
import com.wejuai.entity.mysql.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author ZM.Wang
 */
@Service
public class FeedbackService {

    private final UserRepository userRepository;
    private final ReportRepository reportRepository;
    private final FeedbackRepository feedbackRepository;
    private final HobbyApplyRepository hobbyApplyRepository;

    public FeedbackService(UserRepository userRepository, FeedbackRepository feedbackRepository, HobbyApplyRepository hobbyApplyRepository, ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.feedbackRepository = feedbackRepository;
        this.hobbyApplyRepository = hobbyApplyRepository;
        this.reportRepository = reportRepository;
    }

    public void saveFeedback(CreateFeedbackRequest request, String userId) {
        User user = null;
        if (StringUtils.isNotBlank(userId)) {
            user = userRepository.findById(userId).orElse(null);
        }
        feedbackRepository.save(new Feedback(request.getText(), request.getContact()).setUser(user));
    }

    public void saveHobbyApply(CreateHobbyApplyRequest request, String userId) {
        User user = null;
        if (StringUtils.isNotBlank(userId)) {
            user = userRepository.findById(userId).orElse(null);
        }
        hobbyApplyRepository.save(new HobbyApply(request.getText(), request.getContact()).setUser(user));
    }

    public void report(String userId, CreateReportRequest request) {
        if (request.getType() == ReportType.USER) {
            reportRepository.save(new Report(userId, request.getAppId(), request.getAppType().toString(), request.getReason()));
        } else {
            reportRepository.save(new Report(userId, request.getBeUserId(), request.getReason()));

        }
    }

}
