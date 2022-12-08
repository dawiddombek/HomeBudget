package pl.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.model.Operation;
import pl.model.OperationRepository;
import pl.model.UserInfo;
import pl.model.UserInfoRepository;
import pl.service.UserPageService;
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

    private final OperationRepository operationRepository;
    private final UserPageService userPageService;

    @Autowired
    public UserPageController(UserInfoRepository userInfoRepository, OperationRepository operationRepository, UserPageService userPageService) {
        this.userInfoRepository = userInfoRepository;
        this.operationRepository = operationRepository;
        this.userPageService = userPageService;
    }

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("userInfo", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        return getUserPage(model);
    }

    @GetMapping("loginUser")
    public String getLoginUser(Model model) {
        model.addAttribute("userInfo", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("chartData", userPageService.getIncomeData(userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getAccount().getOperations()));
        return "userPage";
    }

    @GetMapping("userPage")
    public String getUserPage(Model model) {
        model.addAttribute("userInfo", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("chartData", userPageService.getIncomeData(userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getAccount().getOperations()));
        return "userPage";
    }

    @GetMapping("/regularOperation")
    public String getRegularOperation() {
        return "regularOperation";
    }

    @PostMapping("/addRegularOperation")
    public String postAddRegularOperation(Model model, @RequestParam("label") String label, @RequestParam("category") String category, @RequestParam("description") String description, @RequestParam("amount") double amount) {
        model.addAttribute("userInfo", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        userPageService.saveNewRegularOperation(SecurityContextHolder.getContext().getAuthentication().getName(), label, category, description, amount);
        return getUserPage(model);
    }

    @GetMapping("/operationBetweenAccounts")
    public String getOperationBetweenAccounts() {
        return "operationBetweenAccounts";
    }

    @PostMapping("/addOperationBetweenAccounts")
    public String postAddOperationBetweenAccounts(Model model, @RequestParam("label") String label, @RequestParam("category") String category, @RequestParam("description") String description, @RequestParam("amount") double amount, @RequestParam("anotherName") String anotherName) {
        model.addAttribute("userInfo", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        userPageService.saveNewOperationBetweenAccounts(SecurityContextHolder.getContext().getAuthentication().getName(), label, category, description, amount, anotherName);
        return getUserPage(model);
    }

    @GetMapping("/cyclicOperation")
    public String getCyclicOperation() {
        return "cyclicOperation";
    }

    @PostMapping("/addCyclicOperation")
    public String postAddCyclicOperation(Model model, @RequestParam("label") String label, @RequestParam("category") String category, @RequestParam("description") String description, @RequestParam("amount") double amount, @RequestParam("period") int period) {
        model.addAttribute("userInfo", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        userPageService.saveNewCyclicOperation(SecurityContextHolder.getContext().getAuthentication().getName(), label, category, description, amount, period);
        return getUserPage(model);
    }

    @GetMapping("/deleteOperation")
    public String getDeleteOperation(Model model, @RequestParam("operationId") Long id) {
        model.addAttribute("userInfo", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        userPageService.deleteOperation(id);
        return getUserPage(model);
    }

    @GetMapping("/updateCyclicOperations")
    public String getUpdateCyclicOperations(Model model) {
        model.addAttribute("userInfo", userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        userPageService.checkIfOperationsNeedsUpdate(userInfoRepository.getUserInfoByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        return getUserPage(model);
    }
}
