package com.lambda.telegram.bot.mvc.repository;

import com.lambda.telegram.bot.mvc.repository.po.AccountHisPO;
import org.lambda.framework.repository.operation.mysql.ReactiveMySqlCrudRepositoryOperation;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountHisRepository extends ReactiveMySqlCrudRepositoryOperation<AccountHisPO,Long> {

}
