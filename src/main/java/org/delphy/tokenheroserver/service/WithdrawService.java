package org.delphy.tokenheroserver.service;

import lombok.extern.slf4j.Slf4j;
import org.delphy.tokenheroserver.common.Constant;
import org.delphy.tokenheroserver.common.EnumError;
import org.delphy.tokenheroserver.entity.User;
import org.delphy.tokenheroserver.entity.Withdraw;
import org.delphy.tokenheroserver.pojo.UserVo;
import org.delphy.tokenheroserver.pojo.WithdrawVo;
import org.delphy.tokenheroserver.repository.IUserRepository;
import org.delphy.tokenheroserver.repository.IWithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mutouji
 */
@Slf4j
@Service
public class WithdrawService {
    private IUserRepository userRepository;
    private IWithdrawRepository withdrawRepository;

    public WithdrawService(@Autowired IUserRepository userRepository,
                           @Autowired IWithdrawRepository withdrawRepository) {
        this.userRepository = userRepository;
        this.withdrawRepository = withdrawRepository;
    }

    public EnumError withdraw(UserVo userVo, WithdrawVo withdrawVo) {
        User user = userRepository.findByPhone(userVo.getPhone());
        if (user == null) {
            return EnumError.USER_ERROR;
        }
        if (user.getDpy() < Constant.WITHDRAW_MIN) {
            return EnumError.WITHDRAW_LT_MIN;
        }
        if (user.getDpy() < withdrawVo.getAmount()) {
            return EnumError.INSUFFICIENT_DPY;
        }
        if (withdrawVo.getAddress().trim().length() == 0) {
            return EnumError.BADADRESS;
        }
        Withdraw withdraw = new Withdraw(user, withdrawVo);
        user.setDpy(user.getDpy() - withdrawVo.getAmount());
        withdrawRepository.insert(withdraw);
        userRepository.save(user);
        return EnumError.SUCCESS;
    }

    public List<Withdraw> getUserWithdraw(UserVo userVo, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return withdrawRepository.findAllByUserIdOrderByIdDesc(userVo.getId(), pageable);
    }

    public Long getUserWithdrawCount(UserVo userVo) {
        return withdrawRepository.countByUserId(userVo.getId());
    }
}
