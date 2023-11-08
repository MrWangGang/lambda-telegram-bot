package com.lambda.telegram.bot.mvc.service.impl;

import com.lambda.telegram.bot.mvc.controller.response.ViewPanel;
import com.lambda.telegram.bot.mvc.repository.AccountHisRepository;
import com.lambda.telegram.bot.mvc.repository.AccountRepository;
import com.lambda.telegram.bot.mvc.repository.RateRepository;
import com.lambda.telegram.bot.mvc.repository.po.AccountHisPO;
import com.lambda.telegram.bot.mvc.repository.po.AccountPO;
import com.lambda.telegram.bot.mvc.repository.po.RatePO;
import com.lambda.telegram.bot.mvc.service.IBotTestAccountService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BotTestAccountServicImpl implements IBotTestAccountService {

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private AccountHisRepository accountHisRepository;

    @Resource
    private RateRepository rateRepository;

    private String S =
 """
请输入 #1或#2 记账选择模式
1.普通模式
2.回U模式
""";
    private String O =
"""
**设置** - 选择记账模式
**+金额** - 入账
**-金额** - 出账
**入款-金额** - 下发纠错
**入款+金额** - 下发纠错
""";

    public Mono<String> auxse(String username){
        AccountPO _account = AccountPO.builder().username(username).build();
        Example<AccountPO> exampleAc = Example.of(_account);
       return accountRepository.findOne(exampleAc).flatMap(e->{
            return Mono.just(ViewPanel.builder()
                    .date(now(e.getCreateTime()))
                    .model(e.getModel())
                    .build()
            );
        }).flatMap(e->{
            AccountHisPO _accountHis = AccountHisPO.builder().username(username).model(e.getModel()).build();
            Example<AccountHisPO> exampleHis = Example.of(_accountHis);
            Mono<List<AccountHisPO>> monoList = accountHisRepository.findAll(exampleHis).collect(Collectors.toList());
            return monoList.flatMap(x->{
                Double ru = 0.00;
                Double ying = 0.00;
                Double yi = 0.00;
                Double wei = 0.00;
                for(AccountHisPO accountHisPO : x){
                    if("+".equals(accountHisPO.getOperate())){
                        ru = ru + accountHisPO.getAmount();
                        ying = ying + accountHisPO.getAmount();
                    }

                    if("-".equals(accountHisPO.getOperate())){
                        yi = yi + accountHisPO.getAmount();
                    }

                    wei = ying-yi;
                }
                e.setRuAmount(ru.toString());
                e.setYingAmount(ying.toString());
                e.setYiAmount(yi.toString());
                e.setWeiAmount(wei.toString());
                e.setShouAmount(String.valueOf(0.00));
                return Mono.just(e);
            });
        }).flatMap(e->{
            e.setDistribute(e.getYiAmount());
            e.setIncome(e.getRuAmount());
            RatePO _rate = RatePO.builder().username(username).model(e.getModel()).build();
            Example<RatePO> exampleRate = Example.of(_rate);
           RatePO initRate = RatePO.builder().rate(0.00).build();
            return rateRepository.findOne(exampleRate).switchIfEmpty(Mono.just(initRate)).flatMap(x->{
               e.setRate(x.getRate().toString());
               return Mono.just(e);
            });
        }).switchIfEmpty(Mono.just(ViewPanel.builder().build())).flatMap(e->{
            String N =
                    """
            *模式：* """+e.getModel()+"""
            
            --------------------------
            
            *日期：* """ + e.getDate() + """
            
            - **入款：** """+e.getIncome()+"""
            
            
            - **下发：** """+e.getDistribute()+"""

            --------------------------
            **当前费率：** """+e.getRate()+"""  
            %
            **入款金额：** """+e.getRuAmount()+""" 
            元
            **应下金额：** """+e.getYingAmount()+""" 
            元
            **已下金额：** """+e.getYiAmount()+""" 
            元
            **未下金额：** """+e.getWeiAmount()+""" 
            元
            **手续费用：** """+e.getShouAmount()+""" 
            元
            """;
            return Mono.just(N);
        });
    }

    public String now(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = localDateTime.format(formatter);
        return formattedDate;
    }

    public Mono<String> processAccount(String username, String msg) {
        return Mono.just(msg).flatMap(message->{
            if(message.equals("账户命令help")){
                return Mono.just(O);
            }
            if(message.equals("设置")){
                return Mono.just(S);
            }
            if(message.equals("#1")){
                AccountPO _account = AccountPO.builder().username(username).build();
                Example<AccountPO> example = Example.of(_account);
                AccountPO initAccountPO = AccountPO.builder().username(username).amount(0.00).model("普通模式").createTime(LocalDateTime.now()).build();
                return accountRepository.findOne(example).switchIfEmpty(Mono.just(initAccountPO)).flatMap(e->{
                    e.setModel("普通模式");
                    return accountRepository.save(e);
                }).flatMap(e->{
                    return auxse(username);
                });
            }
            if(message.equals("#2")){
                AccountPO _account = AccountPO.builder().username(username).build();
                Example<AccountPO> example = Example.of(_account);
                AccountPO initAccountPO = AccountPO.builder().username(username).amount(0.00).model("回U模式").createTime(LocalDateTime.now()).build();
                return accountRepository.findOne(example).switchIfEmpty(Mono.just(initAccountPO)).flatMap(e->{
                    e.setModel("回U模式");
                    return accountRepository.save(e);
                }).flatMap(e->{
                    return auxse(username);
                });
            }
            if (message.startsWith("+")) {
                // 处理入款
                double amount = Double.parseDouble(message.substring(1));
                AccountPO _account = AccountPO.builder().username(username).build();
                Example<AccountPO> example = Example.of(_account);
                AccountPO initAccountPO = AccountPO.builder().id(-1L).build();
                return accountRepository.findOne(example)
                        .switchIfEmpty(Mono.just(initAccountPO))
                        .flatMap(e -> {
                            if(e.getId() == -1L){
                                return Mono.just("请先设置模式");
                            }
                            e.setAmount(e.getAmount() + amount);

                            AccountHisPO _accountHis = AccountHisPO.builder()
                                    .username(e.getUsername())
                                    .accountId(e.getId())
                                    .operate("+")
                                    .createTime(LocalDateTime.now())
                                    .model(e.getModel())
                                    .amount(amount)
                                    .build();
                            return Mono.when(accountRepository.save(e),accountHisRepository.save(_accountHis)).then(auxse(username));

                        });
            }
            if (message.startsWith("-")) {
                // 处理入款
                double amount = Double.parseDouble(message.substring(1));
                AccountPO _account = AccountPO.builder().username(username).build();
                Example<AccountPO> example = Example.of(_account);
                AccountPO initAccountPO = AccountPO.builder().id(-1L).build();
                return accountRepository.findOne(example)
                        .switchIfEmpty(Mono.just(initAccountPO))
                        .flatMap(e -> {
                            if(e.getId() == -1L){
                                return Mono.just("请先设置模式");
                            }
                            e.setAmount(e.getAmount() - amount);

                            AccountHisPO _accountHis = AccountHisPO.builder()
                                    .username(e.getUsername())
                                    .accountId(e.getId())
                                    .operate("-")
                                    .createTime(LocalDateTime.now())
                                    .model(e.getModel())
                                    .amount(amount)
                                    .build();
                            return Mono.when(accountRepository.save(e),accountHisRepository.save(_accountHis)).then(auxse(username));
                        });
            }
            return Mono.just("无效指令");
        });
    }
}
