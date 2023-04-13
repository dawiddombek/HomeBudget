package aplication.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import aplication.model.classes.UserInfo;
import aplication.model.repositories.UserInfoRepository;
import aplication.service.UserPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UserPageController {

    private final UserInfoRepository userInfoRepository;
    private final UserPageService userPageService;

    @Autowired
    public UserPageController(UserInfoRepository userInfoRepository, UserPageService userPageService) {
        this.userInfoRepository = userInfoRepository;
        this.userPageService = userPageService;
    }

    @GetMapping
    public String getIndex(Model model) {
        return "redirect:/userPage";
    }

    @GetMapping("loginUser")
    public String getLoginUser(Model model) {
        userPageService.updateUserView(SecurityContextHolder.getContext().getAuthentication().getName());
        return "redirect:/userPage";
    }

    @GetMapping("userPage")
    public String getUserPage(Model model) {
        UserInfo userInfo = userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userInfo.getAccount().getUserView().equals("")) {
            model.addAttribute("userInfo", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
            model.addAttribute("chartIncomeData", userPageService.getChartData(userInfoRepository.getUserInfoByUsername(userInfo.getUsername()).getAccount().getOperations(), "Income"));
            model.addAttribute("chartExpenseData", userPageService.getChartData(userInfoRepository.getUserInfoByUsername(userInfo.getUsername()).getAccount().getOperations(), "Expense"));
            model.addAttribute("operationsList", userPageService.sortOperations(userInfoRepository.getUserInfoByUsername(userInfo.getUsername()).getAccount().getOperations()));
        }
        else {
            model.addAttribute("userInfo", userInfoRepository.getUserInfoByUsername(userInfo.getAccount().getUserView()));
            model.addAttribute("chartIncomeData", userPageService.getChartData(userInfoRepository.getUserInfoByUsername(userInfo.getAccount().getUserView()).getAccount().getOperations(), "Income"));
            model.addAttribute("chartExpenseData", userPageService.getChartData(userInfoRepository.getUserInfoByUsername(userInfo.getAccount().getUserView()).getAccount().getOperations(), "Expense"));
            model.addAttribute("operationsList", userPageService.sortOperations(userInfoRepository.getUserInfoByUsername(userInfo.getAccount().getUserView()).getAccount().getOperations()));
        }
        return "userPage";
    }

    @GetMapping("/regularOperation")
    public String getRegularOperation(Model model) {
        model.addAttribute("userInfo", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "regularOperation";
    }

    @PostMapping("/addRegularOperation")
    public String postAddRegularOperation(Model model, @RequestParam("label") String label, @RequestParam("category") String category, @RequestParam("description") String description, @RequestParam("amount") double amount) {
        UserInfo userInfo = userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userInfo.getAccount().getUserView().equals("")) {
            userPageService.saveNewRegularOperation(userInfo.getUsername(), label, category, description, amount);
        }
        else {
            userPageService.saveNewRegularOperation(userInfo.getAccount().getUserView(), label, category, description, amount);
        }

        return "redirect:/userPage";
    }

    @GetMapping("/operationBetweenAccounts")
    public String getOperationBetweenAccounts() {
        return "operationBetweenAccounts";
    }

    @PostMapping("/addOperationBetweenAccounts")
    public String postAddOperationBetweenAccounts(Model model, @RequestParam("label") String label, @RequestParam("category") String category, @RequestParam("description") String description, @RequestParam("amount") double amount, @RequestParam("anotherName") String anotherName) {
        UserInfo userInfo = userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userInfo.getAccount().getUserView().equals("")) {
            userPageService.saveNewOperationBetweenAccounts(userInfo.getUsername(), label, category, description, amount, anotherName);
        }
        else {
            userPageService.saveNewOperationBetweenAccounts(userInfo.getAccount().getUserView(), label, category, description, amount, anotherName);
        }

        return "redirect:/userPage";
    }

    @GetMapping("/cyclicOperation")
    public String getCyclicOperation() {
        return "cyclicOperation";
    }

    @PostMapping("/addCyclicOperation")
    public String postAddCyclicOperation(Model model, @RequestParam("label") String label, @RequestParam("category") String category, @RequestParam("description") String description, @RequestParam("amount") double amount, @RequestParam("period") int period) {
        UserInfo userInfo = userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userInfo.getAccount().getUserView().equals("")) {
            userPageService.saveNewCyclicOperation(userInfo.getUsername(), label, category, description, amount, period);
        }
        else {
            userPageService.saveNewCyclicOperation(userInfo.getAccount().getUserView(), label, category, description, amount, period);
        }

        return "redirect:/userPage";
    }

    @GetMapping("/deleteOperation")
    public String getDeleteOperation(Model model, @RequestParam("operationId") Long id) {
        userPageService.deleteOperation(id);
        return "redirect:/userPage";
    }

    @GetMapping("/updateCyclicOperations")
    public String getUpdateCyclicOperations(Model model) {
        UserInfo userInfo = userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(userInfo.getAccount().getUserView().equals("")) {
            userPageService.checkIfOperationsNeedsUpdate(userInfoRepository.getUserInfoByUsername(userInfo.getUsername()));
        }
        else {
            userPageService.checkIfOperationsNeedsUpdate(userInfoRepository.getUserInfoByUsername(userInfo.getAccount().getUserView()));
        }

        return "redirect:/userPage";
    }
}
