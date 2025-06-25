package com.minhnphde180174.fu.hsf301assigment1.controller.admin;

import com.minhnphde180174.fu.hsf301assigment1.controller.BaseController;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api/v1/admin")
public class AdminController extends BaseController {


    @GetMapping("/dashboard")
    public String admin() {
        return "admin/dashboard"; // Trả về view admin/dashboard.html
    }


}
