package aplication.service;

import aplication.model.classes.Account;
import aplication.model.classes.Operation;
import aplication.model.classes.UserInfo;
import aplication.model.repositories.AccountRepository;
import aplication.model.repositories.OperationRepository;
import aplication.model.repositories.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserPageService {

    private final OperationRepository operationRepository;

    private final UserInfoRepository userInfoRepository;

    private final AccountRepository accountRepository;

    @Autowired
    public UserPageService(OperationRepository operationRepository, UserInfoRepository userInfoRepository, AccountRepository accountRepository) {
        this.operationRepository = operationRepository;
        this.userInfoRepository = userInfoRepository;
        this.accountRepository = accountRepository;
    }

    public void updateUserView(String username) {
        UserInfo userInfo = userInfoRepository.getUserInfoByUsername(username);
        userInfo.getAccount().setUserView(userInfo.getUsername());
        userInfoRepository.save(userInfo);
    }

    public void saveNewRegularOperation(String username, String label, String category, String description, double amount) {
        Calendar creationDate = new GregorianCalendar();
        Operation operation = new Operation(userInfoRepository.getUserInfoByUsername(username).getAccount(), creationDate, label, category, description, amount);

        operationRepository.save(operation);

        Account account = operation.getAccount();
        account.calculateCurrentAmount(amount);

        accountRepository.save(account);
    }

    public void saveNewOperationBetweenAccounts(String username, String label, String category, String description, double amount, String relatedUsername) {
        Calendar creationDate = new GregorianCalendar();
        Operation operation = new Operation(userInfoRepository.getUserInfoByUsername(username).getAccount(), creationDate, label, category, description, -amount);
        Operation relatedOperation = new Operation(userInfoRepository.getUserInfoByUsername(relatedUsername).getAccount(), creationDate, label, category, description, amount);

        operationRepository.save(operation);
        operationRepository.save(relatedOperation);

        operation.setOperation(relatedOperation);
        relatedOperation.setOperation(operation);

        operationRepository.save(operation);
        operationRepository.save(relatedOperation);

        Account account = operation.getAccount();
        Account relatedAccount = relatedOperation.getAccount();
        account.calculateCurrentAmount(-amount);
        relatedAccount.calculateCurrentAmount(amount);

        accountRepository.save(account);
        accountRepository.save(relatedAccount);
    }

    public void saveNewCyclicOperation(String username, String label, String category, String description, double amount, int period) {
        Calendar creationDate = new GregorianCalendar();
        Calendar nextUpdateDate = new GregorianCalendar();
        Operation operation;
        if(period < 0) {
            nextUpdateDate.add(Calendar.DATE, -period);
            operation = new Operation(userInfoRepository.getUserInfoByUsername(username).getAccount(), creationDate, nextUpdateDate, label, category, description, amount, -period);
        }
        else if (period == 0) {
            operation = new Operation(userInfoRepository.getUserInfoByUsername(username).getAccount(), creationDate, label, category, description, amount);
        }
        else {
            nextUpdateDate.add(Calendar.DATE, period);
            operation = new Operation(userInfoRepository.getUserInfoByUsername(username).getAccount(), creationDate, nextUpdateDate, label, category, description, amount, period);
        }


        operationRepository.save(operation);

        Account account = operation.getAccount();
        account.calculateCurrentAmount(amount);

        accountRepository.save(account);
    }

    public void deleteOperation(Long id) {
        Operation operation = operationRepository.getOperationById(id);
        if(operation.getOperation() != null) {
            Operation relatedOperation = operationRepository.getOperationById(operation.getOperation().getId());
            relatedOperation.getAccount().calculateCurrentAmount(-relatedOperation.getAmount());
            operationRepository.delete(relatedOperation);
        }
        for(int i = 0; i < operation.getPeriodCount() + 1; i++) {
            operation.getAccount().calculateCurrentAmount(-operation.getAmount());
        }
        operationRepository.delete(operation);
    }

    public void checkIfOperationsNeedsUpdate(UserInfo userInfo) {
        Calendar now = new GregorianCalendar();
        for(Operation operation : userInfo.getAccount().getOperations()) {
            if(operation.getPeriod() != 0 && now.compareTo(operation.getNextUpdateDate()) > 0) {
                userInfo.getAccount().calculateCurrentAmount(operation.getAmount());
                operation.getNextUpdateDate().add(Calendar.DATE, operation.getPeriod());
                operation.incrementPeriodCount();
                userInfoRepository.save(userInfo);
                operationRepository.save(operation);
            }
        }
    }

    public List<List<Object>> getChartData(Set<Operation> operations, String sign) {
        List<List<Object>> listOfLists = new ArrayList<>();
        List<String> categoryList = new LinkedList<>();
        double amount = 0;

        if(sign.equals("Income")) {
            for(Operation addToCategoryList : operations) {
                if(addToCategoryList.getAmount() >= 0 && !categoryList.contains(addToCategoryList.getCategory())) {
                    categoryList.add(addToCategoryList.getCategory());
                }
            }

            for(String category : categoryList) {
                for(Operation operation : operations) {
                    if(operation.getCategory().equals(category) && operation.getAmount() >= 0) {
                        amount = amount + operation.getAmount()*(operation.getPeriodCount() + 1);
                    }
                }
                listOfLists.add(List.of(category, amount));
                amount = 0;
            }
        }
        if(sign.equals("Expense")) {
            for(Operation addToCategoryList : operations) {
                if(addToCategoryList.getAmount() < 0 && !categoryList.contains(addToCategoryList.getCategory())) {
                    categoryList.add(addToCategoryList.getCategory());
                }
            }

            for(String category : categoryList) {
                for(Operation operation : operations) {
                    if(operation.getCategory().equals(category) && operation.getAmount() < 0) {
                        amount = amount + -operation.getAmount()*(operation.getPeriodCount() + 1);
                    }
                }
                listOfLists.add(List.of(category, amount));
                amount = 0;
            }
        }
        if(categoryList.isEmpty()) {
            return List.of(List.of("Brak operacji", 1));
        }
        return listOfLists;

    }

    public List<Operation> sortOperations(Set<Operation> operations) {
        List<Operation> operationsList = new ArrayList<>(operations);

        Collections.sort(operationsList);

        return operationsList;
    }
}
